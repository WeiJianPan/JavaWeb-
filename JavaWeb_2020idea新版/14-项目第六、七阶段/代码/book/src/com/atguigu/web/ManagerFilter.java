package com.atguigu.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ManagerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //1.获取Session域中的user对象数据
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Object user = httpServletRequest.getSession().getAttribute("user");

        //2.如果Session域中user对象数据为空
        if(user == null){
            //请求转发到登录页面
            httpServletRequest.getRequestDispatcher("/pages/user/login.jsp").forward(request, response);
            return;
        }else{
            //user对象不为空，程序继续执行。
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}