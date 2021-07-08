package com.can;

import com.can.mapper.EmployeeMapper;
import com.can.pojo.Employee;
import com.can.service.EmployeeService;
import com.can.service.impl.DepartmentServiceImpl;
import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.sankuai.inf.leaf.service.SnowflakeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringbootWebApplicationTests {
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    private SnowflakeService snowflakeService;
    @Autowired
    private IdGenerator idGenerator;
    @Test
    void contextLoads(){
        for (int i=0; i<100; i++) {
            Employee employee = new Employee();
            com.sankuai.inf.leaf.common.Result r =  snowflakeService.getId("id");
            employee.setId(r.getId());
            employee.setDepartmentName("school");
            employee.setEmail("test"+ i +"@.com");
            employee.setGender(i%2);
            employee.setLastName("test" + i);
            employeeMapper.putEmployee(employee);
        }
    }

}
