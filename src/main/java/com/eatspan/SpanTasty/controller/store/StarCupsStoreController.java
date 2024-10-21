package com.eatspan.SpanTasty.controller.store;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ProductType;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingItemId;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;
import com.eatspan.SpanTasty.service.account.MemberService;
import com.eatspan.SpanTasty.service.store.ProductService;
import com.eatspan.SpanTasty.service.store.ProductTypeService;
import com.eatspan.SpanTasty.service.store.ShoppingItemService;
import com.eatspan.SpanTasty.service.store.ShoppingOrderService;
import com.eatspan.SpanTasty.utils.account.JwtUtil;
import com.eatspan.SpanTasty.utils.account.Result;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/StarCups")
public class StarCupsStoreController {

	@Autowired
	private ShoppingItemService shoppingItemService;

	@Autowired
	private ShoppingOrderService shoppingOrderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductTypeService productTypeService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private HttpSession session;

	
	@GetMapping("/allProduct")
	public String findAllProduct(@RequestHeader(value = "Authorization", required = false) String token,
			@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

		List<ProductType> productTypes = productTypeService.findAllProductType();
		model.addAttribute("productTypes", productTypes);

		List<Product> products = productService.findProductsByPage(page, 12);
		model.addAttribute("products", products);

		int totalProducts = productService.countAllProducts();
		int totalPages = (int) Math.ceil((double) totalProducts / 12);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);

		return "starcups/store/allProduct";
	}

	
	
	@PostMapping("/addPost")
	@ResponseBody
	public ResponseEntity<?> addOrder(@RequestHeader(value = "Authorization") String token,
	                                   @RequestParam Integer productId,
	                                   @RequestParam Integer shoppingItemQuantity) {
	    try {
	        // 解析 JWT token 取得 claims
	        Map<String, Object> claims = JwtUtil.parseToken(token);
	        Integer memberId = (Integer) claims.get("memberId"); // 獲取會員 ID

	        Integer shoppingId = (Integer) session.getAttribute("shoppingId");
	        ShoppingOrder order;
//	        System.out.println("Add Item");
	        if (shoppingId == null) {
	            order = shoppingOrderService.addShoppingOrder(memberId, productId, shoppingItemQuantity);
	            session.setAttribute("shoppingId", order.getShoppingId()); 
	        } else {
	            shoppingItemService.addShoppingItemToExistingOrder(shoppingId, productId, shoppingItemQuantity);
	            order = shoppingOrderService.findShoppingOrderById(shoppingId); 
	        }
	        
	        return ResponseEntity.status(HttpStatus.OK).body(order);
	    } catch (Exception e) {
	        // 日誌記錄錯誤
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發生錯誤：" + e.getMessage());
	    }
	}

	@PostMapping("/updateItem")
	@ResponseBody
	public ResponseEntity<?> updateShoppingItem(
	        @RequestHeader(value = "Authorization") String token,
	        @RequestParam Integer shoppingId,
	        @RequestParam Integer productId,
	        @RequestParam Integer shoppingItemQuantity) {

	    try {
	        // 解析 JWT token 取得 claims
	        Map<String, Object> claims = JwtUtil.parseToken(token);
	        Integer memberId = (Integer) claims.get("memberId"); 
	        System.out.println("updateItem=HIHI");
	        ShoppingItemId shoppingItemId = new ShoppingItemId(shoppingId, productId);
	        ShoppingItem existingItem = shoppingItemService.findShoppingItemById(shoppingItemId);
	        System.out.println("update");
	        if (existingItem != null) {
	            existingItem.setShoppingItemQuantity(shoppingItemQuantity);
	            
	            Integer productPrice = shoppingItemService.getProductPriceById(productId);
	            Integer totalPrice = productPrice != null ? productPrice * shoppingItemQuantity : 0;
	            existingItem.setShoppingItemPrice(totalPrice);
	            shoppingItemService.updateShoppingItem(existingItem);
	            
	            ShoppingOrder shopping = shoppingOrderService.findShoppingOrderById(shoppingId);
	            shopping.setShoppingTotal(shoppingOrderService.calculateTotalAmount(shoppingId));
	            shoppingOrderService.updateShoppingOrder(shopping);
	            
	            return ResponseEntity.status(HttpStatus.OK).body(existingItem);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("購物項目未找到");
	        }
	    } catch (Exception e) {
	        // 日誌記錄錯誤
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發生錯誤：" + e.getMessage());
	    }
	}


	// 查詢單筆
	@PostMapping("/cartDetail")
	public String toShoppingItem(Model model) {
		
		Integer shoppingId = (Integer) session.getAttribute("shoppingId");
		
		 if (shoppingId == null) {
		        return "starcups/store/page505"; // 
		    }
	
		 ShoppingOrder shopping = shoppingOrderService.findShoppingOrderById(shoppingId);
			model.addAttribute("shopping", shopping);
			List<ShoppingItem> items = shoppingItemService.findShoppingItemById(shoppingId);
			model.addAttribute("items", items);
			List<Product> productList = productService.findAllProduct();
			model.addAttribute("productList", productList);
			Integer totalAmount = shoppingOrderService.calculateTotalAmount(shoppingId);
			model.addAttribute("totalAmount", totalAmount);
			System.out.println(shoppingId);
			return "starcups/store/cartPage";
			

	}


	@PostMapping("/checkOut")
	public String checkOut(Model model) {
		Integer shoppingId = (Integer) session.getAttribute("shoppingId");
		ShoppingOrder shopping = shoppingOrderService.findShoppingOrderById(shoppingId);
		
		model.addAttribute("shopping", shopping);
		List<ShoppingItem> items = shoppingItemService.findShoppingItemById(shoppingId);
		model.addAttribute("items", items);
		List<Product> productList = productService.findAllProduct();
		model.addAttribute("productList", productList);
		Integer totalAmount = shoppingOrderService.calculateTotalAmount(shoppingId);
		model.addAttribute("totalAmount", totalAmount);
		Member members = memberService.findMemberByShoppingId(shoppingId);
		model.addAttribute("members",members);
		return "starcups/store/checkOut";
	}

