package com.eatspan.SpanTasty.controller.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ShoppingOrderService shoppingOrderService;

    @Autowired
    private ShoppingItemService shoppingItemService; 
    

    
    @GetMapping("/addShoppingCart")
    public String showShoppingCart(HttpSession session, Model model) {
        // 獲取所有產品
        List<Product> products = productService.findAllProduct();
        model.addAttribute("products", products);

        // 獲取購物車內容
        List<ShoppingItem> cart = (List<ShoppingItem>) session.getAttribute("cart");
        int cartSize = (cart != null) ? cart.size() : 0;
        model.addAttribute("cartSize", cartSize);
        
        
        
        // 計算總金額
        int totalAmount = 0;
        if (cart != null) {
            for (ShoppingItem item : cart) {
                totalAmount += item.getShoppingItemPrice() * item.getShoppingItemQuantity(); 
            }
        }
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("cart", cart); 

        return "store/shopping/shoppingCart"; 
    }

//    @PostMapping("/cart/add/{id}")
//    public String addToCart(@PathVariable Integer id, HttpSession session) {
//        Integer shoppingId = (Integer) session.getAttribute("shoppingId");
//        ShoppingOrder shoppingOrder = shoppingOrderService.findShoppingOrderById(shoppingId);
//        
//        // 如果購物訂單不存在，則創建新的訂單
//        if (shoppingOrder == null) {
//            Integer memberId = (Integer) session.getAttribute("memberId");
//            if (memberId == null) {
//                memberId = 1; // 預設會員 ID
//            }
//            shoppingOrder = shoppingOrderService.createNewShoppingOrder(memberId);
//            session.setAttribute("shoppingId", shoppingOrder.getShoppingId());
//        }
//        
//        // 使用複合主鍵來查找購物項目
//        ShoppingItemId shoppingItemId = new ShoppingItemId(shoppingId, id);
//        ShoppingItem shoppingItemById = shoppingItemService.findShoppingItemById(shoppingItemId);
//        
//        if (shoppingItemById != null) {
//            // 如果已存在，增加數量
//            shoppingItemById.setShoppingItemQuantity(shoppingItemById.getShoppingItemQuantity() + 1);
//            shoppingItemService.merge(shoppingItemById); // 使用 merge
//        } else {
//            // 如果不存在，創建新的購物項目
//            ShoppingItem shoppingItem = new ShoppingItem(shoppingItemId, 1);
//            shoppingItemService.merge(shoppingItem); // 使用 merge
//        }
//        
//        // 更新 session 中的購物項目列表
//        List<ShoppingItem> items = shoppingItemService.findItemsByShoppingOrder(shoppingOrder); // 確保重新獲取
//        session.setAttribute("items", items);
//        
//        return "redirect:/shoppingCart/addShoppingCart";
//    }



    
    
    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Integer id, HttpSession session) {
    	
    	
    	
        Integer shoppingId = (Integer) session.getAttribute("shoppingId");
        ShoppingOrder shoppingOrder = shoppingOrderService.findShoppingOrderById(shoppingId);
        if (shoppingOrder==null) {
            Integer memberId = (Integer) session.getAttribute("memberId"); // 確保這裡能獲取到 memberId
            if(memberId==null) {
            	memberId=1;
            }
            shoppingOrder = shoppingOrderService.createNewShoppingOrder(memberId);
            session.setAttribute("shoppingId", shoppingOrder.getShoppingId());
		}
        
//        if (shoppingId == null) {
//            // 創建新的購物訂單，並返回 shoppingId
//            Integer memberId = (Integer) session.getAttribute("memberId"); // 確保這裡能獲取到 memberId
//            shoppingId = shoppingOrderService.createNewShoppingOrder(memberId);
//            session.setAttribute("shoppingId", shoppingId);
//        }
        
        System.out.println(66666666);
        System.out.println(id);
        System.out.println(shoppingId);
        
        ShoppingItemId shoppingItemId = new ShoppingItemId(shoppingId, id); // 使用複合主鍵
        ShoppingItem shoppingItemById = shoppingItemService.findShoppingItemById(shoppingItemId);
        
        if(shoppingItemById!=null) {
        	shoppingItemById.setShoppingItemQuantity(shoppingItemById.getShoppingItemQuantity()+1);
        }else {
        	ShoppingItem shoppingItem = shoppingItemService.addShoppingItem(new ShoppingItem(shoppingItemId, 1));
		}
        
//        ShoppingItem shoppingItem = new ShoppingItem();
//        shoppingItem.setId(shoppingItemId);
//        shoppingItem.setShoppingItemQuantity(1); // 初始數量

        // 獲取產品價格，假設產品一定存在
//        Integer productPrice = productService.findProductPriceByProductId(id);
//        shoppingItem.setShoppingItemPrice(productPrice); // 設置價格
//
//        List<ShoppingItem> cart = (List<ShoppingItem>) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new ArrayList<>();
//        }
//
//        boolean found = false;
//        for (ShoppingItem item : cart) {
//            if (item.getProductId().equals(id) && item.getShoppingId().equals(shoppingId)) {
//                item.setShoppingItemQuantity(item.getShoppingItemQuantity() + 1); // 增加數量
//                found = true;
//                break;
//            }
//        }
//
//        if (!found) {
//            cart.add(shoppingItem); // 添加新商品
//        }
        
//        ShoppingOrder shoppingOrderById2 = shoppingOrderService.findShoppingOrderById(shoppingId);
//		ShoppingOrder shoppingOrder = shoppingOrderById2;
//        
        List<ShoppingItem> items = shoppingOrder.getItems();
//        
//        session.setAttribute("shoppingOrder", shoppingOrder);
        session.setAttribute("items", items);
//        session.setAttribute("cart", cart);
        return "redirect:/shoppingCart/addShoppingCart";
    }
    
    
    
    

    @PostMapping("/cart/update/{id}/{quantity}")
    public String updateCart(@PathVariable Integer id, @PathVariable int quantity, HttpSession session) {
        List<ShoppingItem> cart = (List<ShoppingItem>) session.getAttribute("cart");
        if (cart != null) {
            for (ShoppingItem item : cart) {
                if (item.getId().getProductId().equals(id)) {
                    if (quantity <= 0) {
                        cart.remove(item); // 移除商品
                    } else {
                        item.setShoppingItemQuantity(quantity); // 更新數量
                    }
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }
        return "redirect:/shoppingCart/addShoppingCart";
    }

    @GetMapping("/cart")
    @ResponseBody
    public List<ShoppingItem> getCart(HttpSession session) {
        List<ShoppingItem> cart = (List<ShoppingItem>) session.getAttribute("cart");
        return (cart != null) ? cart : new ArrayList<>();
    }
}
