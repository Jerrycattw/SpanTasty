package com.eatspan.SpanTasty.controller.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.entity.store.Cart;
import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ProductType;
import com.eatspan.SpanTasty.service.store.CartService;
import com.eatspan.SpanTasty.service.store.ProductService;
import com.eatspan.SpanTasty.service.store.ProductTypeService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/StarCups")
public class ShopController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductTypeService productTypeService;

    @Autowired
    private CartService cartService;

	 @GetMapping("/allProduct")
	    public String findAllProduct(
	            @RequestParam(value = "page", defaultValue = "1") int page,
	            Model model) {
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
	
//	@GetMapping("/allProduct")
//	public String findAllProduct(Model model) {
//		List<ProductType> productTypes = productTypeService.findAllProductType(); 
//		model.addAttribute("productTypes", productTypes);
//		List<Product> products = productService.findAllProduct();
//		model.addAttribute("products", products);
//		return "starcups/store/allProduct";
//	}

	 @PostMapping("/addToCart")
	    public String addToCart(@RequestParam("productId") Integer productId, @RequestParam("quantity") int quantity, HttpSession session) {
	        Product product = productService.findProductById(productId);
	        if (product != null) {
	            cartService.addToCart(session, product, quantity);
	        }
	        return "redirect:/StarCups/allProduct"; // Redirect back to product listing
	    }

	    @PostMapping("/removeFromCart")
	    public String removeFromCart(@RequestParam("productId") Integer productId, HttpSession session) {
	        cartService.removeFromCart(session, productId);
	        return "redirect:/StarCups/cart"; // Redirect to cart view
	    }

	    @GetMapping("/cart")
	    public String viewCart(HttpSession session, Model model) {
	        Cart cart = cartService.getCart(session);
	        model.addAttribute("cart", cart);
	        model.addAttribute("totalPrice", cart.getTotalPrice());
	        return "starcups/store/cart"; // Return cart view
	    }

	
}
