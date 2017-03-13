package com.mallmall.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.common.service.ApiService;
import com.mallmall.web.bean.Cart;
import com.mallmall.web.threadLocal.UserThreadLocal;

@Service
public class CartService {

	@Value("${CART_MALLMALL_URL}")
	private String CART_MALLMALL_URL;
	
	@Autowired
	private ApiService apiService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	public List<Cart> queryCartList() {
		String url = CART_MALLMALL_URL+"service/query/"+UserThreadLocal.get().getId();
		try {
			String jsonData = this.apiService.doGet(url);
			return MAPPER.readValue(jsonData, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Cart>();
	}

}
