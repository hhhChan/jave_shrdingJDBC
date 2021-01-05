package com.can;

import com.can.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringbootWebApplicationTests {
    @Autowired
    DepartmentServiceImpl userService;

    @Test
    void contextLoads(){
        System.out.println(userService.getDepartments());
    }

}
