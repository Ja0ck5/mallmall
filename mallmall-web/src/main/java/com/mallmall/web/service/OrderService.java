package com.mallmall.web.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.common.bean.HttpResult;
import com.mallmall.common.service.ApiService;
import com.mallmall.web.bean.Order;

@Service
public class OrderService {

	@Value("${ORDER_MALLMALL_URL}")
	private String ORDER_MALLMALL_URL;
	
	@Autowired
	private ApiService apiService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 调用订单系统接口完成订单的提交和创建
	 * 
	 * @param order
	 * @return 成功返回订单号
	 */
	public String submitOrder(Order order) {
		String url = ORDER_MALLMALL_URL+"/order/create";
		
		try {
			String json = MAPPER.writeValueAsString(order);
			HttpResult httpResult = this.apiService.doPostJson(url, json);
			Integer code = httpResult.getCode();
			if(200 == code){
				JsonNode jsonNode = MAPPER.readTree(httpResult.getData());
				if(200 == jsonNode.get("status").asInt())
					return jsonNode.get("data").asText();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Order queryOrderById(String orderId) {
		String url = ORDER_MALLMALL_URL+"/order/query/"+orderId;
		Order order=null;
		try {
			String jsonData = this.apiService.doGet(url);
			if(null != jsonData)
				order = MAPPER.readValue(jsonData, Order.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}

	
}
