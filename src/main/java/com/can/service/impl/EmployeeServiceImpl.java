package com.can.service.impl;

import com.can.annatations.CanCache;
import com.can.annatations.CanLimiter;
import com.can.mapper.EmployeeMapper;
import com.can.pojo.Employee;
import com.can.service.EmployeeService;
import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.sankuai.inf.leaf.common.Status;
import com.sankuai.inf.leaf.service.SnowflakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.Collection;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private SnowflakeService snowflakeService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct //  --- 启动的时候去初始化布隆过滤器。
    public void init(){
        // 1. 加载所有数据
        Collection<Employee> employees = getEmployees();
        for (Employee e : employees) {
            Long userId = e.getId();
            int hashValue = Math.abs(userId.hashCode());
            long index = (long) (hashValue % Math.pow(2, 32)); // 元素  和 数组的映射
            // 设置Redis里面二进制数据中的值，对应位置 为 1
            redisTemplate.opsForValue().setBit("emp_bloom_filter", index, true);
        }
    }

    @Override
    @CacheEvict(value = "employee", key = "#employee.id")
    public void saveEmployee(Employee employee) {
        if (employee.getId() == null) {
            com.sankuai.inf.leaf.common.Result r =  snowflakeService.getId("id");
            if(r.getStatus() != Status.SUCCESS) {
                System.out.println("id创建失败");
                return;
            }
            //employee.setId(idGenerator.generateId().longValue());
            employee.setId(r.getId());
            int hashValue = Math.abs(employee.getId().hashCode());
            long index = (long) (hashValue % Math.pow(2, 32)); // 元素  和 数组的映射
            // 设置Redis里面二进制数据中的值，对应位置 为 1
            redisTemplate.opsForValue().setBit("emp_bloom_filter", index, true);
            employeeMapper.putEmployee(employee);
        } else {
            employeeMapper.updateEmployee(employee);
        }
    }

    @Override
    public Collection<Employee> getEmployees() {
        return employeeMapper.getEmployees();
    }

    @Override
    @CanCache(value = "employee", key = "#id")
    public Employee getEmployeeById(Long id) {
        int hashValue = Math.abs(id.hashCode());
        long index = (long) (hashValue % Math.pow(2, 32)); // 元素  和 数组的映射
        Boolean result = redisTemplate.opsForValue().getBit("emp_bloom_filter", index);
        if(!result) {
            System.out.println("数据不存在" + id);
            Employee e = new Employee();
            e.setId(0L);
            e.setLastName("不存在");
            e.setGender(0);
            e.setEmail("null@null");
            return e;
        }
        return employeeMapper.getEmployeeById(id);
    }

    @Override
    @CacheEvict(value = "employee", key = "#id")
    public void delete(Long id) {
            employeeMapper.removeById(id);
        }
}
