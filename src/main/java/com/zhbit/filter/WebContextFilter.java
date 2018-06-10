package com.zhbit.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebContextFilter implements Filter {


    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;

        res.setContentType("text/html;charset=UTF-8");

        res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:65359");

        res.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, DELETE, PUT, PATCH");

        res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");

        res.setHeader("Access-Control-Allow-Credentials", "true");

        res.setHeader("XDomainRequestAllowed","1");

        chain.doFilter(request, response);
    }


    public void destroy() {
        // TODO Auto-generated method stub

    }

}