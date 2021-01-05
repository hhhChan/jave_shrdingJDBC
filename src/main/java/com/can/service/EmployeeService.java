package com.can.service;

import com.can.pojo.Employee;

import java.util.Collection;

public interface EmployeeService {

    public void saveEmployee( Employee employee);

    public Collection<Employee> getEmployees();

    public Employee getEmployeeById( Integer id);

    public void delete( Integer id);
}
