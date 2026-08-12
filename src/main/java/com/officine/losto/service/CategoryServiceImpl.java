package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@Primary
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Category loadById(long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    @Override
    public Category save(Category appUser) {
        return categoryRepo.save(appUser);
    }

    @Override
    public Category update(Category appUser) {
        return categoryRepo.save(appUser);
    }

    @Override
    public List<Category> saveAll(List<Category> appUsers) {
        return categoryRepo.saveAll(appUsers);
    }

    @Override
    public Category saveAndFlush(Category appUser) {
        return categoryRepo.saveAndFlush(appUser);
    }

    @Override
    public List<Category> saveAllAndFlush(List<Category> appUsers) {
        return categoryRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(Category appUser) {
        categoryRepo.delete(appUser);
    }

    @Override
    public Category loadByCode(String code) {
        return categoryRepo.findByCode(code);
    }

    @Override
    public List<Category> findByCriteria(String description, String name) {
        return categoryRepo.findByDescriptionContainingOrCodeContaining(description, name);
    }

}
