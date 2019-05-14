package com.itlike.web.realm;

import com.itlike.domain.Employee;
import com.itlike.service.EmployeeService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class EmployeeRealm extends AuthorizingRealm {

    @Autowired
    EmployeeService employeeService;
    //  认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
       // System.out.println("来到认证");
        String username = (String) token.getPrincipal();
       // System.out.println(username);
        //查询数据库
        Employee employee = employeeService.getEmployeeWithUsername(username);
        if(employee == null){
            // 没有查到用户
            return null;
        }
        //查到用户            //参数: 主体     正确密码        （盐）    当前relam的名字
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(employee, employee.getPassword(), ByteSource.Util.bytes("lulu"), this.getName());
        return info;

    }


    //授权

    /*
    * 什么时候会调用
    * 1. 发现访问路径对应的方法上面 有授权的注解  就会调用doGetAuthorizationInfo
    * 2. 页面当中有授权标签  也会调用doGetAuthorizationInfo
    * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
       // System.out.println("授权调用");
        // 获取用户的身份信息
        Employee employee = (Employee) principals.getPrimaryPrincipal();  // 认证过后传过来的第一个参数
        // 根据当前用户查询角色和权限
        ArrayList<String> roles = new ArrayList<>();
        ArrayList<String> permissions = new ArrayList<>();

        //判断当前用户是不是管理员
        if(employee.getAdmin()){
            // 拥有所有权限
            permissions.add("*:*");
        }else {
            // 查询角色
            roles = employeeService.getRolesById(employee.getId());

            System.out.println(roles);
            // 查询权限
            permissions = employeeService.getPermissionsById(employee.getId());
            System.out.println(permissions);
        }


        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(roles);
        info.addStringPermissions(permissions);
        return info;
    }


}
