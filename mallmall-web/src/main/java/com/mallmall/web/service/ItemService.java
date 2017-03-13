package com.mallmall.web.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mallmall.common.service.ApiService;
import com.mallmall.common.service.RedisService;
import com.mallmall.manage.pojo.ItemDesc;
import com.mallmall.manage.pojo.ItemParamItem;
import com.mallmall.web.bean.Item;

@Service
public class ItemService {

	@Autowired
	private ApiService apiService;

	@Autowired
	private RedisService redisService;
	
	@Value("${MANAGE_MALLMALL_URL}")
	private String MANAGE_MALLMALL_URL;

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Integer SECONDS = 60 * 60 *24;

	public static final String REDIS_WEB_KEY = "MALLMALL_WEB_ITEM_DETAIL_";
	
	public Item queryItemById(Long itemId) {
		String key = REDIS_WEB_KEY+itemId;
		try {
			String jsonData = this.redisService.get(key);
			if(StringUtils.isNotEmpty(jsonData))
				return MAPPER.readValue(jsonData, Item.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = MANAGE_MALLMALL_URL + "/rest/item/" + itemId;
		try {
			String jsonData = this.apiService.doGet(url);
			if (StringUtils.isEmpty(jsonData))
				return null;
			//cache
			this.redisService.set(key, jsonData, SECONDS);
			return MAPPER.readValue(jsonData, Item.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询商品描述
	 * 
	 * @param itemId
	 * @return
	 */
	public ItemDesc queryItemDescByItemId(Long itemId) {
		String url = MANAGE_MALLMALL_URL + "/rest/item/desc/" + itemId;
		try {
			String jsonData = this.apiService.doGet(url);
			if (StringUtils.isEmpty(jsonData))
				return null;
			return MAPPER.readValue(jsonData, ItemDesc.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 商品规格参数
	 * 
	 * @param itemId
	 * @return
	 */
	public String queryItemParamByItemId(Long itemId) {
		/*<div class="Ptable">
		<div class="Ptable-item">
			<h3>电池信息</h3>
			<dl>
				<dt>电池是否可拆卸</dt>
				<dd>否</dd>
				<dt>充电器</dt>
				<dd>9V/2A</dd>
				<dt>闪充</dt>
				<dd>支持</dd>
			</dl>
		</div>
	</div>*/
		String url = MANAGE_MALLMALL_URL + "/rest/item/param/item/" + itemId;
		try {
			String jsonData = this.apiService.doGet(url);
			if (StringUtils.isEmpty(jsonData))
				return null;
			ItemParamItem itemParamItem = MAPPER.readValue(jsonData, ItemParamItem.class);
			String paramData = itemParamItem.getParamData();
			ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);
			StringBuilder sb = new StringBuilder();
			sb.append("<div class='Ptable'>");
			for (JsonNode jsonNode : arrayNode) {
				String group = jsonNode.get("group").asText();
				sb.append("<div class='Ptable-item'><h3>"+group+"</h3><dl>");
				ArrayNode params = (ArrayNode) jsonNode.get("params");
				for (JsonNode param : params) {
					sb.append("<dt>"+param.get("k").asText()+"</dt><dd>"+param.get("v").asText()+"</dd>");
				}
				sb.append("</dl></div>");
			}
			sb.append("</div>");
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}

}
