package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class DrugTypeServiceImpl implements DrugTypeService {
    private final DrugTypeRepo drugTypeRepo;

    public DrugTypeServiceImpl(DrugTypeRepo drugTypeRepo) {
        this.drugTypeRepo = drugTypeRepo;
    }

    @Override
    public List<DrugType> getAll() {
        return drugTypeRepo.findAll();
    }

    @Override
    public DrugType loadById(long id) {
        return drugTypeRepo.findById(id).orElse(null);
    }

    @Override
    public DrugType save(DrugType appUser) {
        return drugTypeRepo.save(appUser);
    }

    @Override
    public DrugType update(DrugType appUser) {
        return drugTypeRepo.save(appUser);
    }

    @Override
    public List<DrugType> saveAll(List<DrugType> appUsers) {
        return drugTypeRepo.saveAll(appUsers);
    }

    @Override
    public DrugType saveAndFlush(DrugType appUser) {
        return drugTypeRepo.saveAndFlush(appUser);
    }

    @Override
    public List<DrugType> saveAllAndFlush(List<DrugType> appUsers) {
        return drugTypeRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(DrugType appUser) {
        drugTypeRepo.delete(appUser);
    }

    @Override
    public DrugType loadByCode(String code) {
        return drugTypeRepo.findByCode(code);
    }

    @Override
    public List<DrugType> findByCriteria(String description, String name) {
        return drugTypeRepo.findByDescriptionContainingOrCodeContaining(description, name);
    }

}
