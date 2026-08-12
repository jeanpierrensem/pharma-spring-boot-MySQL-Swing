package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class PackagingServiceImpl implements PackagingService {
    private final PackagingRepo packagingRepo;

    public PackagingServiceImpl(PackagingRepo packagingRepo) {
        this.packagingRepo = packagingRepo;
    }

    @Override
    public List<Packaging> getAll() {
        return packagingRepo.findAll();
    }

    @Override
    public Packaging loadById(long id) {
        return packagingRepo.findById(id).orElse(null);
    }

    @Override
    public Packaging save(Packaging appUser) {
        return packagingRepo.save(appUser);
    }

    @Override
    public Packaging update(Packaging appUser) {
        return packagingRepo.save(appUser);
    }

    @Override
    public List<Packaging> saveAll(List<Packaging> appUsers) {
        return packagingRepo.saveAll(appUsers);
    }

    @Override
    public Packaging saveAndFlush(Packaging appUser) {
        return packagingRepo.saveAndFlush(appUser);
    }

    @Override
    public List<Packaging> saveAllAndFlush(List<Packaging> appUsers) {
        return packagingRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(Packaging appUser) {
        packagingRepo.delete(appUser);
    }

    @Override
    public Packaging loadByCode(String code) {
        return packagingRepo.findByCode(code);
    }

    @Override
    public List<Packaging> findByCriteria(String description, String name) {
        return packagingRepo.findByDescriptionContainingOrCodeContaining(description, name);
    }

}
