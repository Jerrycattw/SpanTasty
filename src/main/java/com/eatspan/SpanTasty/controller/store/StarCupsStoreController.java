package com.eatspan.SpanTasty.controller.store;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ProductType;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;
import com.eatspan.SpanTasty.service.account.MemberService;
import com.eatspan.SpanTasty.service.store.ProductService;
import com.eatspan.SpanTasty.service.store.ProductTypeService;
import com.eatspan.SpanTasty.service.store.ShoppingItemService;
import com.eatspan.SpanTasty.service.store.ShoppingOrderService;
import com.eatspan.SpanTasty.utils.account.JwtUtil;
import com.eatspan.SpanTasty.utils.account.Result;

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

//	@Autowired
//	private MemberService memberService;

//	@GetMapping("/cartDetail")
//	public String toShoppingItem2(@PathVariable Integer id, Model model) {
//		ShoppingOrder shopping = shoppingOrderService.findShoppingOrderById(id);
//		model.addAttribute("shopping", shopping);
//		List<ShoppingItem> items = shoppingItemService.findShoppingItemById(id);
//		model.addAttribute("items", items);
//		List<Product> productList = productService.findAllProduct();
//		model.addAttribute("productList", productList);
//		Integer totalAmount = shoppingOrderService.calculateTotalAmount(id);
//		model.addAttribute("totalAmount", totalAmount);
//		System.out.println();
//		return "starcups/store/cartPage";
//	}

	// 查詢單筆
	@GetMapping("/cartDetail/{id}")
	public String toShoppingItem(@PathVariable Integer id, Model model) {
		ShoppingOrder shopping = shoppingOrderService.findShoppingOrderById(id);
		model.addAttribute("shopping", shopping);
		List<ShoppingItem> items = shoppingItemService.findShoppingItemById(id);
		model.addAttribute("items", items);
		List<Product> productList = productService.findAllProduct();
		model.addAttribute("productList", productList);
		Integer totalAmount = shoppingOrderService.calculateTotalAmount(id);
		model.addAttribute("totalAmount", totalAmount);
		System.out.println();
		return "starcups/store/cartPage";
	}

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
			@RequestParam Integer shoppingItemQuantity
			) {
		
		
//		@RequestBody ShoppingItem shoppingItem
		
//		ShoppingItem
		// 解析 JWT token 取得 claims
		Map<String, Object> claims = JwtUtil.parseToken(token);
		Integer memberId = (Integer) claims.get("memberId"); // 獲取會員 ID
		
		// 將會員 ID 添加到模型中
//      model.addAttribute("memberId", memberId);

		ShoppingOrder order = shoppingOrderService.addShoppingOrder(memberId, productId, shoppingItemQuantity);
//		ShoppingOrder order = shoppingOrderService.addShoppingOrder(memberId, shoppingItem.getProduct().getProductId(), shoppingItem.getShoppingItemQuantity());

		return ResponseEntity.status(HttpStatus.OK).body(order);
	}

	
	
	
}
