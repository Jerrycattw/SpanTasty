package com.eatspan.SpanTasty.controller.reservation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@Controller
@RequestMapping("/restaurant")
@PropertySource("upload.properties")
public class RestaurantController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Value("${upload.reservationPath}")
	private String uploadPath;
	
	
    @GetMapping("/getAll")
    public String getAllRestaurants(Model model, @RequestParam(defaultValue = "0") Integer page) {
        Page<Restaurant> restaurantsPage = restaurantService.findAllRestaurantsPage(page+1);
        model.addAttribute("restaurantsPage", restaurantsPage);
        return "spantasty/reservation/getAllRestaurant";
    }
    
    
    // ajax 查詢餐廳
    @GetMapping("/getapi/{id}")
    @ResponseBody
    public Restaurant getRestaurant(@PathVariable(name = "id") Integer restaurantId) {
    	return restaurantService.findRestaurantById(restaurantId);
    }
    
    
	
    // 查詢餐廳(byId)
    @GetMapping("/get/{id}")
    public String getRestaurantById(@PathVariable("id") Integer restaurantId, Model model) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "reservation/getRestaurant";
    }
    
    
    
    // 把頁面導向addRestaurant
    @GetMapping("/add")
    public String toAddRestaurant() {
        return "reservation/addRestaurant";
    }
    
    
	
    // 新增餐廳
    @PostMapping("/addPost")
    public String addRestaurant(@ModelAttribute Restaurant addRestaurant, 
                                @RequestParam("rimg") MultipartFile file) throws IllegalStateException, IOException {
        
        // 建立圖片保存的目錄
        File fileSaveDirectory = new File(uploadPath);
        if (!fileSaveDirectory.exists()) {
            fileSaveDirectory.mkdirs();
        }

        // 檢查文件是否不為空，並處理上傳
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = addRestaurant.getRestaurantName() + extension;

            // 保存檔案到指定路徑
            File fileToSave = new File(uploadPath + File.separator + newFileName);
            file.transferTo(fileToSave);
            addRestaurant.setRestaurantImg("/SpanTasty/upload/reservation/" + newFileName);
        }

        // 新增餐廳至資料庫
        restaurantService.addRestaurant(addRestaurant);
        
        return "redirect:/restaurant/getAll";
    }
    
    
    
    
    
    // 把頁面導向setRestaurant
    @GetMapping("/set/{id}")
    public String toSetRestaurant(@PathVariable("id") Integer restaurantId, Model model) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        model.addAttribute("restaurant", restaurant);
        return "reservation/setRestaurant";
    }
    
    
    
    // 修改餐廳
    @PutMapping("/setPut")
    public String updateRestaurant(@ModelAttribute Restaurant setRestaurant, 
    							   @RequestParam("rimg") MultipartFile file) throws IllegalStateException, IOException {
    	
        // 建立圖片保存的目錄
        File fileSaveDirectory = new File(uploadPath);
        if (!fileSaveDirectory.exists()) {
            fileSaveDirectory.mkdirs();
        }

        // 檢查文件是否不為空，並處理上傳
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = setRestaurant.getRestaurantName() + extension;

            // 保存檔案到指定路徑
            File fileToSave = new File(uploadPath + File.separator + newFileName);
            file.transferTo(fileToSave);
            setRestaurant.setRestaurantImg("/SpanTasty/upload/reservation/" + newFileName);
        } else {
        	String restaurantImg = restaurantService.findRestaurantById(setRestaurant.getRestaurantId()).getRestaurantImg();
			setRestaurant.setRestaurantImg(restaurantImg);
		}

        restaurantService.updateRestaurant(setRestaurant);
        
        return "redirect:/restaurant/getAll";
    }
    
    
    
    
    
    // 刪除餐廳
    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable("id") Integer restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    

    

	
	
	
	
	
}
