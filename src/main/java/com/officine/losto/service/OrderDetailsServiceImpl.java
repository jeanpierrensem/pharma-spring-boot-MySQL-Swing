package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final OrderDetailsRepo orderDetailsRepo;

    public OrderDetailsServiceImpl(OrderDetailsRepo orderDetailsRepo) {
        this.orderDetailsRepo = orderDetailsRepo;
    }


    @Override
    public List<OrdersDetails> getAll() {
        return orderDetailsRepo.findAll();
    }

    @Override
    public OrdersDetails loadById(long id) {
        return orderDetailsRepo.findById(id).orElse(null);
    }

    @Override
    public OrdersDetails save(OrdersDetails appUser) {
        return orderDetailsRepo.save(appUser);
    }

    @Override
    public OrdersDetails update(OrdersDetails appUser) {
        return orderDetailsRepo.save(appUser);
    }

    @Override
    public List<OrdersDetails> saveAll(List<OrdersDetails> appUsers) {
        return orderDetailsRepo.saveAll(appUsers);
    }

    @Override
    public OrdersDetails saveAndFlush(OrdersDetails appUser) {
        return orderDetailsRepo.saveAndFlush(appUser);
    }

    @Override
    public List<OrdersDetails> saveAllAndFlush(List<OrdersDetails> appUsers) {
        return orderDetailsRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(OrdersDetails appUser) {
        orderDetailsRepo.delete(appUser);
    }

    @Override
    public OrdersDetails loadByOrders(Orders orders) {

        return orderDetailsRepo.findByOrders(orders);
    }

    @Override
    public List<OrdersDetails> findByCriteria(String code, String description) {
        return null;
    }
	/*@Override
	public List<OrdersDetails> findByCriteria( String description ,   String name ) {
		return 		orderDetailsRepo.findByDescriptionContainingOrCodeContaining(description, name);
	}*/

}
