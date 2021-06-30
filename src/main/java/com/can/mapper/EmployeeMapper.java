package com.can.mapper;

import com.can.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface EmployeeMapper {
    public List<Employee> getEmployees();

    public Employee getEmployeeById(long id);
    void updateEmployee(Employee employee);
    void removeById(long id);

    void putEmployee(Employee employee);
}
