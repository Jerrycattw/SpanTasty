package com.eatspan.SpanTasty.service.store;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ProductType;
import com.eatspan.SpanTasty.repository.store.ProductRepository;
import com.eatspan.SpanTasty.repository.store.ProductTypeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private ProductTypeRepository productTypeRepo;

	
	public Product addProduct(Product product) {
		return productRepo.save(product);
	}
	

	public void deleteProduct(Integer id) {
		productRepo.deleteById(id);
	}
	


//	可以更新商品總類
//	@Transactional
//	public Product updateProduct(Product product) {
//	    Optional<Product> optional = productRepo.findById(product.getProductId());
//	    if (optional.isPresent()) {
//	        Product existingProduct = optional.get();
//
//	        // 更新需要的字段，保留其他字段的值
//	        if (product.getProductName() != null) {
//	            existingProduct.setProductName(product.getProductName());
//	        }
//	        if (product.getProductPrice() != null) {
//	            existingProduct.setProductPrice(product.getProductPrice());
//	        }
//	        if (product.getProductPicture() != null) {
//	            existingProduct.setProductPicture(product.getProductPicture());
//	        }
//	        if (product.getProductStock() != null) {
//	            existingProduct.setProductStock(product.getProductStock());
//	        }
//	        if (product.getProductStatus() != null) {
//	            existingProduct.setProductStatus(product.getProductStatus());
//	        }
//	        if (product.getProductDescription() != null) {
//	            existingProduct.setProductDescription(product.getProductDescription());
//	        }
//
//	        // 更新产品类型
//	        if (product.getProductType() != null) {
//	            ProductType mergedType = entityManager.merge(product.getProductType());
//	            existingProduct.setProductType(mergedType); // 使用合并后的实体
//	        }
//
//	        // 不再使用 merge，而是返回 existingProduct
//	        return existingProduct;
//	    }
//	    return null;
//	}

	public List<Product> findProductsByPage(int page, int pageSize) {
	    Page<Product> productPage = productRepo.findAll(PageRequest.of(page - 1, pageSize));
	    return productPage.getContent();
	}

	public int countAllProducts() {
	    return (int) productRepo.count();
	}



	//除了商品總類其他都可以更新
	@Transactional
    public Product updateProduct(Product product) {
        Optional<Product> optional = productRepo.findById(product.getProductId());
        if (optional.isPresent()) {
            Product existingProduct = optional.get();

            if (product.getProductType() != null && product.getProductType().getProductTypeId() != null) {
                Optional<ProductType> typeOptional = productTypeRepo.findById(product.getProductType().getProductTypeId());
                if (typeOptional.isPresent()) {
                    existingProduct.setProductType(typeOptional.get());
                }
            }
            
            // 更新需要的字段，保留其他字段的值
            if (product.getProductName() != null) {
                existingProduct.setProductName(product.getProductName());
            }
//            if (product.getProductType() != null) {
//                existingProduct.setProductType(product.getProductType());
//            }
            if (product.getProductPrice() != null) {
                existingProduct.setProductPrice(product.getProductPrice());
            }
            if (product.getProductPicture() != null) {
                existingProduct.setProductPicture(product.getProductPicture());
            }
            if (product.getProductStock() != null) {
                existingProduct.setProductStock(product.getProductStock());
            }
            if (product.getProductStatus() != null) {
                existingProduct.setProductStatus(product.getProductStatus());
            }
            if (product.getProductDescription() != null) {
                existingProduct.setProductDescription(product.getProductDescription());
            }

            return productRepo.save(existingProduct);
        }
        return null;
    }
	
	public Product findProductByIdU(Integer id) {
		return productRepo.findById(id).orElse(null);
	}

	
	public Product findProductById(Integer id) {
		Optional<Product> optional = productRepo.findById(id);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public Integer findProductPriceByProductId(Integer productId) {
	    return productRepo.findProductPriceByProductId(productId);
	}
	
	public Optional<Product> findProductByIdS(Integer id) {
	    return productRepo.findById(id);
	}
	
	public List<Product> findAllProduct(){
		return productRepo.findAll();
	}

	
	public Optional<Product> findProductByIdP(Integer id) {
	    return productRepo.findById(id);
	}
}
