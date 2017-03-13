package com.mallmall.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mallmall.manage.mapper.ContentMapper;
import com.mallmall.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content> {
	
	@Autowired
	private ContentMapper contentMapper;

	public PageInfo<Content> queryList(Long categoryId, Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		return new PageInfo<Content>(this.contentMapper.queryListOrderByUpdatedDesc(categoryId));
	}

}
