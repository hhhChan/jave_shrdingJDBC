package com.can.controller;

import com.can.annatations.CanLimiter;
import com.can.limiter.JedisRateLimiterSeter;
import com.can.pojo.Department;
import com.can.service.impl.DepartmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class DepartmentController {
    @Autowired
    DepartmentServiceImpl departmentService;

    @CanLimiter()
    @RequestMapping("/depar")
    public String dep(Model model){
        Collection<Department> departments = departmentService.getDepartments();
        model.addAttribute("deps",departments);
        return "dep/depar";
    }
}
