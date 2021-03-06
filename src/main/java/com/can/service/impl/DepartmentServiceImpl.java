package com.can.service.impl;

import com.can.annatations.CanLimiter;
import com.can.mapper.DepartmentMapper;
import com.can.pojo.Department;
import com.can.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    @CanLimiter
    public List<Department> getDepartments() {
        return departmentMapper.getDepartments();
    }

    @Override
    @CanLimiter
    @Cacheable(value = "department", key = "#id")
    public Department getDepartmentById(Long id) {
        return departmentMapper.getDepartmentById(id);
    }
}
