package com.mallmall.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mallmall.common.service.RedisService;
import com.mallmall.web.service.ItemService;

@RequestMapping("item/cache")
@Controller
public class ItemCacheController {

	@Autowired
	private RedisService redisService;

	/**
	 * 根据 itemId delete cache 数据
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "{itemId}", method = RequestMethod.POST)
	public ResponseEntity<Void> deleteCache(@PathVariable("itemId") Long itemId) {
		try {
			this.redisService.del(ItemService.REDIS_WEB_KEY + itemId);
			return ResponseEntity.ok(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
