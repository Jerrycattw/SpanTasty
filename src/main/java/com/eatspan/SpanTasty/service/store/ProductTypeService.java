package com.eatspan.SpanTasty.service.store;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ProductType;
import com.eatspan.SpanTasty.entity.store.ProductType;
import com.eatspan.SpanTasty.repository.store.ProductDetailsRepository;
import com.eatspan.SpanTasty.repository.store.ProductTypeRepository;

@Service
public class ProductTypeService {

	@Autowired
	private ProductTypeRepository productTypeRepo;
		
	
	public ProductType addProductType(ProductType productType) {
		return productTypeRepo.save(productType);
	}
	
//	public ProductType addProductType(String productTypeName) {
//		ProductType productType = new ProductType();
//		productType.setProductTypeName(productTypeName);
//		return productTypeRepo.save(productType);
//	}
	
	
	public void deleteProductType(Integer id) {
		productTypeRepo.deleteById(id);
	}
	
	public ProductType updateProductType(ProductType productType) {
		Optional<ProductType> optional =productTypeRepo.findById(productType.getProductTypeId());
		if(optional.isPresent()) {
			return productTypeRepo.save(productType);
		}
		return null;
	}
	
	
	public ProductType updateProductType(Integer id, String newProductTypeName) {
		Optional<ProductType> optional = productTypeRepo.findById(id);
		
		if(optional.isPresent()) {
			ProductType productType = optional.get();
			productType.setProductTypeName(newProductTypeName);
			return productTypeRepo.save(productType);
		}
		return null;
	}
	
	public ProductType findProductTypeById(Integer id) {
		Optional<ProductType> optional = productTypeRepo.findById(id);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<ProductType> findProductByProductName(String productName) {
	    return productTypeRepo.findByProductTypeName(productName);
	}
	
	public List<ProductType> findAllProductType(){
		return productTypeRepo.findAll();
	}
	
	public Optional<ProductType> findProductTypeByIdP(Integer id) {
	    return productTypeRepo.findById(id); // 確保這裡返回 Optional<ProductType>
	}
	
}
