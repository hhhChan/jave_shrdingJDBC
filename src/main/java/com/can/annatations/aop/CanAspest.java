package com.can.annatations.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.can.annatations.CanCache;
import com.can.pojo.Employee;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Aspect
public class CanAspest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.can.annatations.CanCache)")
        public void cachePointcut() {
    }

    @Autowired
    private CaffeineCache caffeine;

    Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    @Around("cachePointcut()")
    public Object doCache(ProceedingJoinPoint joinPoint) {
        Object value = null;
        ReentrantLock lock = new ReentrantLock();
        Boolean isLock = false;
        String key = "";
        try {
            // 0-1、 当前方法上注解的内容
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = joinPoint.getTarget().getClass().getMethod(signature.getName(), signature.getMethod().getParameterTypes());
            CanCache cacheAnnotation = method.getAnnotation(CanCache.class);
            String keyEl = cacheAnnotation.key();
            String prefix = cacheAnnotation.value();
            // 0-2、 前提条件：拿到作为key的依据  - 解析springEL表达式
            // 创建解析器
            ExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(keyEl);
            EvaluationContext context = new StandardEvaluationContext();
            // 添加参数
            Object[] args = joinPoint.getArgs();
            DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(method);
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i].toString());
            }
            // 解析
            key = prefix + "::" + expression.getValue(context).toString();
            // 1、 判定缓存中是否存在
            //先一级缓存
            Cache.ValueWrapper valueWrapper = caffeine.get(key);
            System.out.println("查询一级缓存 key:{" + key + "},返回值是:{" + valueWrapper + "}");
            if (valueWrapper != null) {
                value = valueWrapper.get();
                System.out.println("一级缓存不为空" + value);
                return value;
            }

            System.out.println("------------dada-----------------");
            //去redis缓存
            value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                System.out.println("从redis缓存中读到值： " + value);
                value = JSON.parseObject((String) value, method.getReturnType());
                caffeine.put(key, value);
                return value;
            }

            //2、 如何应对 大量并发 -- 减少并发 --
            // 不同的主播ID 不同的锁
            if(lockMap.putIfAbsent(key, lock) != null) {
                lock = lockMap.get(key);
            }
            lock.lock();// 没拿到锁 就等待
            isLock = true;
            //3、当拿到锁在进行判断一次，内存是否有
            value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                System.out.println("从缓存中读到值： " + value);
                value = JSON.parseObject((String) value, method.getReturnType());
                caffeine.put(key, value);
                return value;
            }

            //4、不存在则执行方法
            value = joinPoint.proceed();

            //5、 同步存储value到缓存。 可使用SerializerFeature.WriteClassName 自行实现RedisSerializer接口
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
            caffeine.put(key, value);
            // redis 缓存过期机制【数据标签】 - 7天，30天，认证大咖--不过期处理
            redisTemplate.expire(key, new Random().nextInt(7 * 24), TimeUnit.HOURS); // 需要手动设
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (isLock) {
                if(!lock.hasQueuedThreads()) {
                    // 当锁的最后一个释放的时候，删除掉
                    lockMap.remove(key);
                }
                lock.unlock();
            }

        }
        return value;
    }
}
