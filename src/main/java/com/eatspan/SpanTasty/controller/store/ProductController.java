package com.eatspan.SpanTasty.controller.store;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ProductType;
import com.eatspan.SpanTasty.repository.store.ProductRepository;
import com.eatspan.SpanTasty.service.store.ProductService;
import com.eatspan.SpanTasty.service.store.ProductTypeService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/product")
@PropertySource("upload.properties")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductTypeService productTypeService;

	@Value("${upload.storePath}")
	private String uploadPath;

	// 導向新增商品頁面
	@GetMapping("/add")
	public String toAddProduct(Model model) {
		List<ProductType> productTypes = productTypeService.findAllProductType(); // 獲取所有商品類別
		model.addAttribute("productTypes", productTypes); // 添加商品類別到模型
		return "spantasty/store/product/addProduct";
	}

	// 新增
	@PostMapping("/addPost")
	public String addProduct(@ModelAttribute Product addProduct, @RequestParam MultipartFile file,
			@RequestParam Integer productTypeId) throws IllegalStateException, IOException {

		ProductType productType = productTypeService.findProductTypeById(productTypeId);
		addProduct.setProductType(productType);

		// 建立圖片保存的目錄
		File fileSaveDirectory = new File(uploadPath);
		if (!fileSaveDirectory.exists()) {
			fileSaveDirectory.mkdirs();
		}

		// 檢查文件是否不為空，並處理上傳
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String extension = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = addProduct.getProductName() + extension;

			// 保存檔案到指定路徑
			File fileToSave = new File(uploadPath + File.separator + newFileName);
			file.transferTo(fileToSave);
			addProduct.setProductPicture("/SpanTasty/upload/store/" + newFileName);
		}

		// 新增商品至資料庫
		productService.addProduct(addProduct);

		return "redirect:/product/findAll";
	}

	 @DeleteMapping("/del/{id}")
	    public String deleteProduct(@PathVariable("id") Integer productId) {
	        productService.deleteProduct(productId);
	        return "redirect:/product/findAll";
	    }
	

	// 導向更新商品頁面
	@GetMapping("/update/{id}")
	public String toUpdateProduct(@PathVariable("id") Integer productId, Model model) {
		List<ProductType> productTypes = productTypeService.findAllProductType(); // 獲取所有商品類別
		model.addAttribute("productTypes", productTypes);
		Product product = productService.findProductById(productId);
		model.addAttribute("product", product);
		return "spantasty/store/product/updateProduct";
	}
	

	
	@PutMapping("/updatePut")
	public String updateProduct(@ModelAttribute Product updateProduct, 
								@RequestParam Integer productTypeId,
								@RequestParam MultipartFile file) throws IllegalStateException, IOException {
		


		System.out.println(productTypeId+"ok");
		
		// 建立圖片保存的目錄
		File fileSaveDirectory = new File(uploadPath);
		if (!fileSaveDirectory.exists()) {
			fileSaveDirectory.mkdirs();
		}

		// 檢查文件是否不為空，並處理上傳
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String extension = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = updateProduct.getProductName() + extension;

			// 保存檔案到指定路徑
			File fileToSave = new File(uploadPath + File.separator + newFileName);
			file.transferTo(fileToSave);
			updateProduct.setProductPicture("/SpanTasty/upload/store/" + newFileName);
		}
		productService.updateProduct(updateProduct);

		return "redirect:/product/findAll";
	}


	@GetMapping("/find/{id}")
	public ResponseEntity<?> findProductById(@PathVariable("id") Integer productId) {
		Product product = productService.findProductById(productId);
		if (product != null) {
			return new ResponseEntity<>(product, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findAll")
	public String findAllProduct(Model model) {
		List<ProductType> productTypes = productTypeService.findAllProductType(); // 獲取所有商品類別
		model.addAttribute("productTypes", productTypes);
		List<Product> products = productService.findAllProduct();
		model.addAttribute("products", products);
		return "spantasty/store/product/searchAllProduct";
	}

}
