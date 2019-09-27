package com.qingcheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.pojo.system.LoginLog;
import com.qingcheng.service.system.LoginLogService;
import com.qingcheng.utils.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 登录日志
 *
 */
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Reference
    private LoginLogService loginLogService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("登录成功!!!!!");
        String name = authentication.getName();
        String ip = request.getRemoteAddr();

        LoginLog loginLog = new LoginLog();
        loginLog.setLoginName(name);//当前登录的管理员
        loginLog.setLoginTime(new Date());//登录时间
        loginLog.setIp(ip);//管理员登录IP地址
        loginLog.setLocation(WebUtil.getCityByIP(ip));//地区
        String header = request.getHeader("user-agent"); //获取浏览器响应头名称
        System.out.println(header);
        loginLog.setBrowserName(WebUtil.getBrowserName(header));///浏览器名称


        loginLogService.add(loginLog);
        //重定向跳转到main.html
        request.getRequestDispatcher("/main.html").forward(request,response);
    }
}
