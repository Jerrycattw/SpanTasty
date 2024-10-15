package com.eatspan.SpanTasty.service.store;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingItemId;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;
import com.eatspan.SpanTasty.repository.store.ShoppingItemRepository;
import com.eatspan.SpanTasty.repository.store.ShoppingOrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
public class ShoppingOrderService {

	@Autowired
	private ShoppingOrderRepository shoppingOrderRepo;

	@Autowired
	@Lazy
	private ShoppingItemService shoppingItemService;
	
	@Autowired ShoppingItemRepository shoppingItemRepo;
	
	@Autowired
	private ProductService productService;

	@PersistenceContext
	private EntityManager entityManager;
	

	public Integer calculateTotalAmount(Integer shoppingId) {
	    String hql = "SELECT SUM(i.product.productPrice * i.shoppingItemQuantity) "
	            + "FROM ShoppingItem i WHERE i.shoppingOrder.shoppingId = :shoppingId";
	    TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
	    query.setParameter("shoppingId", shoppingId);

	    Long totalAmount = query.getSingleResult();
	    return (totalAmount != null) ? totalAmount.intValue() : 0; // 轉換為 Integer
	}
	
	
	@Transactional
	public ShoppingOrder addShoppingOrder(Integer memberId, Integer productId, Integer shoppingItemQuantity) {
	    
	    ShoppingOrder shoppingOrder = new ShoppingOrder();
	    shoppingOrder.setMemberId(memberId);
	    shoppingOrder.setShoppingDate(LocalDateTime.now());
	    shoppingOrder.setShoppingStatus(1);
	    shoppingOrder.setShoppingTotal(0); 

	    ShoppingOrder savedOrder = shoppingOrderRepo.save(shoppingOrder);

	    Optional<Product> productOpt = productService.findProductByIdS(productId);
	    if (!productOpt.isPresent()) {
	        throw new RuntimeException("Product not found with ID: " + productId);
	    }
	    
	    Product product = productOpt.get();
	    Integer price = product.getProductPrice(); 
	    
	    // 創建複合主鍵
	    ShoppingItemId itemId = new ShoppingItemId(savedOrder.getShoppingId(), productId); // 使用已保存的訂單 ID

	    ShoppingItem shoppingItem = new ShoppingItem(itemId, shoppingItemQuantity, price);

	    shoppingItem.setShoppingOrder(savedOrder);
	    
	    shoppingItemRepo.save(shoppingItem); 

	    shoppingOrder.setShoppingTotal(shoppingOrder.getShoppingTotal() + (price * shoppingItemQuantity));
	    shoppingOrderRepo.save(shoppingOrder);

	    return savedOrder; 
	}

	@Transactional
	public ShoppingOrder addShoppingOrder(Integer memberId, List<Integer> productId, List<Integer> shoppingItemQuantity) {
	    
	    ShoppingOrder shoppingOrder = new ShoppingOrder();
	    shoppingOrder.setMemberId(memberId);
	    shoppingOrder.setShoppingDate(LocalDateTime.now());
	    shoppingOrder.setShoppingStatus(1);
	    shoppingOrder.setShoppingTotal(0);

	    ShoppingOrder savedOrder = shoppingOrderRepo.save(shoppingOrder);

	    for (int i = 0; i < productId.size(); i++) {
	        Integer currentProductId = productId.get(i);
	        Integer currentQuantity = shoppingItemQuantity.get(i);

	        Optional<Product> productOpt = productService.findProductByIdS(currentProductId);
	        if (!productOpt.isPresent()) {
	            throw new RuntimeException("Product not found with ID: " + currentProductId);
	        }
	        
	        Product product = productOpt.get();
	        Integer price = product.getProductPrice(); 
	        
	        // 創建複合主鍵
	        ShoppingItemId itemId = new ShoppingItemId(savedOrder.getShoppingId(), currentProductId); 

	        ShoppingItem shoppingItem = new ShoppingItem(itemId, currentQuantity, price);
	        shoppingItem.setShoppingOrder(savedOrder);
	        
	        shoppingItemRepo.save(shoppingItem); 

	        // 更新訂單總金額
	        shoppingOrder.setShoppingTotal(shoppingOrder.getShoppingTotal() + (price * currentQuantity));
	    }

	    shoppingOrderRepo.save(shoppingOrder);

	    return savedOrder; 
	}


	
	
