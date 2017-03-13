package com.mallmall.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mallmall.manage.pojo.ItemDesc;
import com.mallmall.web.bean.Item;
import com.mallmall.web.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {

	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 根据类目Id查找详情数据
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value="{itemId}", method=RequestMethod.GET)
	public ModelAndView showItemDetail(@PathVariable("itemId")Long itemId){
		ModelAndView mv = new ModelAndView("item");
		//basic datas
		Item item = this.itemService.queryItemById(itemId);
		mv.addObject("item", item);
		//desc
		ItemDesc itemDesc = this.itemService.queryItemDescByItemId(itemId);
		mv.addObject("itemDesc", itemDesc);
		//规格参数
		String itemParam = this.itemService.queryItemParamByItemId(itemId);
		mv.addObject("itemParam", itemParam);
		return mv;
	}
	
}
