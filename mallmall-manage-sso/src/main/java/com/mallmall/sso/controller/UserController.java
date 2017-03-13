package com.mallmall.sso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mallmall.common.utils.CookieUtils;
import com.mallmall.sso.pojo.User;
import com.mallmall.sso.service.UserService;

@RequestMapping
@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	private static final String MALLMALL_TOKEN ="WEB_TOKEN";

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public String go2Register() {
		return "register";
	}

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String go2Login() {
		return "login";
	}

	@RequestMapping(value = "user/check/{param}/{type}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> check(@PathVariable("param") String param,
										 @PathVariable("type") Integer type) {

		try {
			Boolean bool = this.userService.check(param,type);
			if(null == bool)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			return ResponseEntity.ok(bool);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="user/doRegister", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doRegister(@Valid User user,BindingResult bindingResult){
		Map<String,Object> result = new HashMap<>();
		List<String> msg = new ArrayList<>();
		if(bindingResult.hasErrors()){
			result.put("status", "400");
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			for (ObjectError objectError : allErrors) {
				msg.add(objectError.getDefaultMessage());
			}
			result.put("msg", StringUtils.join(msg, "|"));
			return result;
		}
		try {
			Boolean bool = this.userService.save(user);
			if(bool){
				result.put("status", "200");
			}else{
				result.put("status", "400");
			}
		} catch (Exception e) {
			result.put("status", "500");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="user/doLogin", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> doLogin(User user,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result = new HashMap<>();
		try {
			String token = this.userService.doLogin(user);
			if(null == token ){
				result.put("status", 400);
			}else{
				result.put("status", 200);
				CookieUtils.setCookie(request, response, MALLMALL_TOKEN, token);
			}
		} catch (Exception e) {
			result.put("status", 500);
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "user/{token}", method = RequestMethod.GET)
	public ResponseEntity<User> query(@PathVariable("token") String token) {
		//禁用此服务，改用新的 dubbo 中的服务
		/*try {
			User user = this.userService.queryUserByToken(token);
			if(null == user)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);*/
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
}
