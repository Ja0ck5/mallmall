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
import com.mallmall.manage.service.ItemParamService;

@RequestMapping("item/param")
@Controller
public class ItemParamController {
	
	@Autowired
	private ItemParamService itemParamService;

	/**
	 * 根据 itemCatId 查找 itemParam
	 * 
	 * @param itemCatId
	 * @return
	 */
	@RequestMapping(value="{itemCatId}",method = RequestMethod.GET)
	public ResponseEntity<ItemParam> queryByItemCatId(@PathVariable("itemCatId")Long itemCatId){
		try {
			ItemParam param = new ItemParam();
			param.setItemCatId(itemCatId);
			ItemParam itemParam = itemParamService.queryOne(param);
			if(itemParam  == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			return ResponseEntity.ok(itemParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 查询模板列表
	 * 
	 * @param itemCatId
	 * @param paramData
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<EasyUIResult> queryList(@RequestParam(value = "page",defaultValue = "1")Integer page,
												  @RequestParam(value = "rows",defaultValue = "10")Integer rows){
		try {
			PageInfo<ItemParam> pageInfo = itemParamService.queryPageListByWhere(null, page, rows);
			return ResponseEntity.ok(new EasyUIResult(pageInfo.getTotal(), pageInfo.getList()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	/**
	 * 新增模板数据
	 * 
	 * @param itemCatId
	 * @param paramData
	 * @return
	 */
	@RequestMapping(value="{itemCatId}",method = RequestMethod.POST)
	public ResponseEntity<Void> saveItemParam(@PathVariable("itemCatId")Long itemCatId,
			@RequestParam("paramData")String paramData){
		try {
			ItemParam param = new ItemParam();
			param.setItemCatId(itemCatId);
			param.setParamData(paramData);
			itemParamService.save(param);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
