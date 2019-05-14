package com.itlike.service;

import com.itlike.domain.Employee;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;

import java.util.ArrayList;
import java.util.List;

public interface EmployeeService {
    /*查询员工*/
    public PageListRes getEmployee(QueryVo vo);
    /*保存员工*/
    void saveEmployee(Employee employee);
    /*更新员工*/
    void updateEmployee(Employee employee);
    /*设置员工离职状态*/
    void updateState(Long id);

    Employee getEmployeeWithUsername(String username);

    ArrayList<String> getRolesById(Long id);

    ArrayList<String> getPermissionsById(Long id);

    List<Employee> getAllEmployee();
}
