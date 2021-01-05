package com.can.mapper;

import com.can.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DepartmentMapper {
    List<Department> getDepartments();
    public Department getDepartmentById(Long id);
}
