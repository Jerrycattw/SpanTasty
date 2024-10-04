package com.eatspan.SpanTasty.controller.store;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.entity.store.Product;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingItemId;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;
import com.eatspan.SpanTasty.service.store.ProductService;
import com.eatspan.SpanTasty.service.store.ShoppingItemService;
import com.eatspan.SpanTasty.service.store.ShoppingOrderService;

@Controller
@RequestMapping("/shoppingItem")
public class ShoppingItemController {

    @Autowired
    private ShoppingItemService shoppingItemService;
    
    @Autowired
    private ShoppingOrderService shoppingOrderService;
    
    @Autowired
    private ProductService productService;
    

    
    @GetMapping("/itemDetail/{id}")
    public String toShoppingItem(@PathVariable Integer id, Model model) {
        ShoppingOrder shopping = shoppingOrderService.findShoppingOrderById(id);
        model.addAttribute("shopping", shopping);
        List<ShoppingItem> items = shoppingItemService.findShoppingItemById(id);
        model.addAttribute("items",items);
        List<Product> productList = productService.findAllProduct();
        model.addAttribute("productList",productList);
        Integer totalAmount = shoppingOrderService.calculateTotalAmount(id);
        model.addAttribute("totalAmount",totalAmount);
        
        return "store/shopping/shoppingItemDetail";
    }
    
//    // 顯示訂單明細
//    @GetMapping("/itemDetail/{id}")
//    public String toShoppingItem(@RequestParam Integer shoppingId, Model model) {
////    	ShoppingOrder shoppingOrder = shoppingOrderService.findShoppingOrderById(shoppingId);
//        List<ShoppingItem> items = shoppingItemService.findShoppingItemById(shoppingId);
//        model.addAttribute("items", items);
//        model.addAttribute("shopping", shoppingId);
//        model.addAttribute("productList", productService.findAllProduct()); 
//        return "store/shopping/shoppingItemDetail"; 
//    }
    
    
//    @PostMapping("/itemDetailsPost")
//    public String showShoppingItem(@RequestParam Integer shoppingId,
//    								@RequestParam Integer productId,Model model) {
//    	shoppingItemService.findAllShoppingItems(shoppingId,productId);
//    	
//    	return ""
//    }
    

    // 新增商品項目
    @PostMapping("/addItem")
    public String addShoppingItem(@ModelAttribute ShoppingItem shoppingItem) {
        shoppingItemService.addShoppingItem(shoppingItem);
        return "redirect:/shoppingItem/details?shoppingId=" + shoppingItem.getId().getShoppingId();
    }


    // 刪除商品項目
    @PostMapping("/delItem")
    public String deleteShoppingItem(@RequestParam Integer shoppingId, @RequestParam Integer productId) {
        ShoppingItemId shoppingItemId = new ShoppingItemId(shoppingId, productId);
        shoppingItemService.deleteShoppingItem(shoppingItemId);
        return "redirect:/shoppingItem/details?shoppingId=" + shoppingId;
    }

    // 刪除所有商品項目
    @PostMapping("/deleteAll")
    public String deleteAllShoppingItems(@RequestParam Integer shoppingId) {
        shoppingItemService.deleteAllShoppingItems(shoppingId);
        return "redirect:/shoppingItem/details?shoppingId=" + shoppingId;
    }
    
    // 更新商品項目
    @PostMapping("/updateItem")
    public String updateShoppingItem(@ModelAttribute ShoppingItem shoppingItem) {
    	shoppingItemService.updateShoppingItem(shoppingItem);
    	return "redirect:/shoppingItem/details?shoppingId=" + shoppingItem.getId().getShoppingId();
    }
}