package com.mallmall.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.mallmall.common.bean.EasyUIResult;
import com.mallmall.manage.pojo.Content;
import com.mallmall.manage.service.ContentService;

@RequestMapping("content")
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> saveContent(Content content) {
		try {
			this.contentService.save(content);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<EasyUIResult> queryList(@RequestParam("categoryId")Long categoryId,
			@RequestParam(value="page",defaultValue="1")Integer page,
			@RequestParam(value="rows",defaultValue="10")Integer rows) {
		try {
			 PageInfo<Content> pageInfo = this.contentService.queryList(categoryId,page,rows);
			return ResponseEntity.ok(new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		
	}

}
