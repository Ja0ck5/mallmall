package com.mallmall.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.mallmall.cart.mapper.CartMapper;
import com.mallmall.cart.pojo.Cart;
import com.mallmall.cart.pojo.Item;
import com.mallmall.cart.threadLocal.UserThreadLocal;

@Service
public class CartService {
	
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ItemService itemService;

	
	public void addItem2Cart(Long itemId) {
		// 判断该商品是否存在于购物车中
		Cart record = new Cart();
		record.setUserId(UserThreadLocal.get().getId());
		record.setItemId(itemId);
		Cart cart = this.cartMapper.selectOne(record);
		
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
			this.cartMapper.insert(cart);
		}else{
			//商品在购物车中存在，需要合并
			cart.setNum(cart.getNum()+1);//TODO 这里默认为 1
			cart.setUpdated(new Date());
			this.cartMapper.updateByPrimaryKey(cart);
		}
	}


	
	public List<Cart> queryCartList() {
		return this.queryCartList(UserThreadLocal.get().getId());
	}



	public void deleteItemFromCart(Long itemId) {
		Cart record = new Cart();
		record.setUserId(UserThreadLocal.get().getId());
		record.setItemId(itemId);
		this.cartMapper.delete(record);
	}



	public void updateItem2Cart(Long itemId, Integer num) {
		Cart record = new Cart();
		record.setNum(num);
		record.setUpdated(new Date());
		Example example = new Example(Cart.class);
		example.createCriteria().andEqualTo("userId", UserThreadLocal.get().getId()).andEqualTo("itemId", itemId);
		this.cartMapper.updateByExampleSelective(record, example);
	}



	public List<Cart> queryCartList(Long userId) {
		Example example = new Example(Cart.class);
		example.createCriteria().andEqualTo("userId", userId);
		example.setOrderByClause("created DESC");
		return this.cartMapper.selectByExample(example);
		}

}
