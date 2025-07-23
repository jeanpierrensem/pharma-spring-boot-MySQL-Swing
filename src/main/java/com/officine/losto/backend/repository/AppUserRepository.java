package com.officine.losto.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.officine.losto.backend.entity.AppUser;

public interface AppUserRepository  extends JpaRepository<AppUser, Long>{
	AppUser findByUsername(String username);
	
	@Query("SELECT u  FROM AppUser u where u.username = ?1 and u.password = ?2")
	AppUser findByUsernameAndPassword(String username, String password);	
    List<AppUser> findByMatriculeContainingOrNomContainingOrPrenomContainingOrUsernameContaining(String matricule, String nom, String prenom, String username); 
		
    
    

}
