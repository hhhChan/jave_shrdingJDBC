package com.can.service;

import com.can.pojo.Department;

import java.util.List;

public interface DepartmentService {

    public List<Department> getDepartments();

    public Department getDepartmentById( Long id);
}
