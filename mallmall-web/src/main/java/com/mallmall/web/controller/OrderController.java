package com.mallmall.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mallmall.sso.query.bean.User;
import com.mallmall.web.bean.Cart;
import com.mallmall.web.bean.Order;
import com.mallmall.web.service.CartService;
import com.mallmall.web.service.ItemService;
import com.mallmall.web.service.OrderService;
import com.mallmall.web.service.UserService;
import com.mallmall.web.threadLocal.UserThreadLocal;

@RequestMapping("order")
@Controller
public class OrderController {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CartService cartService;
	
	@RequestMapping(value = "{itemId}",method = RequestMethod.GET)
	public ModelAndView go2Order(@PathVariable("itemId")Long itemId){
		ModelAndView mv = new ModelAndView("order");
		mv.addObject("item", this.itemService.queryItemById(itemId));
		return mv;
	}

	@RequestMapping(value = "create",method = RequestMethod.GET)
	public ModelAndView go2CartOrder(){
		ModelAndView mv = new ModelAndView("order-cart");
		List<Cart> carts = this.cartService.queryCartList();
		if(carts.isEmpty()){
			//TODO 提示出错或者购物车为空
		}
			mv.addObject("carts", carts);
		return mv;
	}

	/*@RequestMapping(value = "submit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> submitOrder(Order order,
			@CookieValue(UserLoginHandlerInterceptor.MALLMALL_TOKEN)String token){
		//add current user info
		User user = this.userService.queryUserByToken(token);
		order.setUserId(user.getId());
		order.setBuyerNick(user.getUsername());
		Map<String,Object> result = new HashMap<>();
		String orderId = this.orderService.submitOrder(order);
		if(null == orderId){
			result.put("status",400);
		}else{
			result.put("status",200);
			result.put("data",orderId);
		}
		return result;
	}*/

	@RequestMapping(value = "submit",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> submitOrder(Order order){
		//add current user info
		User user = UserThreadLocal.get();
		order.setUserId(user.getId());
		order.setBuyerNick(user.getUsername());
		Map<String,Object> result = new HashMap<>();
		String orderId = this.orderService.submitOrder(order);
		if(null == orderId){
			result.put("status",400);
		}else{
			result.put("status",200);
			result.put("data",orderId);
		}
		return result;
	}
	
	@RequestMapping(value = "success")
	public ModelAndView success(@RequestParam("id")String orderId){
		ModelAndView mv = new ModelAndView("success");
		mv.addObject("order", this.orderService.queryOrderById(orderId));
		mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
		return mv;
	}
}
