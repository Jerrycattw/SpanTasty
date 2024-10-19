package com.eatspan.SpanTasty.service.store;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingItemId;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;
import com.eatspan.SpanTasty.repository.store.ShoppingItemRepository;
import com.eatspan.SpanTasty.repository.store.ShoppingOrderRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class ShoppingOrderService {

	@Autowired
	private ShoppingOrderRepository shoppingOrderRepo;

	@Autowired
	@Lazy
	private ShoppingItemService shoppingItemService;
	
	@Autowired 
	ShoppingItemRepository shoppingItemRepo;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private HttpSession session;

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

//	public String ecpayCheckout() {
//
//		AllInOne all = new AllInOne("");
//
//		AioCheckOutALL obj = new AioCheckOutALL();
//		obj.setMerchantTradeNo("testCompany0004");
//		obj.setMerchantTradeDate("2017/01/01 08:05:23");
//		obj.setTotalAmount("50");
//		obj.setTradeDesc("test Description");
//		obj.setItemName("TestItem");
//		// 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok
////		obj.setReturnURL("<http://211.23.128.214:5000>");
//		obj.setReturnURL("<http://localhost:8080/SpanTasty/StarCups>");
//		obj.setNeedExtraPaidInfo("N");
//		// 商店轉跳網址 (Optional)
////		obj.setClientBackURL("<http://192.168.1.37:8080/>");
//		obj.setClientBackURL("<http://localhost:8080/SpanTasty/StarCups>");
//		String form = all.aioCheckOut(obj, null);
//
//		return form;
//	}

	

//	    // 取得購物訂單
//	    public String ecpayCheckout(Integer shoppingId) {
//	        // 获取当前时间
//	        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
//	        
//	        // 计算总金额
//	        Integer totalAmount = calculateTotalAmount(shoppingId);
//
//	        // 设置 ECPay 结账信息
//	        AllInOne all = new AllInOne("");
//	        AioCheckOutALL obj = new AioCheckOutALL();
////	        obj.setMerchantTradeNo(String.valueOf(shoppingId)); // 使用 shoppingId 作為交易編號
//	        obj.setMerchantTradeNo("SC" + System.currentTimeMillis()); // 使用 shoppingId 作為交易編號
//	        obj.setMerchantTradeDate(currentDateTime); // 使用當前時間
//	        obj.setTotalAmount(String.valueOf(totalAmount)); // 使用總金額
//	        obj.setItemName("商品名稱"); // 可根據需要設定商品名稱
//	        obj.setTradeDesc("test Description");
////	        obj.setReturnURL("https://5b6d-61-222-34-1.ngrok-free.app/SpanTasty/StarCups/allProduct");
//	        obj.setReturnURL("https://5b6d-61-222-34-1.ngrok-free.app/SpanTasty/StarCups/OrderConfirm");
//	        obj.setNeedExtraPaidInfo("N");
////	        obj.setClientBackURL("http://localhost:8080/SpanTasty/StarCups/allProduct");
//	        obj.setClientBackURL("http://localhost:8080/SpanTasty/StarCups/OrderConfirm");
//
//	        // 生成 ECPay 表单			
//	        return all.aioCheckOut(obj, null);
//	    }

    // 取得購物訂單
    public String ecpayCheckout(Integer shoppingId) {
        // 获取当前时间
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        
        // 计算总金额
        Integer totalAmount = calculateTotalAmount(shoppingId);

        // 设置 ECPay 结账信息
        AllInOne all = new AllInOne("");
        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo(String.valueOf(shoppingId)); // 使用 shoppingId 作為交易編號
        obj.setMerchantTradeNo("SC" + System.currentTimeMillis()); // 使用 shoppingId 作為交易編號
        obj.setMerchantTradeDate(currentDateTime); // 使用當前時間
        obj.setTotalAmount(String.valueOf(totalAmount)); // 使用總金額

        // 獲取所有商品名稱和金額資訊
        List<ShoppingItem> shoppingItems = shoppingItemService.findShoppingItemById(shoppingId);
        StringBuilder itemNames = new StringBuilder();

        for (ShoppingItem item : shoppingItems) {
            Product product = item.getProduct(); // 假設 ShoppingItem 中有 Product 對象
            if (product != null) {
                Integer quantity = item.getShoppingItemQuantity(); // 獲取數量
                Integer price = item.getShoppingItemPrice(); // 獲取價格
                itemNames.append(product.getProductName())
                          .append(" ")
                          .append(quantity)
                          .append("*NT$")
                          .append(price)
                          .append(".  /  "); // 格式化商品資訊
            }
        }

        // 移除最後的逗號和空格
        if (itemNames.length() > 0) {
            itemNames.setLength(itemNames.length() - 2);
        }

        obj.setItemName(itemNames.toString()); // 設置商品名稱
        obj.setTradeDesc("test Description");
//        obj.setReturnURL("https://5b6d-61-222-34-1.ngrok-free.app/SpanTasty/StarCups/allProduct");
        obj.setReturnURL("https://5b6d-61-222-34-1.ngrok-free.app/SpanTasty/StarCups/OrderConfirm");
        obj.setNeedExtraPaidInfo("N");
//        obj.setClientBackURL("http://localhost:8080/SpanTasty/StarCups/allProduct");
        obj.setClientBackURL("http://localhost:8080/SpanTasty/StarCups/OrderConfirm");

        // 生成 ECPay 表单			
        return all.aioCheckOut(obj, null);
    }

}
