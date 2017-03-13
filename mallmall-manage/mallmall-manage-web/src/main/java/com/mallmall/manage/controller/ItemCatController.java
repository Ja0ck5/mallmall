package com.mallmall.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.common.bean.ItemCatResult;
import com.mallmall.manage.pojo.ItemCat;
import com.mallmall.manage.service.ItemCatService;


@RequestMapping("item/cat")
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ItemCat>> queryItemCatList(
			@RequestParam(value = "id", defaultValue = "0") Long parentId) {

		try {
			ItemCat itemCat = new ItemCat();
			itemCat.setParentId(parentId);
			List<ItemCat> itemCats = this.itemCatService.queryByWhere(itemCat);
			// 资源不存在 404
			if (itemCats == null || itemCats.isEmpty())
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			// 200
			// return ResponseEntity.status(HttpStatus.OK).body(itemCats);
			return ResponseEntity.ok(itemCats);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 查询所有类目数据
	 * 提供给前台的接口
	 * 
	 * @return
	 */
	@RequestMapping(value="all",method = RequestMethod.GET)
	public ResponseEntity<ItemCatResult> queryItemCatAll() {
		try {
			ItemCatResult itemCatResult = this.itemCatService.queryAll2Tree();
			if(null == itemCatResult)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			return ResponseEntity.ok(itemCatResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

}
