package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ReceiptDetailsServiceImpl implements ReceiptDetailsService {
    private final ReceiptDetailsRepo receiptDetailsRepo;

    public ReceiptDetailsServiceImpl(ReceiptDetailsRepo receiptDetailsRepo) {
        this.receiptDetailsRepo = receiptDetailsRepo;
    }

    @Override
    public List<ReceiptDetails> getAll() {
        return receiptDetailsRepo.findAll();
    }

    @Override
    public ReceiptDetails loadById(long id) {
        return receiptDetailsRepo.findById(id).orElse(null);
    }

    @Override
    public ReceiptDetails save(ReceiptDetails appUser) {
        return receiptDetailsRepo.save(appUser);
    }

    @Override
    public ReceiptDetails update(ReceiptDetails appUser) {
        return receiptDetailsRepo.save(appUser);
    }

    @Override
    public List<ReceiptDetails> saveAll(List<ReceiptDetails> appUsers) {
        return receiptDetailsRepo.saveAll(appUsers);
    }

    @Override
    public ReceiptDetails saveAndFlush(ReceiptDetails appUser) {
        return receiptDetailsRepo.saveAndFlush(appUser);
    }

    @Override
    public List<ReceiptDetails> saveAllAndFlush(List<ReceiptDetails> appUsers) {
        return receiptDetailsRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(ReceiptDetails appUser) {
        receiptDetailsRepo.delete(appUser);
    }
	/*@Override
	public List<ReceiptDetails> findByOrders ( Orders orders ) {
		return 		receiptDetailsRepo.findByOrders(orders);
	}*/

}
