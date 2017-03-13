package com.mallmall.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.mallmall.common.bean.EasyUIResult;
import com.mallmall.manage.pojo.ItemParam;
import com.mallmall.manage.pojo.ItemParamItem;
import com.mallmall.manage.service.ItemParamItemService;

@RequestMapping("item/param/item")
@Controller
public class ItemParamItemController {
	
	@Autowired
	private ItemParamItemService itemParamItemService;

	/**
	 * 根据 商品 ID 查找 数据
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value="{itemId}",method = RequestMethod.GET)
	public ResponseEntity<ItemParamItem> queryByItemId(@PathVariable("itemId")Long itemId){
		try {
			ItemParamItem params = new ItemParamItem();
			params.setItemId(itemId);
			ItemParamItem itemParamItem = itemParamItemService.queryOne(params);
			if(itemParamItem  == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			return ResponseEntity.ok(itemParamItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
