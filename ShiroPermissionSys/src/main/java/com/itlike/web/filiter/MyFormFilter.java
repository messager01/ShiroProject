package com.itlike.web.filiter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itlike.domain.AjaxRes;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/*
*  表单认证过滤器  用来监听认证是否通过
* */
public class MyFormFilter extends FormAuthenticationFilter {

    // 认证成功时会调用
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {

        //设置编码
        response.setCharacterEncoding("utf-8");

        //响应给浏览器
        //System.out.println("成功");
        AjaxRes ajaxRes = new AjaxRes();
        ajaxRes.setSuccess(true);
        ajaxRes.setMsg("登录成功");
        // 把对象转成json格式的字符串
        String s = new ObjectMapper().writeValueAsString(ajaxRes);
        response.getWriter().print(s);  // 只能传输字符串
        return false;  // 不管是否成功  直接停止  手动控制接下来的转发
    }

    //  认证失败调用
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e,
                                     ServletRequest request,
                                     ServletResponse response) {

        //设置编码
        response.setCharacterEncoding("utf-8");

        AjaxRes ajaxRes = new AjaxRes();
        ajaxRes.setSuccess(false);
        if(e != null){
            // 获取异常名称
            String name = e.getClass().getName();
            if ( name.equals(UnknownAccountException.class.getName())){
                ajaxRes.setMsg("账号不存在");
            }else if(name.equals(IncorrectCredentialsException.class.getName())){
                ajaxRes.setMsg("密码错误");
            }else {
                ajaxRes.setMsg("未知异常");
            }
        }
        try {
            String s = new ObjectMapper().writeValueAsString(ajaxRes);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(s);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return false;  // 不管是否成功  直接停止  手动控制接下来的转发
    }
}
