package com.can.controller;

import com.can.pojo.Department;
import com.can.pojo.Employee;
import com.can.service.impl.DepartmentServiceImpl;
import com.can.service.impl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class EmployeeController {

    @Autowired
    DepartmentServiceImpl departmentServiceImpl;
    @Autowired
    EmployeeServiceImpl employeeServiceImpl;

    @RequestMapping("/emps")
    public String list(Model model){
        Collection<Employee> employees = employeeServiceImpl.getEmployees();
        model.addAttribute("emps",employees);
        return "emp/list";
    }

    @GetMapping("/emps/emp")
    public String toAddpage(Model model){
        Collection<Department> departments = departmentServiceImpl.getDepartments();
        model.addAttribute("departments", departments);
        return "emp/add";
    }

    @PostMapping("/emps/emp")
    public String addpEmp(Employee employee){
        employeeServiceImpl.saveEmployee(employee);
        return "redirect:/emps";
    }

    @GetMapping("/emps/{id}")
    public String toUpdateEmp(@PathVariable("id")long id, Model model){
        Employee employee = employeeServiceImpl.getEmployeeById((Long)id);
        Collection<Department> departments = departmentServiceImpl.getDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("emp", employee);
        return "emp/update";
    }

    @PostMapping("/emps/updateEmp")
    public String updateEmp(Employee employee){
        employeeServiceImpl.saveEmployee(employee);
        return "redirect:/emps";
    }

    @GetMapping("/emps/delemp/{id}")
    public String deleteEmp(@PathVariable("id") long id){
        employeeServiceImpl.delete((long)id);
        return "redirect:/emps";
    }
}
