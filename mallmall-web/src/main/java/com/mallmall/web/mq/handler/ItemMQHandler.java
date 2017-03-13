package com.mallmall.web.mq.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.common.service.RedisService;
import com.mallmall.web.service.ItemService;

@Component
public class ItemMQHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemMQHandler.class);
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
	private RedisService redisService;
	
	public void execute(String msg) {
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("recieve the MSG = {}",msg);
		try {
			JsonNode jsonNode = MAPPER.readTree(msg);
			Long itemId = jsonNode .get("itemId").asLong();
			this.redisService.del(ItemService.REDIS_WEB_KEY+itemId);
			
		} catch (Exception e) {
			LOGGER.error("process error. The MSG is = "+msg,e);
		}
		
	}
}
