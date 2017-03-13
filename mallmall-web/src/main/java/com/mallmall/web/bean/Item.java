package com.mallmall.web.bean;

import org.apache.commons.lang3.StringUtils;

public class Item extends com.mallmall.manage.pojo.Item {

	public String[] getImages(){
		return super.getImage() == null ? null : StringUtils.split(super.getImage(), ',');
	}
}
