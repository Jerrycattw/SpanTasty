package com.eatspan.SpanTasty.service.store;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.store.ShoppingItem;
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
	private ShoppingItemService shoppingItemService;

	@PersistenceContext
	private EntityManager entityManager;

	public Integer calculateTotalAmount(Integer shoppingId) {
		String hql = "SELECT SUM(i.shoppingItemPrice * i.shoppingItemQuantity) "
				+ "FROM ShoppingItem i WHERE i.shoppingOrder.shoppingId = :shoppingId";
		TypedQuery<Integer> query = entityManager.createQuery(hql, Integer.class);
		query.setParameter("shoppingId", shoppingId);

		Integer totalAmount = query.getSingleResult();
		return (totalAmount != null) ? totalAmount : 0;
	}

	@Transactional
	public ShoppingOrder addShoppingOrder(Integer memberId, String shoppingMemo) {
		ShoppingOrder shoppingOrder = new ShoppingOrder();

		shoppingOrder.setShoppingDate(LocalDateTime.now());
		shoppingOrder.setMemberId(memberId);
		shoppingOrder.setShoppingStatus(1);
		shoppingOrder.setShoppingMemo(shoppingMemo);

		ShoppingOrder savedOrder = shoppingOrderRepo.save(shoppingOrder);
		savedOrder.setShoppingTotal(0); 

		return shoppingOrderRepo.save(shoppingOrder);
	}

	@Transactional
	public void deleteShoppingOrder(Integer shoppingId) {
		shoppingItemService.deleteAllShoppingItems(shoppingId);
		shoppingOrderRepo.deleteById(shoppingId);
	}

	@Transactional
	public ShoppingOrder updateShoppingOrder(Integer shoppingId, String shoppingMemo, Integer shoppingStatus) {
	    Optional<ShoppingOrder> optionalOrder = shoppingOrderRepo.findById(shoppingId);

	    if (optionalOrder.isPresent()) {
	        ShoppingOrder shoppingOrder = optionalOrder.get();
	        
	        if (shoppingMemo != null) {
	            shoppingOrder.setShoppingMemo(shoppingMemo);
	        }
	        if (shoppingStatus != null) {
	            shoppingOrder.setShoppingStatus(shoppingStatus);
	        }

	        shoppingOrder.setShoppingTotal(calculateTotalAmount(shoppingId));

	        return shoppingOrderRepo.save(shoppingOrder);
	    }
	    return null; 
	}

	
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

}
