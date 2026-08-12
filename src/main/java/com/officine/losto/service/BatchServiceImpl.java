package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class BatchServiceImpl implements BatchService {
    private final BatchRepo batchRepo;

    public BatchServiceImpl(BatchRepo batchRepo) {
        this.batchRepo = batchRepo;
    }

    @Override
    public List<Batch> getAll() {
        return batchRepo.findAll();
    }

    @Override
    public Batch loadById(long id) {
        return batchRepo.findById(id).orElse(null);
    }

    @Override
    public Batch save(Batch appUser) {
        return batchRepo.save(appUser);
    }

    @Override
    public Batch update(Batch appUser) {
        return batchRepo.save(appUser);
    }

    @Override
    public List<Batch> saveAll(List<Batch> appUsers) {
        return batchRepo.saveAll(appUsers);
    }

    @Override
    public Batch saveAndFlush(Batch appUser) {
        return batchRepo.saveAndFlush(appUser);
    }

    @Override
    public List<Batch> saveAllAndFlush(List<Batch> appUsers) {
        return batchRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(Batch appUser) {
        batchRepo.delete(appUser);
    }

    @Override
    public Batch loadByCode(String code) {
        return batchRepo.findByNumber(code);
    }

    @Override
    public List<Batch> findByCriteria(String code) {
        return batchRepo.findByNumberContaining(code);
    }

    @Override
    public List<Batch> findByProviderId(long providerId) {
        return batchRepo.findByProvider_Id(providerId);
    }
}