	@Transactional
	public ShoppingOrder createNewShoppingOrder(Integer memberId) {
	    ShoppingOrder shoppingOrder = new ShoppingOrder();
	    shoppingOrder.setMemberId(memberId);
	    shoppingOrder.setShoppingDate(LocalDateTime.now());
	    shoppingOrder.setShoppingStatus(1);
	    shoppingOrder.setShoppingMemo(""); // 可以根據需要設置默認備註
	    shoppingOrder.setShoppingTotal(0); // 初始金額

	    ShoppingOrder savedOrder = shoppingOrderRepo.save(shoppingOrder);
	    return savedOrder;
	}
	

	@Transactional
	public void deleteShoppingOrder(Integer shoppingId) {
		shoppingItemService.deleteAllShoppingItems(shoppingId);
		shoppingOrderRepo.deleteById(shoppingId);
	}

	@Transactional
	public ShoppingOrder updateShoppingOrder(ShoppingOrder shoppingOrder) {
		Optional<ShoppingOrder> optional = shoppingOrderRepo.findById(shoppingOrder.getShoppingId());
		 if (optional.isPresent()) {
		        ShoppingOrder existingshoppingOrder = optional.get();
		        
		        if (shoppingOrder.getShoppingMemo() != null) {
		            existingshoppingOrder.setShoppingMemo(shoppingOrder.getShoppingMemo());
		        }
		        if (shoppingOrder.getShoppingStatus() != null) {
		            existingshoppingOrder.setShoppingStatus(shoppingOrder.getShoppingStatus());
		        }
	
		        existingshoppingOrder.setShoppingTotal(calculateTotalAmount(shoppingOrder.getShoppingId()));
	
		        return shoppingOrderRepo.save(existingshoppingOrder);
		    }
		    return null; 
		}

	
	
//	@Transactional
//	public ShoppingOrder updateShoppingOrder(Integer shoppingId, String shoppingMemo, Integer shoppingStatus) {
//	    Optional<ShoppingOrder> optionalOrder = shoppingOrderRepo.findById(shoppingId);
//
//	    if (optionalOrder.isPresent()) {
//	        ShoppingOrder shoppingOrder = optionalOrder.get();
//	        
//	        if (shoppingMemo != null) {
//	            shoppingOrder.setShoppingMemo(shoppingMemo);
//	        }
//	        if (shoppingStatus != null) {
//	            shoppingOrder.setShoppingStatus(shoppingStatus);
//	        }
//
//	        shoppingOrder.setShoppingTotal(calculateTotalAmount(shoppingId));
//
//	        return shoppingOrderRepo.save(shoppingOrder);
//	    }
//	    return null; 
//	}

	
	public ShoppingOrder findShoppingOrderById(Integer id) {
		Optional<ShoppingOrder> optional = shoppingOrderRepo.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public List<ShoppingOrder> findAllShoppingOrder() {
		return shoppingOrderRepo.findAll();
	}
	
	@Transactional
	public List<ShoppingOrder> findOrdersByMemberId(Integer memberId) {
	    return shoppingOrderRepo.findByMemberId(memberId);
	}

	// 按照購物日期排序，搜尋最近的一筆訂單
	@Transactional
	public ShoppingOrder getLatestShoppingOrderByMemberId(Integer memberId) {
	    List<ShoppingOrder> orders = shoppingOrderRepo.findByMemberId(memberId);
	    if (orders != null && !orders.isEmpty()) {
	        return orders.stream()
	                     .max((o1, o2) -> o1.getShoppingDate().compareTo(o2.getShoppingDate()))
	                     .orElse(null);
	    }
	    return null; 
	}




}
