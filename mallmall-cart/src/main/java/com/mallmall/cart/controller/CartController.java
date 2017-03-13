package com.mallmall.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mallmall.cart.pojo.Cart;
import com.mallmall.cart.service.CartCookieService;
import com.mallmall.cart.service.CartService;
import com.mallmall.cart.threadLocal.UserThreadLocal;
import com.mallmall.sso.query.bean.User;

@Controller
public class CartController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartCookieService cartCookieService;
	
	@RequestMapping(value="{itemId}",method=RequestMethod.GET)
	public String addItem2Cart(@PathVariable("itemId")Long itemId,HttpServletRequest request,
			HttpServletResponse response){
		//判断用户是否登录
		User user = UserThreadLocal.get();
		if(null == user){
			//logout
			this.cartCookieService.addItem2Cart(itemId,request,response);
		}else{
			//login
			this.cartService.addItem2Cart(itemId);	
		}
		return "redirect:/show.html";
	}

	@RequestMapping(value="query/{userId}",method=RequestMethod.GET)
	public ResponseEntity<List<Cart>> queryCartListByUserId(@PathVariable("userId")Long userId){
		//判断用户是否登录
		try {
			return ResponseEntity.ok(this.cartService.queryCartList(userId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}

	@RequestMapping(value="show",method=RequestMethod.GET)
	public ModelAndView show(HttpServletRequest request){
		ModelAndView mv = new ModelAndView("cart");
		//判断用户是否登录
		User user = UserThreadLocal.get();
		if(null == user){
			//logout
			List<Cart> carts = this.cartCookieService.queryCartList(request);	
		}else{
			//login
			List<Cart> carts = this.cartService.queryCartList();	
			mv.addObject("cartList", carts);
		}
		return mv;
	}
	
	@RequestMapping(value="delete/{itemId}",method=RequestMethod.GET)
	public String delete(@PathVariable("itemId")Long itemId,HttpServletRequest request,
			HttpServletResponse response){
		//判断用户是否登录
		User user = UserThreadLocal.get();
		if(null == user){
			//logout
			this.cartCookieService.deleteItemFromCart(itemId,request,response);	
		}else{
			//login
			this.cartService.deleteItemFromCart(itemId);	
		}
		return "redirect:/show.html";
	}

	@RequestMapping(value="cart/update/num/{itemId}/{num}",method=RequestMethod.POST)
	public ResponseEntity<Void> update(@PathVariable("itemId")Long itemId,@PathVariable("num")Integer num,HttpServletRequest request,
			HttpServletResponse response){
		//判断用户是否登录
		User user = UserThreadLocal.get();
		if(null == user){
			//logout
			this.cartCookieService.updateItem2Cart(itemId,num,request,response);	
		}else{
			//login
			this.cartService.updateItem2Cart(itemId,num);	
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
