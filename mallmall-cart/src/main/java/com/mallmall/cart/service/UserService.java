package com.mallmall.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mallmall.sso.query.api.UserQueryService;
import com.mallmall.sso.query.bean.User;

@Service
public class UserService {

//	@Autowired
//	private ApiService apiService;

	@Autowired
	private UserQueryService userQueryService;
	
	@Value("${SSO_MALLMALL_URL}")
	public String SSO_MALLMALL_URL;
	
//	private static final ObjectMapper MAPPER = new ObjectMapper();
	public User queryUserByToken(String token) {
		/*try {
			String url = SSO_MALLMALL_URL + "/service/user/" + token;
			String jsonData = this.apiService.doGet(url);
			if (null == jsonData)
				return null;
			return MAPPER.readValue(jsonData, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return userQueryService.queryUserByToken(token);
	}
}
