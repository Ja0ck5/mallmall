package com.mallmall.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 负责页面跳转
 * @author Administrator
 *
 */
@RequestMapping("page")
@Controller
public class PageController {

	/**
	 * 通用页面跳转
	 * @param pageName
	 * @return
	 */
	@RequestMapping(value = "{pageName}",method=RequestMethod.GET)
	public String toPage(@PathVariable("pageName")String pageName){
		
		return pageName;
	}
}
