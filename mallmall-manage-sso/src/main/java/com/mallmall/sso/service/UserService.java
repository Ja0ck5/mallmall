package com.mallmall.sso.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.common.service.RedisService;
import com.mallmall.sso.mapper.UserMapper;
import com.mallmall.sso.pojo.User;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RedisService redisService;
	
	
	private static final Map<Integer,Boolean> TYPE = new HashMap<Integer, Boolean>();

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final String WEB_TOKEN_PREFIX= "TOKEN_";

	private static final Integer SECONDS = 60*30;
	
	static{
		TYPE.put(1, true);
		TYPE.put(2, true);
		TYPE.put(3, true);
	}
	
	/**
	 * 数据校验
	 * 
	 * @param param
	 * @param type
	 * @return
	 */
	public Boolean check(String param, Integer type) {
		if(!TYPE.containsKey(type)){
			return null;
		}
		User record = new User();
		switch (type) {
		case 1:
			record.setUsername(param);
			break;
		case 2:
			record.setPhone(param);
			break;
		case 3:
			record.setEmail(param);
			break;

		default:
			break;
		}
		User user = this.userMapper.selectOne(record);
		// true : 不可用
		return user != null;
	}

	public Boolean save(User user) {
		user.setId(null);
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		return 1 == this.userMapper.insert(user) ;
	}

	public String doLogin(User param) throws Exception {
		User record = new User();
		record.setUsername(param.getUsername());
		User user = this.userMapper.selectOne(record);
		if (null == user)
			return null;
		if (!StringUtils.equals(DigestUtils.md5Hex(param.getPassword()), user.getPassword()))
			return null;
		String token = DigestUtils.md5Hex(System.currentTimeMillis() + user.getUsername());
		this.redisService.set(WEB_TOKEN_PREFIX + token, MAPPER.writeValueAsString(user), SECONDS);
		return token;
	}

	public User queryUserByToken(String token) throws Exception{
		String key = WEB_TOKEN_PREFIX + token;
		String jsonData = this.redisService.get(key);
		User user = MAPPER.readValue(jsonData, User.class);
		this.redisService.expire(token, SECONDS);
		return user;
	}

}
