package com.can.service.impl;

import com.can.mapper.EmployeeMapper;
import com.can.pojo.Employee;
import com.can.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public void saveEmployee(Employee employee) {
        if (employee.getId() == null) {
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
    public Employee getEmployeeById(Integer id) {
        return employeeMapper.getEmployeeById(id);
    }

    @Override
    public void delete(Integer id) {
            employeeMapper.removeById(id);
        }
}
