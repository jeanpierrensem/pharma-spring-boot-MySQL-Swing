package com.officine.losto.service;

import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserPhotoStorageService userPhotoStorageService;

    public UserServiceImpl(UserRepo userRepo, UserPhotoStorageService userPhotoStorageService) {
        this.userRepo = userRepo;
        this.userPhotoStorageService = userPhotoStorageService;
    }

    @Override
    public List<AppUser> getAll() {
        return userRepo.findAll();
    }

    @Override
    public AppUser loadById(long id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public AppUser save(AppUser appUser) {
        return userRepo.save(appUser);
    }

    @Override
    public AppUser update(AppUser appUser) {
        return userRepo.save(appUser);
    }

    @Override
    public List<AppUser> saveAll(List<AppUser> appUsers) {
        return userRepo.saveAll(appUsers);
    }

    @Override
    public AppUser saveAndFlush(AppUser appUser) {
        return userRepo.saveAndFlush(appUser);
    }

    @Override
    public List<AppUser> saveAllAndFlush(List<AppUser> appUsers) {
        return userRepo.saveAllAndFlush(appUsers);
    }

    @Override
    public void remove(AppUser appUser) {
        userPhotoStorageService.deleteStoredFileIfPresent(appUser);
        userRepo.delete(appUser);
    }

    @Override
    public AppUser loadByName(String username) {
        return userRepo.findByName(username);
    }

    /**
     * this method filters the table containing the user data and return the
     * list of user that satisfied the filters.
     *
     * @param name
     * @param login
     * @param email
     * @return
     */
    @Override
    public List<AppUser> findByCriteria(String name, String login, String email) {
        return userRepo.findByNameContainingOrLoginContainingOrEmailContaining(name, login, email);
    }

    @Override
    public List<AppUser> findAllByIdsInOrder(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        List<AppUser> batch = userRepo.findAllById(ids);
        Map<Long, AppUser> byId = batch.stream().collect(Collectors.toMap(AppUser::getId, Function.identity()));
        List<AppUser> ordered = new ArrayList<>();
        for (Long id : ids) {
            AppUser u = byId.get(id);
            if (u != null) {
                ordered.add(u);
            }
        }
        return ordered;
    }
}
