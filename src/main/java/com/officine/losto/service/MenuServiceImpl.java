package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepo menuRepo;
    private final MenuCatalogSyncService menuCatalogSyncService;

    public MenuServiceImpl(MenuRepo menuRepo, MenuCatalogSyncService menuCatalogSyncService) {
        this.menuRepo = menuRepo;
        this.menuCatalogSyncService = menuCatalogSyncService;
    }

    @Override
    public List<Menu> getAll() {
        return menuRepo.findAllWithGroups();
    }

    @Override
    public Menu loadById(long id) {
        return menuRepo.findById(id).orElse(null);
    }

    @Override
    public Menu save(Menu appUser) {
        return menuRepo.save(appUser);
    }

    @Override
    public Menu update(Menu appUser) {
        return menuRepo.save(appUser);
    }

    @Override
    public List<Menu> saveAll(List<Menu> appUsers) {
        return menuRepo.saveAll(appUsers);
    }

    @Override
    public Menu saveAndFlush(Menu appUser) {
        return menuRepo.saveAndFlush(appUser);
    }

    @Override
    public List<Menu> saveAllAndFlush(List<Menu> appUsers) {
        return menuRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(Menu appUser) {
        menuRepo.delete(appUser);
    }

    @Override
    public Menu loadByName(String username) {
        return menuRepo.findByName(username);
    }

    @Override
    public List<Menu> findByCriteria(String description, String name) {
        return menuRepo.findByDescriptionContainingOrNameContaining(description, name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> loadHabilitationMenuTree() {
        List<Menu> roots = menuRepo.findRootsWithImmediateChildren();
        List<Long> level1Ids = new ArrayList<>();
        for (Menu root : roots) {
            if (root.getChildren() == null) {
                continue;
            }
            for (Menu child : root.getChildren()) {
                if (child.getId() != null) {
                    level1Ids.add(child.getId());
                }
            }
        }
        if (!level1Ids.isEmpty()) {
            menuRepo.findByIdInWithChildrenFetched(level1Ids);
        }
        return roots;
    }

    @Override
    public int syncHabilitationCatalog() {
        return menuCatalogSyncService.syncFromCatalog();
    }

}
