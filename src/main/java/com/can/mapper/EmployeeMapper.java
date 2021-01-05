package com.can.mapper;

import com.can.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface EmployeeMapper {
    public List<Employee> getEmployees();

    Employee getEmployeeById(Integer id);
    void updateEmployee(Employee employee);
    void removeById(Integer id);

    void putEmployee(Employee employee);
}