//	@PostMapping("/ecpayCheckout")
//	public String ecpayCheckout() {
//		String aioCheckOutALLForm = shoppingOrderService.ecpayCheckout();
//
//		return aioCheckOutALLForm;
//	}

//	@PostMapping("/ecpayCheckout")
//	public String ecpayCheckout(HttpServletRequest request) {
//	    return shoppingOrderService.ecpayCheckout(request);
//	}
	
	@GetMapping("/findItem")
	@ResponseBody
	public List<ShoppingItem> findItemAjax(@RequestParam Integer shoppingId) {
		return shoppingItemService.findShoppingItemById(shoppingId);
	}
	

	@PostMapping("/ecpayCheckout")
	@ResponseBody
	public String ecpayCheckout(Model model) {
	    Integer shoppingId = (Integer) session.getAttribute("shoppingId");
	    ShoppingOrder shopping = shoppingOrderService.findShoppingOrderById(shoppingId);
	    if (shopping.getShoppingStatus() == 2) {
	        return "已完成結帳，無法重複結帳";
	    }
	    
	    String aioCheckOutALLForm = shoppingOrderService.ecpayCheckout(shoppingId);

	    model.addAttribute("aioCheckOutALLForm", aioCheckOutALLForm);
	    
	    
	    return aioCheckOutALLForm;
	}

	
	@GetMapping("/OrderConfirm")
//	public String checkOutFinish(@RequestParam Map<String, String>map, Model model) {
		public String checkOutFinish(Model model) {
		
		Integer shoppingId = (Integer) session.getAttribute("shoppingId");
		ShoppingOrder shopping = shoppingOrderService.findShoppingOrderById(shoppingId);
//		String string = map.get("TradeNo");
//		System.out.println(map);
		
		
		
		System.out.println("shoppingId "+shoppingId );
		
		model.addAttribute("shopping", shopping);
		List<ShoppingItem> items = shoppingItemService.findShoppingItemById(shoppingId);
		model.addAttribute("items", items);
		List<Product> productList = productService.findAllProduct();
		model.addAttribute("productList", productList);
		Integer totalAmount = shoppingOrderService.calculateTotalAmount(shoppingId);
		model.addAttribute("totalAmount", totalAmount);
//		Member members = memberService.findMemberByShoppingId(shoppingId);
//		model.addAttribute("members",members);
		
	    Integer discountAmount = shopping.getDiscountAmount();
	    if (discountAmount == null) {
	        discountAmount = 0;
	    }
	    
////	    // 計算 finalAmount
//	    Integer shoppingTotal = shopping.getShoppingTotal();
//	    model.addAttribute("shoppingTotal",shoppingTotal);
//	    Integer finalAmount = shoppingTotal - discountAmount;
//	    
	    model.addAttribute("discountAmount", discountAmount);
//	    model.addAttribute("finalAmount", finalAmount);
	    
		
//		shopping.setFinalAmount(shopping.getShoppingTotal()-shopping.getDiscountAmount());
		
		
	    shopping.setShoppingStatus(2);
	    shoppingOrderService.updateShoppingOrder(shopping);
	    
        try {
        	shoppingOrderService.sendMail(shopping);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	    
	    session.removeAttribute("shoppingId");
	    
		return "starcups/store/OrderConfirm";
	}
	
}
