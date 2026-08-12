package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class OrdersServiceImpl implements OrdersService {
    private final OrdersRepo ordersRepo;

    public OrdersServiceImpl(OrdersRepo ordersRepo) {
        this.ordersRepo = ordersRepo;
    }

    @Override
    public List<Orders> getAll() {
        return ordersRepo.findAll();
    }

    @Override
    public Orders loadById(long id) {
        return ordersRepo.findById(id).orElse(null);
    }

    @Override
    public Orders save(Orders appUser) {
        return ordersRepo.save(appUser);
    }

    @Override
    public Orders update(Orders appUser) {
        return ordersRepo.save(appUser);
    }

    @Override
    public List<Orders> saveAll(List<Orders> appUsers) {
        return ordersRepo.saveAll(appUsers);
    }

    @Override
    public Orders saveAndFlush(Orders appUser) {
        return ordersRepo.saveAndFlush(appUser);
    }

    @Override
    public List<Orders> saveAllAndFlush(List<Orders> appUsers) {
        return ordersRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(Orders appUser) {
        ordersRepo.delete(appUser);
    }

    //implemented from
    @Override
    public Orders loadByCode(String code) {
        return ordersRepo.findByNumber(code);
    }

    @Override
    public List<Orders> findByCriteria(String numer, String orderDate, String description) {
        return ordersRepo.findByNumberContainingOrOrderDateContainingOrDescriptionContaining(numer, orderDate, description);
    }

}
