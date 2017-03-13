package com.mallmall.manage.mapper;

import java.util.List;

import com.github.abel533.mapper.Mapper;
import com.mallmall.manage.pojo.Content;

public interface ContentMapper extends Mapper<Content> {

	List<Content> queryListOrderByUpdatedDesc(Long categoryId);

}
