package com.officine.losto.services.security;

import java.util.List;

import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.AppUser;
import com.officine.losto.backend.repository.AppUserRepository;

@Service
public non-sealed class AccountServiceImpl implements AccountService {

	private AppUserRepository appUserRepository;
	public AccountServiceImpl(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}
	@Override
	public List<AppUser> listUsers() {
		return appUserRepository.findAll();
	}
	@Override
	public AppUser save(AppUser appUser) {
		return appUserRepository.save(appUser);
	}
	@Override
	public AppUser loadUserByName(String username) {
		return appUserRepository.findByUsername(username);
	}
	@Override
	public List<AppUser> saveAll(List<AppUser> appUsers) {
		return appUserRepository.saveAll(appUsers);
	}
	@Override
	public AppUser saveAndFlush(AppUser appUser) {

		return appUserRepository.saveAndFlush(appUser);
	}
	@Override
	public List<AppUser> saveAllAndFlush(List<AppUser> appUsers) {
		return appUserRepository.saveAllAndFlush(appUsers);
	}
	@Override
	public void remove(AppUser appUser) {
		appUserRepository.delete(appUser);
	}
	@Override
	public List<AppUser> findUserByCriteria(String matricule, String nom, String prenom, String groupeName,
			String login) {

		return appUserRepository.findByMatriculeContainingOrNomContainingOrPrenomContainingOrUsernameContaining(
				matricule, nom, prenom, login);
	}

	@Override
	public AppUser Authenticate(String username, String password) {

		return appUserRepository.findByUsernameAndPassword(username, password);
	}

	
	
	
}
