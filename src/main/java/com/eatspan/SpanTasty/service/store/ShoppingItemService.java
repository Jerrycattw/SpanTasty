package com.eatspan.SpanTasty.service.store;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingItemId;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;
import com.eatspan.SpanTasty.repository.store.ShoppingItemRepository;

@Service
public class ShoppingItemService {
	
	@Autowired
	private ShoppingItemRepository shoppingItemRepo;
	
	public ShoppingItem addShoppingItem(ShoppingItemId shoppingItemId, Integer shoppingItemQuantity, Integer ShoppingItemPrice, Product product, ShoppingOrder shoppingOrder) {
	    ShoppingItem shoppingItem = new ShoppingItem();
	    
	    // 設置複合主键
	    shoppingItem.setId(shoppingItemId);
	    shoppingItem.setShoppingItemQuantity(shoppingItemQuantity);
	    shoppingItem.setShoppingItemPrice(ShoppingItemPrice);
	    
	    // 設置與產品和訂單的關聯
	    shoppingItem.setProduct(product);
	    shoppingItem.setShoppingOrder(shoppingOrder);
	    
	    return shoppingItemRepo.save(shoppingItem); 
	}

	public void deleteShoppingItem(ShoppingItemId shoppingItemId) {
        shoppingItemRepo.deleteById(shoppingItemId);
    }

	public boolean deleteAllShoppingItems(int shoppingId) {
        try {
            shoppingItemRepo.deleteAllByShoppingOrderId(shoppingId);
            return true;
        } catch (Exception e) {
            return false; 
        }
    }
	
    public ShoppingItem updateShoppingItem(ShoppingItemId shoppingItemId, Integer shoppingItemQuantity, Integer shoppingItemprice) {
        Optional<ShoppingItem> optional = shoppingItemRepo.findById(shoppingItemId);
        if (optional.isPresent()) {
            ShoppingItem shoppingItem = optional.get();
            if (shoppingItemQuantity != null) {
                shoppingItem.setShoppingItemQuantity(shoppingItemQuantity);
            }
            if (shoppingItemprice != null) {
                shoppingItem.setShoppingItemPrice(shoppingItemprice);
            }
            return shoppingItemRepo.save(shoppingItem);
        }
        return null;
    }

    
    public ShoppingItem findShoppingItemById(ShoppingItemId shoppingItemId) {
        Optional<ShoppingItem> optional = shoppingItemRepo.findById(shoppingItemId);
        
        if(optional.isPresent()) {
        	return optional.get();
        }
        return null;
    }

    public List<ShoppingItem> findAllShoppingItems() {
        return shoppingItemRepo.findAll();
    }
	
}
