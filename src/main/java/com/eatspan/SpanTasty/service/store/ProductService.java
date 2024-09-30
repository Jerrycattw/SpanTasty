package com.eatspan.SpanTasty.service.store;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ProductType;
import com.eatspan.SpanTasty.repository.store.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepo;

	public Product addProduct(String productName, ProductType productType, Integer productPrice, String productPicture,
			Integer productStock, Integer productStatus, String productDescription) {
		Product product = new Product();
		product.setProductName(productName);
		product.setProductType(productType);
		product.setProductPrice(productPrice);
		product.setProductPicture(productPicture);
		product.setProductStock(productStock);
		product.setProductStatus(productStatus);
		product.setProductDescription(productDescription);

		return productRepo.save(product); 
	}

	public void deleteProduct(Integer id) {
		productRepo.deleteById(id);
	}

	public Product updateProduct(Integer id, String newProductName, ProductType newProductType, Integer newProductPrice,
			String newProductPicture, Integer newProductStock, Integer newProductStatus, String newProductDescription) {
		Optional<Product> optional = productRepo.findById(id);

		if (optional.isPresent()) {
			Product product = optional.get(); 

			// 選擇性更新
			if (newProductName != null) {
				product.setProductName(newProductName);
			}
			if (newProductType != null) {
				product.setProductType(newProductType);
			}
			if (newProductPrice != null) {
				product.setProductPrice(newProductPrice);
			}
			if (newProductPicture != null) {
				product.setProductPicture(newProductPicture);
			}
			if (newProductStock != null) {
				product.setProductStock(newProductStock);
			}
			if (newProductStatus != null) {
				product.setProductStatus(newProductStatus);
			}
			if (newProductDescription != null) {
				product.setProductDescription(newProductDescription);
			}
			return productRepo.save(product);
		}

		return null;
	}
	
	public Product findProductById(Integer id) {
		Optional<Product> optional = productRepo.findById(id);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<Product> findAllProduct(){
		return productRepo.findAll();
	}

}
