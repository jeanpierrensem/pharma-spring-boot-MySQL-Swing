package com.officine.losto.services.security;

import java.util.List;

import com.officine.losto.backend.entity.AppUser;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

/**
 * The {@code AccountService} class is sealed interface to manage user and roles/authorities
 * in our application. Methods to implement are
 * <p> @addNewUser : to add new user
 * <p> @addNewRole  : to add new role
 * <p> @addRoleToUser : to add role to user
 * <p> @loadUserByName : to retrieve user by name
 * <p> @listUsers : to list all users
 * <p>
 * @author  JP NSEM
 * @since   1.0
 */
public sealed interface AccountService permits AccountServiceImpl {

   @TransactionalReadOnly
   List<AppUser> listUsers();
   
   @TransactionalWrite
   AppUser  save(AppUser appUser) ;
   
   @TransactionalReadOnly
   AppUser loadUserByName (String username);
   
   @TransactionalWrite
   List<AppUser>   saveAll(List<AppUser> appUsers) ;
   
   @TransactionalWrite
   AppUser  saveAndFlush(AppUser appUser) ;
   
   @TransactionalWrite
   List<AppUser>   saveAllAndFlush(List<AppUser> appUsers) ;
   
   @TransactionalWrite
   void remove(AppUser appUser);
   @TransactionalWrite
   
   @TransactionalReadOnly
   AppUser Authenticate(String username, String password); 
     
   @TransactionalReadOnly
   List<AppUser> findUserByCriteria(String matricule, String nom, String prenom, String groupeName, String login); 
  
   
   

}
