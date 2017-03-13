package com.mallmall.manage.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mallmall.manage.pojo.Item;
import com.mallmall.manage.pojo.ItemParam;
import com.mallmall.manage.pojo.ItemParamItem;
import com.mallmall.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {
	// 使用日志,便于生产环境测试
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams")String itemParams) {
		try {
			// 判断是否是开发状态
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Create an item = {},desc = {}", item, desc);
			}
			// 新增商品			//新增商品规格参数
			this.itemService.saveItem(item, desc,itemParams);
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Create an item SUCCESS! itemId = {}", item.getId());
			}
			// 201
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			LOGGER.error("Create an item error : [Item] " + item, e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,
			@RequestParam("itemParams")String itemParams,
			@RequestParam("itemParamId")Long itemParamId) {
		try {
			// 判断是否是开发状态
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("update an item = {},desc = {}", item, desc);
			}
			ItemParamItem itemParamItem  = null;
			if(null != itemParamId){
				//封装对象 itemParamItem
				itemParamItem = new ItemParamItem();
				itemParamItem.setId(itemParamId);
				itemParamItem.setParamData(itemParams);
			}
			// 修改商品
			this.itemService.updateItem(item, desc,itemParamItem);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("update an item SUCCESS! itemId = {}", item.getId());
			}
			// 204
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			LOGGER.error("update an item error : [Item] " + item, e);
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 查询商品
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<EasyUIResult> queryItemList(
			@RequestParam(value = "page",defaultValue = "1") Integer page,
			@RequestParam(value = "rows",defaultValue = "30") Integer rows) {
		try {
			PageInfo<Item> pageInfo = this.itemService.queryItemList(page, rows);
			return ResponseEntity.ok(new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	@RequestMapping(value="{itemId}",method = RequestMethod.GET)
	public ResponseEntity<Item> queryItemById(@PathVariable(value = "itemId") Long itemId) {
		try {
			Item item = this.itemService.queryById(itemId);
			if(null == item)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			return ResponseEntity.ok(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 逻辑删除商品
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteItemByIds(@RequestParam("ids")List<Object> ids) {
		try {
			this.itemService.updateByIds(ids);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 500
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
