package com.atguigu.web;

import com.atguigu.utils.JdbcUtils;
import javax.servlet.*;
import java.io.IOException;

public class TransactionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            chain.doFilter(request, response);
            //没有异常,执行commit提交方法
            JDBCutils.commitAndClose(); // 提交事务
        } catch (Exception e) {
            //存在异常执行rollback回滚方法
            JDBCutils.rollbackAndClose();//回滚事务
            e.printStackTrace();
            throw new RuntimeException(e); //同时把异常抛给TomCat服务器,让服务器进行错误页面展示.
        }
    }

    @Override
    public void destroy() {

    }
}