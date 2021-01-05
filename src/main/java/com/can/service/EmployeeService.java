package com.can.service;

import com.can.pojo.Employee;

import java.util.Collection;

public interface EmployeeService {

    public void saveEmployee( Employee employee);

    public Collection<Employee> getEmployees();

    public Employee getEmployeeById(Long id);

    public void delete(Long id);
}
