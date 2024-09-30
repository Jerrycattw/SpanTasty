package com.eatspan.SpanTasty.controller.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.repository.store.ProductRepository;
import com.eatspan.SpanTasty.service.store.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@GetMapping("/store/findAllProduct")
	public String findAllProduct(Model model) {
		List<Product> products = productService.findAllProduct();
		model.addAttribute("products", products);
		return "store/product/findAllProductPage";
	}
	
	
	@GetMapping("/store/api/findAllProduct")
	public List<Product> finaAllProduct(){
		return productRepo.findAll();
	}
}
