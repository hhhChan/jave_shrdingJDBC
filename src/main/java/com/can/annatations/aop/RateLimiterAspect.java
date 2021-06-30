package com.can.annatations.aop;

import com.can.annatations.CanLimiter;
import com.can.limiter.JedisRateLimiter;
import com.can.limiter.JedisRateLimiterSeter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
public class RateLimiterAspect {
    private Logger logger = LoggerFactory.getLogger(RateLimiterAspect.class);
    JedisRateLimiterSeter jrls;

    @PostConstruct
    public void initLUA() {
        jrls = new JedisRateLimiterSeter("RateLimitSet.lua", "Can-RateLimit", "3");
    }

    @Pointcut("@annotation(com.can.annatations.CanLimiter)")
    public void limitPointcut() {
    }

    @Around("limitPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        CanLimiter limiter = getCanLimiter(joinPoint);
        if (limiter != null) {
            JedisRateLimiter jrl = new JedisRateLimiter("RateLimit.lua", limiter.key());
            if (jrl.acquire()) {
                logger.info("处理查询：{}, {}", limiter.key(), System.currentTimeMillis() / 1000);
            } else {
                logger.info("被分布式限流了 {}", System.currentTimeMillis() / 1000);
                responseResult(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse(), 500, limiter.msg());
                return null;
            }
        }
        return joinPoint.proceed();
    }

    /**
     * 获取注解对象
     *
     * @param joinPoint 对象
     * @return ten LogAnnotation
     */
    private CanLimiter getCanLimiter(final JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        String name = joinPoint.getSignature().getName();
        if (!StringUtils.isEmpty(name)) {
            for (Method method : methods) {
                CanLimiter annotation = method.getAnnotation(CanLimiter.class);
                if (!Objects.isNull(annotation) && name.equals(method.getName())) {
                    return annotation;
                }
            }
        }
        return null;
    }
    /**
     * 自定义响应结果
     *
     * @param response 响应
     * @param code     响应码
     * @param message  响应信息
     */
    private void responseResult(HttpServletResponse response, Integer code, String message) {
        response.resetBuffer();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.println("{\"code\":" + code + " ,\"message\" :\"" + message + "\"}");
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(" 输入响应出错 e = {}", e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}

