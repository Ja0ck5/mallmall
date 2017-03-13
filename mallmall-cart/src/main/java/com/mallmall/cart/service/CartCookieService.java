package com.mallmall.cart.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallmall.cart.pojo.Cart;
import com.mallmall.cart.pojo.Item;
import com.mallmall.cart.threadLocal.UserThreadLocal;
import com.mallmall.common.utils.CookieUtils;

@Service
public class CartCookieService {

	@Autowired
	private ItemService itemService;
	
	private static final String COOKIE_CART = "MM_CK_CART";
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final Integer COOKIE_MAX_AGE = 60*60*24*30*3;
	
	
	public void addItem2Cart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Cart> carts = this.queryCartList(request);
			Cart cart = null;
			for (Cart c : carts) {
				if(c.getItemId().intValue() == itemId.intValue()){
					cart = c;
					break;
				}
			}
			
			if(null == cart){
				//商品在购物车中不存在，添加
				cart = new Cart();
				cart.setCreated(new Date());
				cart.setUpdated(cart.getCreated());
				cart.setId(null);
				cart.setUserId(UserThreadLocal.get().getId());
				cart.setItemId(itemId);
				//查询商品数据
				Item item = this.itemService.queryItemById(itemId);
				cart.setItemImage(item.getImage());
				cart.setItemPrice(item.getPrice());
				cart.setItemTitle(item.getTitle());
				cart.setNum(1);
				carts.add(cart);
			}else{
				//商品在购物车中存在，需要合并
				cart.setNum(cart.getNum()+1);//TODO 这里默认为 1
			}
			CookieUtils.setCookie(request, response, COOKIE_CART, MAPPER.writeValueAsString(carts),
					COOKIE_MAX_AGE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public List<Cart> queryCartList(HttpServletRequest request) {
		try {
			String cookieCart = CookieUtils.getCookieValue(request, COOKIE_CART , true);
			if(StringUtils.isEmpty(cookieCart))
				return new ArrayList<Cart>();
			return  MAPPER.readValue(cookieCart, MAPPER.getTypeFactory()
					.constructCollectionType(List.class, Cart.class));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return new ArrayList<Cart>();
	}


	public void deleteItemFromCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
		List<Cart> carts = this.queryCartList(request);
		try {
		for (Cart c : carts) {
			if(c.getItemId().intValue() == itemId.intValue()){
				carts.remove(c);
				break;
			}
		}
			CookieUtils.setCookie(request, response, COOKIE_CART, MAPPER.writeValueAsString(carts),
					COOKIE_MAX_AGE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}


	public void updateItem2Cart(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
		List<Cart> carts = this.queryCartList(request);
		try {
			for (Cart c : carts) {
				if(c.getItemId().intValue() == itemId.intValue()){
					c.setNum(num);
					break;
				}
			}
			CookieUtils.setCookie(request, response, COOKIE_CART, MAPPER.writeValueAsString(carts),
					COOKIE_MAX_AGE, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
