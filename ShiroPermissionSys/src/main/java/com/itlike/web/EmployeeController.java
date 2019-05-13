package com.itlike.web;

import com.itlike.domain.AjaxRes;
import com.itlike.domain.Employee;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;
import com.itlike.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmployeeController {
    /*注入业务层*/
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/employee")
    public String employee(){
        return "employee";
    }

    @RequestMapping("/employeeList")
    @ResponseBody
    public PageListRes employeeList(QueryVo vo){
        System.out.println(vo);
        /*调用业务层查询员工*/
        PageListRes pageListRes = employeeService.getEmployee(vo);
        return pageListRes;
    }

    /*接收员工添加表单*/
    @RequestMapping("/saveEmployee")
    @ResponseBody
    public AjaxRes saveEmployee(Employee employee){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            /*调用业务层,保存用户*/
            employee.setState(true);
            employeeService.saveEmployee(employee);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("保存失败");
        }
        return ajaxRes;
    }

    /*接收更新员工请求*/
    @RequestMapping("/updateEmployee")
    @ResponseBody
    public AjaxRes updateEmployee(Employee employee){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            /*调用业务层,更新员工*/
            employeeService.updateEmployee(employee);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("更新失败");
        }
        return ajaxRes;
    }


    /*接收离职操作请求*/
    @RequestMapping("/updateState")
    @ResponseBody
    public AjaxRes updateState(Long id){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            /*调用业务层,设置员工离职状态*/
            employeeService.updateState(id);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            System.out.println(e);
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("更新失败");
        }
        return ajaxRes;
    }




}
