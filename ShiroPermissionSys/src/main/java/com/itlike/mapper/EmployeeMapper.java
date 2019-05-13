package com.itlike.mapper;

import com.itlike.domain.Employee;
import com.itlike.domain.QueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Employee record);

    Employee selectByPrimaryKey(Long id);

    List<Employee> selectAll(QueryVo vo);

    int updateByPrimaryKey(Employee record);
    /*设置员工离职状态*/
    void updateState(Long id);
    /*保存员工和角色 关系*/
    void insertEmployeeAndRoleRel(@Param("id") Long id, @Param("rid") Long rid);
    /*打破与角色之间关系*/
    void deleteRoleRel(Long id);
}