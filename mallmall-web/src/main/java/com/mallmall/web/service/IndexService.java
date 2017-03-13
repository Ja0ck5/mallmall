package com.mallmall.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.common.bean.EasyUIResult;
import com.mallmall.common.service.ApiService;
import com.mallmall.manage.pojo.Content;

@Service
public class IndexService {

	@Value("${MANAGE_MALLMALL_URL}")
	private String MANAGE_MALLMALL_URL;
	
	@Value("${INDEX_AD1_URL}")
	private String INDEX_AD1_URL;
	
	@Autowired
	private ApiService apiService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	/**
	 * 查询大广告位的数据
	 * 
	 * @return
	 */
/*	public String getIndexAd1() {
		//通过调用后台系统的接口获取数据
		String url = MANAGE_MALLMALL_URL + INDEX_AD1_URL;
		try {
			String jsonData = this.apiService.doGet(url);
			//解析 json,获取数据，封装成前端需要的格式
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			ArrayNode arrayNode = (ArrayNode) jsonNode.get("rows");
			List<Map<String,Object>> result = new ArrayList<>();
			for (JsonNode node : arrayNode) {
				Map<String,Object> map = new LinkedHashMap<>();
				map.put("srcB", node.get("pic").asText());
				map.put("height", 240);
				map.put("alt", node.get("title").asText());
				map.put("width", 670);
				map.put("src", map.get("srcB"));
				map.put("widthB", 550);
				map.put("href", node.get("url").asText());
				map.put("heightB", 240);
				result.add(map);
			}
			return MAPPER.writeValueAsString(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
*/
	public String getIndexAd1() {
		//通过调用后台系统的接口获取数据
		String url = MANAGE_MALLMALL_URL + INDEX_AD1_URL;
		try {
			String jsonData = this.apiService.doGet(url);
			//解析 json,获取数据，封装成前端需要的格式
			EasyUIResult easyUiResult = EasyUIResult.formatToList(jsonData, Content.class);
			List<Content> contents = (List<Content>) easyUiResult.getRows();
			List<Map<String,Object>> result = new ArrayList<>();
			for (Content content: contents) {
				Map<String,Object> map = new LinkedHashMap<>();
				map.put("srcB", content.getPic());
				map.put("height", 240);
				map.put("alt", content.getTitle());
				map.put("width", 670);
				map.put("src", content.getPic());
				map.put("widthB", 550);
				map.put("href", content.getUrl());
				map.put("heightB", 240);
				result.add(map);
			}
			return MAPPER.writeValueAsString(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
