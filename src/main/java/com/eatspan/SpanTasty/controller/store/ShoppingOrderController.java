package com.eatspan.SpanTasty.controller.store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingItemId;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;
import com.eatspan.SpanTasty.service.account.MemberService;
import com.eatspan.SpanTasty.service.store.ProductService;
import com.eatspan.SpanTasty.service.store.ShoppingItemService;
import com.eatspan.SpanTasty.service.store.ShoppingOrderService;

@Controller
@RequestMapping("/shoppingOrder")
public class ShoppingOrderController {

	@Autowired
	private ShoppingOrderService shoppingOrderService;
	
	@Autowired
	private ShoppingItemService shoppingItemService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private MemberService memberService;
	
	// 導向新增訂單頁面
    @GetMapping("/add")
    public String toAddShoppingOrder(Model model) {
        List<Member> members = memberService.findAllMembers(); // 獲取所有會員
        List<Product> products = productService.findAllProduct(); // 獲取所有商品
        model.addAttribute("members", members);
        model.addAttribute("products", products);
        return "store/shopping/addShoppingOrder"; // 返回新增訂單的視圖
    }
	
	
    @PostMapping("/addPost")
    public String addOrder(@RequestParam Integer memberId,
                           @RequestParam Integer productId,
                           @RequestParam Integer shoppingItemQuantity,
                           Model model) {
        
        ShoppingOrder order = shoppingOrderService.addShoppingOrder(memberId, productId, shoppingItemQuantity);

        return "redirect:/shoppingOrder/findAll";
    }

	
	@DeleteMapping("/del/{id}")
	public String deleteShoppingOrder(@PathVariable("id") Integer shoppingOrderId){
		shoppingOrderService.deleteShoppingOrder(shoppingOrderId);
		return "redirect:/shoppingOrder/findAll";
	}
	
	@GetMapping("/update/{id}")
	 public String showUpdateForm(@PathVariable("id") Integer shoppingId, Model model) {
        ShoppingOrder order = shoppingOrderService.findShoppingOrderById(shoppingId);
        model.addAttribute("shoppingOrder", order);
        return "store/shopping/updateShoppingOrder"; 
    }
	
	@PutMapping("/updatePut")
	public String updateShoppingOrder(
	        @RequestParam Integer shoppingId,
	        @RequestParam Integer shoppingStatus,
	        @RequestParam String shoppingMemo) {
	    
	    ShoppingOrder shoppingOrder = new ShoppingOrder();
	    shoppingOrder.setShoppingId(shoppingId);
	    shoppingOrder.setShoppingStatus(shoppingStatus);
	    shoppingOrder.setShoppingMemo(shoppingMemo);

	    shoppingOrderService.updateShoppingOrder(shoppingOrder);
	    
	    return "redirect:/shoppingOrder/findAll"; 
	}
	
	
//	 // 更新訂單
//    @PutMapping("/{id}")
//    public ResponseEntity<ShoppingOrder> updateOrder(@PathVariable Integer id, @RequestBody ShoppingOrder shoppingOrder) {
//        ShoppingOrder updatedOrder = shoppingOrderService.update(id, shoppingOrder);
//        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.notFound().build();
//    }
	
	
//	@PutMapping("/update")
//	public ResponseEntity<?> updateShoppingOrder(@RequestBody ShoppingOrder updateShoppingOrder){
//		ShoppingOrder shoppingOrder = shoppingOrderService.updateShoppingOrder(updateShoppingOrder);
//		if(shoppingOrder != null) {
//			return new ResponseEntity<>(shoppingOrder, HttpStatus.OK);
//		}
//		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	}
	
	@GetMapping("/find/{id}")
	public ResponseEntity<?> findByShoppingOrderId(@PathVariable("id") Integer shoppingOrderId){
		ShoppingOrder shoppingOrder = shoppingOrderService.findShoppingOrderById(shoppingOrderId);
		if(shoppingOrder != null) {
			return new ResponseEntity<>(shoppingOrder,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
//	@GetMapping("/findAll")
//	public String findAllShoppingOrder(@RequestParam Integer memberId, Model model) {
//	    // 獲取會員
//	    Member member = memberService.findMemberById(memberId).orElse(null);
//	    model.addAttribute("member", member);
//	    
//	    // 獲取所有購物訂單
//	    List<ShoppingOrder> shoppings = shoppingOrderService.findAllShoppingOrder();
//	    model.addAttribute("shoppings", shoppings);
//	    
//	    return "store/shopping/searchAllShopping";
//	}
	
	
	@GetMapping("/findAll")
	public String findAllShoppingOrder(@RequestParam(required = false) Integer memberId, Model model) {
	    if (memberId != null) {
	        Optional<Member> memberOptional = memberService.findMemberById(memberId);
	        if (memberOptional.isPresent()) {
	            model.addAttribute("member", memberOptional.get());
	        } else {
	            model.addAttribute("member", null);
	            // 這裡可以記錄一些日誌
	            System.out.println("找不到會員，memberId: " + memberId);
	        }
	    } else {
	        model.addAttribute("member", null);
	    }

	    List<ShoppingOrder> shoppings = shoppingOrderService.findAllShoppingOrder();
	    model.addAttribute("shoppings", shoppings);

	    return "store/shopping/searchAllShopping";
	}

	
//	@GetMapping("/findAll")
//	public ResponseEntity<List<ShoppingOrder>> findAllShoppingOrder(){
//		List<ShoppingOrder> shoppingOrders = shoppingOrderService.findAllShoppingOrder();
//		return new ResponseEntity<>(shoppingOrders,HttpStatus.OK);
//	}
}
