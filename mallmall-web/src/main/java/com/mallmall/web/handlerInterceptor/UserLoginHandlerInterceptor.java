package com.mallmall.web.handlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mallmall.common.utils.CookieUtils;
import com.mallmall.sso.query.bean.User;
import com.mallmall.web.service.UserService;
import com.mallmall.web.threadLocal.UserThreadLocal;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;
	
	public static final String MALLMALL_TOKEN ="WEB_TOKEN";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String token = CookieUtils.getCookieValue(request, MALLMALL_TOKEN);
		if(StringUtils.isEmpty(token)){
			response.sendRedirect(this.userService.SSO_MALLMALL_URL+"/login.html");
			UserThreadLocal.set(null);
			return false;
		}
		User user = this.userService.queryUserByToken(token);
		if(null == user){
			response.sendRedirect(this.userService.SSO_MALLMALL_URL+"/login.html");
			UserThreadLocal.set(null);
			return false;
		}
		//放进 threadLocal
		UserThreadLocal.set(user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
