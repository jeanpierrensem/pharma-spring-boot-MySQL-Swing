package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class SectionServiceImpl implements SectionService {
    private final SectionRepo sectionRepo;

    public SectionServiceImpl(SectionRepo sectionRepo) {
        this.sectionRepo = sectionRepo;
    }

    @Override
    public List<Section> getAll() {
        return sectionRepo.findAll();
    }

    @Override
    public Section loadById(long id) {
        return sectionRepo.findById(id).orElse(null);
    }

    @Override
    public Section save(Section appUser) {
        return sectionRepo.save(appUser);
    }

    @Override
    public Section update(Section appUser) {
        return sectionRepo.save(appUser);
    }

    @Override
    public List<Section> saveAll(List<Section> appUsers) {
        return sectionRepo.saveAll(appUsers);
    }

    @Override
    public Section saveAndFlush(Section appUser) {
        return sectionRepo.saveAndFlush(appUser);
    }

    @Override
    public List<Section> saveAllAndFlush(List<Section> appUsers) {
        return sectionRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(Section appUser) {
        sectionRepo.delete(appUser);
    }

    @Override
    public Section loadByCode(String code) {
        return sectionRepo.findByCode(code);
    }

    @Override
    public List<Section> findByCriteria(String code, String description) {
        return sectionRepo.findByDescriptionContainingOrCodeContaining(code, description);
    }

}
