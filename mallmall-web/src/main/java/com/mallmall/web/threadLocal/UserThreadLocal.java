package com.mallmall.web.threadLocal;

import com.mallmall.sso.query.bean.User;

public class UserThreadLocal {
	
	private static final ThreadLocal<User> USERLOCAL = new ThreadLocal<User>();
	
	public static void set(User user){
		USERLOCAL.set(user);
	}
	
	public static User get(){
		return USERLOCAL.get();
	}
}
