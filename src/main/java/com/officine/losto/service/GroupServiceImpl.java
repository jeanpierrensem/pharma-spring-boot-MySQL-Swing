package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;
    private final MenuRepo menuRepo;

    public GroupServiceImpl(GroupRepo groupRepo, UserRepo userRepo, MenuRepo menuRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
        this.menuRepo = menuRepo;
    }

    //implemented from IService
    @Override
    public List<AppGroup> getAll() {
        return groupRepo.findAllWithMenus();
    }

    @Override
    public AppGroup loadById(long id) {
        return groupRepo.findById(id).map(this::initGroupCollections).orElse(null);
    }

    @Override
    public AppGroup save(AppGroup appUser) {
        return groupRepo.save(appUser);
    }

    @Override
    public AppGroup update(AppGroup appUser) {
        return groupRepo.save(appUser);
    }

    @Override
    public List<AppGroup> saveAll(List<AppGroup> appUsers) {
        return groupRepo.saveAll(appUsers);
    }

    @Override
    public AppGroup saveAndFlush(AppGroup appUser) {
        return groupRepo.saveAndFlush(appUser);
    }

    @Override
    public List<AppGroup> saveAllAndFlush(List<AppGroup> appUsers) {
        return groupRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(AppGroup group) {
        if (group == null || group.getId() == null) {
            return;
        }
        // APP_USER.GROUP_ID FK: clear references or delete fails with constraint violation (HTTP 500).
        for (AppUser u : userRepo.findByGroup_Id(group.getId())) {
            u.setGroup(null);
        }
        userRepo.flush();
        groupRepo.delete(group);
    }

    @Override
    public AppGroup loadByName(String username) {
        AppGroup g = groupRepo.findGroupByName(username);
        return g == null ? null : initGroupCollections(g);
    }

    @Override
    public List<AppGroup> findByCriteria(String description, String name) {
        List<AppGroup> list = groupRepo.findByDescriptionContainingOrNameContaining(description, name);
        list.forEach(this::initGroupCollections);
        return list;
    }

    @Override
    public void assignMenusToGroup(Long groupId, List<Long> menuIds) {
        AppGroup group = groupRepo.findById(groupId).orElseThrow();
        group.getMenus().clear();
        List<Long> safeMenuIds = menuIds == null ? Collections.emptyList() : menuIds;
        for (Long id : new LinkedHashSet<>(safeMenuIds)) {
            if (id == null) {
                continue;
            }
            menuRepo.findById(id).ifPresent(group.getMenus()::add);
        }
        groupRepo.saveAndFlush(group);
    }

    private AppGroup initGroupCollections(AppGroup group) {
        group.getMenus().size();
        return group;
    }

}
