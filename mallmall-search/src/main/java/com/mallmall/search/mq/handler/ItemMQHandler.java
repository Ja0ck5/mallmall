package com.mallmall.search.mq.handler;


import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.search.bean.Item;
import com.mallmall.search.service.ItemService;

@Component
public class ItemMQHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemMQHandler.class);
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private HttpSolrServer httpSolrServer;
	
	@Autowired
	private ItemService itemService;
	
	public void execute(String msg) {
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("recieve the MSG = {}",msg);
		try {
			JsonNode jsonNode = MAPPER.readTree(msg);
			Long itemId = jsonNode.get("itemId").asLong();
			String type = jsonNode.get("type").asText();
			if(StringUtils.equals(type,"insert") || StringUtils.equals(type,"update")){
				//查询商品数据
				Item item = this.itemService.queryItemById(itemId);
				this.httpSolrServer.addBean(item);
			}else if(StringUtils.equals(type,"delete") ){
				this.httpSolrServer.deleteById(String.valueOf(itemId));
			}
			this.httpSolrServer.commit();
		} catch (Exception e) {
			LOGGER.error("process error. The MSG is = "+msg,e);
		}
		
	}
}
