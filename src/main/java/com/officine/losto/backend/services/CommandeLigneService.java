package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Commande;
import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.ReceptionLigne;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  CommandeLigneService permits CommandeLigneServiceImpl  {
	   @TransactionalWrite
	   CommandeLigne  save(CommandeLigne commandeLigne) ;
	   @TransactionalWrite
	    void saveAll(List<CommandeLigne> commandeLignes) ;
	   @TransactionalWrite
	    List<CommandeLigne> saveAllAndFlush(List<CommandeLigne> commandeLignes) ;
	   @TransactionalWrite
	   CommandeLigne  saveAndFlush(CommandeLigne commandeLigne) ;
	   @TransactionalWrite
	   void remove(CommandeLigne commandeLigne);
	   @TransactionalWrite
	   void deleteByCommandeLigneCommande(Commande commande); 
	   //@TransactionalReadOnly
	   //CommandeLigne loadCommandeLigneByName (String commandeLigneName);
	   @TransactionalReadOnly
	   CommandeLigne loadCommandeLigneById (Long id);
	   @TransactionalReadOnly
	   List<CommandeLigne> listCommandeLignes();
	   //@TransactionalReadOnly
	   List<CommandeLigne> findByCommande(Commande commande); 
	   
	   @TransactionalReadOnly
	   List<ReceptionLigne> loadByCommandeLigneId(long id); 
}
