package com.can;

import com.can.mapper.EmployeeMapper;
import com.can.pojo.Employee;
import com.can.service.EmployeeService;
import com.can.service.impl.DepartmentServiceImpl;
import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringbootWebApplicationTests {
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Test
    void contextLoads(){
        long[] a = new long[10];
        for (int i=0; i<10; i++) {
            a[i] = idGenerator.generateId().longValue();
        }
        for (int i=0; i<10; i++) {
            Employee employee = new Employee();
            employee.setId(a[i]+1);
            employee.setDepartmentName("教学楼" + (i%6 + 1));
            employee.setEmail("test"+ i +"@.com");
            employee.setGender(i%2);
            employee.setLastName("test" + i);
            employeeMapper.putEmployee(employee);
        }
    }

}
