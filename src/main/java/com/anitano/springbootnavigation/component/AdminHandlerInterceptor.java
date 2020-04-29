package com.anitano.springbootnavigation.component;

import com.anitano.springbootnavigation.dataobject.WebsiteUser;
import com.anitano.springbootnavigation.enums.ResultCodeEnum;
import com.anitano.springbootnavigation.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * @ClassName: AdminHandlerInterceptor
 * @Author: 杨11352
 * @Date: 2020/4/12 13:20
 */
public class AdminHandlerInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*在业务处理器处理请求之前被调用。*/
        logger.info("[管理员拦截器]请求地址：{}",request.getRequestURI());
        //1、获取请求头传过来的 用户名和token
        String token = request.getHeader("Authorization");
        if(token == null){
            logger.info("获取到的token为空：{}",token);
            return false;
        }
        String[] usernameAndToken = token.split(":");
        String username = URLDecoder.decode(usernameAndToken[0], "UTF-8");
        logger.info("[管理员拦截器]用户名：{},token：{}",username,usernameAndToken[1]);
        //2、用户登录验证
        WebsiteUser user = userService.getUser(username);
        if(user != null){
            //3、管理员权限
            if(user.getRole() == 0){
                //4、验证token
                String checkToken = userService.checkToken(user.getUsername(), usernameAndToken[1]);
                if(checkToken !=null){
                    //验证通过
                    return true;
                }
            }
        }
        response.setHeader("Authorization", ResultCodeEnum.ADMIN_NOT_LOGGED_IN.getCode().toString());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        /*在业务处理器处理请求执行完成后，生成视图之前执行。*/
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        /*在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。*/
    }
}