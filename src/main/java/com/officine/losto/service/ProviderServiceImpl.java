package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ProviderServiceImpl implements ProviderService {
    private final ProviderRepo providerRepo;

    public ProviderServiceImpl(ProviderRepo providerRepo) {
        this.providerRepo = providerRepo;
    }

    @Override
    public List<Provider> getAll() {
        return providerRepo.findAll();
    }

    @Override
    public Provider loadById(long id) {
        return providerRepo.findById(id).orElse(null);
    }

    @Override
    public Provider save(Provider appUser) {
        return providerRepo.save(appUser);
    }

    @Override
    public Provider update(Provider appUser) {
        return providerRepo.save(appUser);
    }

    @Override
    public List<Provider> saveAll(List<Provider> appUsers) {
        return providerRepo.saveAll(appUsers);
    }

    @Override
    public Provider saveAndFlush(Provider appUser) {
        return providerRepo.saveAndFlush(appUser);
    }

    @Override
    public List<Provider> saveAllAndFlush(List<Provider> appUsers) {
        return providerRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(Provider appUser) {
        providerRepo.delete(appUser);
    }

    @Override
    public Provider loadByCode(String code) {
        return providerRepo.findByCode(code);
    }

    @Override
    public List<Provider> findByCriteria(String designation, String code) {
        return providerRepo.findByPhoneNumberContainingOrCodeContaining(designation, code);
    }

}
