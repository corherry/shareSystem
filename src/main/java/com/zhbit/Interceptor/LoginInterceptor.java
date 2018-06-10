package com.zhbit.Interceptor;

import com.zhbit.service.TokenService;
import com.zhbit.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Resource(name = "tokenService")
    private TokenService tokenService;

    private List<String> exceptUrls;

    public List<String> getExceptUrls() {
        return exceptUrls;
    }

    public void setExceptUrls(List<String> exceptUrls) {
        this.exceptUrls = exceptUrls;
    }


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtil.getCookieValue(request, "token");
        String requestURI = request.getRequestURI();
        for (String url : exceptUrls) {
            if (url.endsWith("/**")) {
                if (requestURI.startsWith(url.substring(0, url.length() - 3))) {
                    return true;
                }
            } else if (requestURI.startsWith(url)) {
                return true;
            }
        }
        //token为空，没有登陆
        if (StringUtils.isBlank(token)) {
            response.setStatus(403);
            return false;
        }
        int id = tokenService.getUserByToken(token);
        //登陆过期
        if (id == 0) {
            response.setStatus(403);
            return false;
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    }
}
