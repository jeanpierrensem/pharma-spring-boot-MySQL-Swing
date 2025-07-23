package com.officine.losto.services.security;

import java.util.List;

import com.officine.losto.backend.entity.AppGroupe;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;


public sealed interface  GroupeService permits GroupeServiceImpl {
	   @TransactionalWrite
	   AppGroupe  save(AppGroupe appGroupe) ;
	   
	   @TransactionalWrite
	   List<AppGroupe>   saveAll(List<AppGroupe> appGroupes) ;
	   
	   @TransactionalWrite
	   AppGroupe  saveAndFlush(AppGroupe appGroupe) ;
	   
	   @TransactionalWrite
	   List<AppGroupe>   saveAllAndFlush(List<AppGroupe> appGroupes) ;
	   
	   @TransactionalWrite
	   void remove(AppGroupe appGroupe);
	   @TransactionalWrite
	   
	   void removeByGroupeCode(String groupeCode);
	   
	   @TransactionalReadOnly
	   List<AppGroupe> listGroupes();
	   
	   @TransactionalReadOnly
	   List<AppGroupe> findGroupeByCriteria(String groupeDescription, String groupeName); 
	   
	   @TransactionalReadOnly
	   AppGroupe findGroupeById(long  id); 
	   
	   @TransactionalReadOnly
	   List<AppGroupe> listAllGroupes(String groupeCode); 
	   
	 
	   
	   @TransactionalWrite
	   int update( boolean isAfficher, 
   		    boolean isEnregistrer, 
   		    boolean isModifier, 
   		    boolean isSupprimer, 
   		    boolean isImprimer, 
   		    String groupeCode, 
   		    Long menueId    
   		); 
	   
}
