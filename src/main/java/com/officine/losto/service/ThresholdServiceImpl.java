package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class ThresholdServiceImpl implements ThresholdService {
    private final ThresholdRepo thresholdRepo;

    public ThresholdServiceImpl(ThresholdRepo thresholdRepo) {
        this.thresholdRepo = thresholdRepo;
    }

    @Override
    public List<Threshold> getAll() {
        return thresholdRepo.findAll();
    }

    @Override
    public Threshold loadById(long id) {
        return thresholdRepo.findById(id).orElse(null);
    }

    @Override
    public Threshold save(Threshold appUser) {
        return thresholdRepo.save(appUser);
    }

    @Override
    public Threshold update(Threshold appUser) {
        return thresholdRepo.save(appUser);
    }

    @Override
    public List<Threshold> saveAll(List<Threshold> appUsers) {
        return thresholdRepo.saveAll(appUsers);
    }

    @Override
    public Threshold saveAndFlush(Threshold appUser) {
        return thresholdRepo.saveAndFlush(appUser);
    }

    @Override
    public List<Threshold> saveAllAndFlush(List<Threshold> appUsers) {
        return thresholdRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(Threshold appUser) {
        thresholdRepo.delete(appUser);
    }

    @Override
    public Threshold loadByName(String code) {
        return thresholdRepo.findByCode(code);
    }

    @Override
    public List<Threshold> findByCriteria(String code, String description) {
        return thresholdRepo.findByCodeContainingOrDescriptionContaining(code, description);
    }

}
