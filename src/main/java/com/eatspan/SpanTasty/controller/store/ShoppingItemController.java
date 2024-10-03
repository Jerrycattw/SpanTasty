package com.eatspan.SpanTasty.controller.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingItemId;
import com.eatspan.SpanTasty.service.store.ShoppingItemService;

@Controller
@RequestMapping("/shoppingItem")
public class ShoppingItemController {

	@Autowired
	private ShoppingItemService shoppingItemService;
	
//	@GetMapping("/add")
//	public String toAddOrder(Model model) {
//		
//	}
	
	
	@PostMapping("/addPost")
	public ResponseEntity<?> addShoppingItem(@RequestBody ShoppingItem shoppingItem) {
	    ShoppingItem savedShoppingItem = shoppingItemService.addShoppingItem(shoppingItem);
	    return new ResponseEntity<>(savedShoppingItem, HttpStatus.CREATED);
	}
	
	
//	@PostMapping("/add")
//	public ResponseEntity<?> addShoppingItem(@RequestBody ShoppingItem shoppingItem) {
//	    ShoppingItem savedShoppingItem = shoppingItemService.addShoppingItem(shoppingItem);
//	    if (savedShoppingItem != null) {
//	        return new ResponseEntity<>(savedShoppingItem, HttpStatus.CREATED);
//	    }
//	    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	}
//	
//	 @DeleteMapping("/del/{shoppingId}/{productId}")
//	    public ResponseEntity<?> deleteShoppingItem(@PathVariable("shoppingId") Integer shoppingId,
//	                                                 @PathVariable("productId") Integer productId) {
//	        ShoppingItemId shoppingItemId = new ShoppingItemId(shoppingId, productId);
//	        if (shoppingItemService.findShoppingItemById(shoppingItemId) == null) {
//	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 購物項目不存在
//	        }
//	        shoppingItemService.deleteShoppingItem(shoppingItemId);
//	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	    }
	 
	 
	  @PutMapping("/update")
	    public ResponseEntity<ShoppingItem> updateShoppingItem(@RequestBody ShoppingItem shoppingItem) {
	        ShoppingItem item = shoppingItemService.updateShoppingItem(shoppingItem);
	        if (item != null) {
	            return new ResponseEntity<>(item, HttpStatus.OK);
	        }
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	    
//	    @PutMapping("/update/{shoppingId}/{productId}")
//	    public ResponseEntity<?> updateShoppingItem(@PathVariable("shoppingId") Integer shoppingId,
//	                                                 @PathVariable("productId") Integer productId,
//	                                                 @RequestBody ShoppingItem updatedShoppingItem) {
//	        ShoppingItemId shoppingItemId = new ShoppingItemId(shoppingId, productId);
//	        ShoppingItem shoppingItem = shoppingItemService.updateShoppingItem(shoppingItemId, 
//	                updatedShoppingItem.getShoppingItemQuantity(), 
//	                updatedShoppingItem.getShoppingItemPrice());
//	        if (shoppingItem != null) {
//	            return new ResponseEntity<>(shoppingItem, HttpStatus.OK);
//	        }
//	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	    }
	    
//		@GetMapping("/find/{id}")
//		public ResponseEntity<?> findShoppingItemById(@PathVariable("id") Integer shoppingId){
//			ShoppingItem shoppingItem = shoppingItemService.findShoppingItemById(shoppingItem);
//			if(shoppingItem != null) {
//				return new ResponseEntity<>(shoppingItem, HttpStatus.OK);
//			}
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
//		}
	  
	  @GetMapping("/find/{shoppingId}")
	  public ResponseEntity<List<ShoppingItem>> findShoppingItemById(@PathVariable("shoppingId") Integer shoppingId) {
	      List<ShoppingItem> shoppingItems = shoppingItemService.findShoppingItemById(shoppingId);
	      if (shoppingItems.size() == 1) {
	          return new ResponseEntity<>(shoppingItems, HttpStatus.OK); // 返回單筆
	      } else if (shoppingItems.isEmpty()) {
	          return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 找不到資料
	      } else {
	          return new ResponseEntity<>(shoppingItems, HttpStatus.OK); // 返回多筆
	      }
	  }
//	    @GetMapping("/find/{shoppingId}/{productId}")
//	    public ResponseEntity<?> findShoppingItemById(@PathVariable("shoppingId") Integer shoppingId,
//	                                                   @PathVariable("productId") Integer productId) {
//	        ShoppingItemId shoppingItemId = new ShoppingItemId(shoppingId, productId);
//	        ShoppingItem shoppingItem = shoppingItemService.findShoppingItemById(shoppingItemId);
//	        if (shoppingItem != null) {
//	            return new ResponseEntity<>(shoppingItem, HttpStatus.OK);
//	        }
//	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	    }
	    
	    @GetMapping("/findAll")
	    public ResponseEntity<List<ShoppingItem>> findAllShoppingItems() {
	        List<ShoppingItem> shoppingItems = shoppingItemService.findAllShoppingItems();
	        return new ResponseEntity<>(shoppingItems, HttpStatus.OK);
	    }
	}