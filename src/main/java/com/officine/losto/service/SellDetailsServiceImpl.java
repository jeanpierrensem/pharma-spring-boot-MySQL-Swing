package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class SellDetailsServiceImpl implements SellDetailsService {
    private final SellDetailsRepo sellDetailsRepo;

    public SellDetailsServiceImpl(SellDetailsRepo sellDetailsRepo) {
        this.sellDetailsRepo = sellDetailsRepo;
    }

    @Override
    public List<SellDetails> getAll() {
        return sellDetailsRepo.findAll();
    }

    @Override
    public SellDetails loadById(long id) {
        return sellDetailsRepo.findById(id).orElse(null);
    }

    @Override
    public SellDetails save(SellDetails appUser) {
        return sellDetailsRepo.save(appUser);
    }

    @Override
    public SellDetails update(SellDetails appUser) {
        return sellDetailsRepo.save(appUser);
    }

    @Override
    public List<SellDetails> saveAll(List<SellDetails> appUsers) {
        return sellDetailsRepo.saveAll(appUsers);
    }

    @Override
    public SellDetails saveAndFlush(SellDetails appUser) {
        return sellDetailsRepo.saveAndFlush(appUser);
    }

    @Override
    public List<SellDetails> saveAllAndFlush(List<SellDetails> appUsers) {
        return sellDetailsRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(SellDetails appUser) {
        sellDetailsRepo.delete(appUser);
    }

    @Override
    public List<SellDetails> loadBySell(Sell sell) {
        return sellDetailsRepo.findBySell(sell);


    }
	/*@Override
	public List<SellDetails> findByCriteria( Sell  sell ,  Product product ) {
		//return 		sellDetailsRepo.findBySellContainingOrProductContaining(sell, product);
	}
*/
}
