package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class FormServiceImpl implements FormService {
    private final FormRepo formRepo;

    public FormServiceImpl(FormRepo formRepo) {
        this.formRepo = formRepo;
    }

    @Override
    public List<Form> getAll() {
        return formRepo.findAll();
    }

    @Override
    public Form loadById(long id) {
        return formRepo.findById(id).orElse(null);
    }

    @Override
    public Form save(Form appUser) {
        return formRepo.save(appUser);
    }

    @Override
    public Form update(Form appUser) {
        return formRepo.save(appUser);
    }

    @Override
    public List<Form> saveAll(List<Form> appUsers) {
        return formRepo.saveAll(appUsers);
    }

    @Override
    public Form saveAndFlush(Form appUser) {
        return formRepo.saveAndFlush(appUser);
    }

    @Override
    public List<Form> saveAllAndFlush(List<Form> appUsers) {
        return formRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(Form appUser) {
        formRepo.delete(appUser);
    }

    @Override
    public Form loadByCode(String code) {
        return formRepo.findByCode(code);
    }

    @Override
    public List<Form> findByCriteria(String description, String name) {
        return formRepo.findByDescriptionContainingOrCodeContaining(description, name);
    }

}
