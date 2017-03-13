package com.mallmall.sso.query.api;

import com.mallmall.sso.query.bean.User;

/**
 * 定义服务接口
 * @author Ja0ck5
 *
 */
public interface UserQueryService {
	
	public User queryUserByToken(String token);

}
