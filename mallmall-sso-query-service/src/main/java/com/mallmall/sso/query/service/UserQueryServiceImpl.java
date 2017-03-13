package com.mallmall.sso.query.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.common.service.RedisService;
import com.mallmall.sso.query.api.UserQueryService;
import com.mallmall.sso.query.bean.User;

@Service
public class UserQueryServiceImpl implements UserQueryService{

	@Autowired
	private RedisService redisService;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final String WEB_TOKEN_PREFIX= "TOKEN_";

	private static final Integer SECONDS = 60*30;
	

	public User queryUserByToken(String token){
		String key = WEB_TOKEN_PREFIX + token;
		String jsonData = this.redisService.get(key);
		
		if(StringUtils.isEmpty(jsonData))
			return null;
		try {
			this.redisService.expire(token, SECONDS);
			return  MAPPER.readValue(jsonData, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
