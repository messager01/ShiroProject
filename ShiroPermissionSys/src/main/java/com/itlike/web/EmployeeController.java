package com.itlike.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itlike.domain.AjaxRes;
import com.itlike.domain.Employee;
import com.itlike.domain.PageListRes;
import com.itlike.domain.QueryVo;
import com.itlike.service.EmployeeService;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class EmployeeController {
    /*注入业务层*/
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/employee")
    @RequiresPermissions("employee:index")
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
    @RequiresPermissions("employee:add")
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
    @RequiresPermissions("employee:edit")
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
    @RequiresPermissions("employee:delete")
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

        @ExceptionHandler(AuthorizationException.class)   // 处理未授权异常
        public void handleShiroException(HandlerMethod method, HttpServletResponse response) throws Exception {  // 会把发生异常的方法传过来
                //跳转到一个界面
            //  判断当前请求是不是一个json 请求  如果是  返回json给浏览器  让它自己跳转
            //  贴有@responsebody的是json请求   没有@respobody的是 服务器内部请求

            // 获取方法上的注解
            ResponseBody methodAnnotation = method.getMethodAnnotation(ResponseBody.class);
            if (methodAnnotation != null){
                // json 请求    返回到浏览器
                AjaxRes ajaxRes = new AjaxRes();
                ajaxRes.setSuccess(false);
                ajaxRes.setMsg("暂未授权");
                String s = new ObjectMapper().writeValueAsString(ajaxRes);
                response.setCharacterEncoding("utf-8");
                response.getWriter().print(s);
            }else {
                // 重定向
                response.sendRedirect("nopermission.jsp");
            }
        }


}
