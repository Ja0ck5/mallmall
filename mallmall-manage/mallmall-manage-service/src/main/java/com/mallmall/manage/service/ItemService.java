package com.mallmall.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageInfo;
import com.mallmall.common.service.ApiService;
import com.mallmall.manage.pojo.Item;
import com.mallmall.manage.pojo.ItemDesc;
import com.mallmall.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {

	@Autowired
	private ItemDescService itemDescService;

	@Autowired
	private ItemParamItemService itemParamItemService;
	
	@Autowired
	private ApiService apiService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${MALLMALL_WEB_URL}")
	private String MALLMALL_WEB_URL;
	
	@Value("${MALLMALL_WEB_CACHE_URI}")
	private String MALLMALL_WEB_CACHE_URI;
	
	@Value("${MALLMALL_WEB_SUFFIX}")
	private String MALLMALL_WEB_SUFFIX;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final String ROUTING_KEY_PREFIX = "item.";
	/**
	 * 新增商品
	 * @param item
	 * @param desc
	 */
	public void saveItem(Item item, String desc,String itemParams) {
		
		//新增商品
		item.setStatus(1);
		item.setId(null);
		super.save(item);
		
		//新增商品描述
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(item.getId());
		this.itemDescService.save(itemDesc);
		
		//新增商品规格参数
		if(StringUtils.isNotEmpty(itemParams)){
			ItemParamItem itemParamItem = new ItemParamItem();
			itemParamItem.setItemId(item.getId());
			itemParamItem.setParamData(itemParams);
			this.itemParamItemService.save(itemParamItem);
		}
		//使用 rabbitmq 通知新增产品
		sendMsg(item.getId(),"insert");
	}

	
	public PageInfo<Item> queryItemList(Integer page, Integer rows) {
		Example example = new Example(Item.class);
		example.setOrderByClause("updated DESC");
		example.createCriteria().andNotEqualTo("status", 3);
		return super.queryPageListByExample(example, page, rows);
	}


	/**
	 * 实现商品的逻辑删除
	 * 
	 * @param ids
	 */
	public void updateByIds(List<Object> ids) {
		Example example = new Example(Item.class);
		example.createCriteria().andIn("id", ids);
		Item item = new Item();
		item.setStatus(3);
		super.getMapper().updateByExampleSelective(item, example);
		for (Object id : ids) {
			//使用  rabbitmq 通知删除产品
			sendMsg(Long.valueOf(id.toString()),"delete");
		}
	}


	/**
	 * 更新商品
	 * 
	 * @param item
	 * @param desc
	 */
	public void updateItem(Item item, String desc,ItemParamItem itemParamItem) {
		item.setStatus(null);
		item.setCreated(null);
		super.updateSelective(item);

		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		this.itemDescService.updateSelective(itemDesc);
		
		//更新规格参数
		if(null != itemParamItem){
			this.itemParamItemService.updateSelective(itemParamItem);
		}
		// notify other system item is updated
	/*	String url = MALLMALL_WEB_URL+MALLMALL_WEB_CACHE_URI+item.getId()+MALLMALL_WEB_SUFFIX;
		try {
			this.apiService.doPost(url);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		//使用 rabbitmq 通知更新产品
		sendMsg(item.getId(),"update");
	}


	public void sendMsg(Long itemId,String type) {
		try {
			Map<String,Object> msg = new HashMap<>();
			msg.put("itemId", itemId);
			msg.put("type", type);
			msg.put("created", System.currentTimeMillis());
			this.rabbitTemplate.convertAndSend(ROUTING_KEY_PREFIX+type, MAPPER.writeValueAsString(msg));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
