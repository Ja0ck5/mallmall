package com.mallmall.search.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.common.service.ApiService;
import com.mallmall.search.bean.Item;

@Service
public class ItemService {

	@Autowired
	private ApiService apiService;

	@Value("${MANAGE_MALLMALL_URL}")
	private String MANAGE_MALLMALL_URL;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	
	public Item queryItemById(Long itemId) {
		String url = MANAGE_MALLMALL_URL + "/rest/item/" + itemId;
		try {
			String jsonData = this.apiService.doGet(url);
			if (StringUtils.isEmpty(jsonData))
				return null;
			return MAPPER.readValue(jsonData, Item.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
