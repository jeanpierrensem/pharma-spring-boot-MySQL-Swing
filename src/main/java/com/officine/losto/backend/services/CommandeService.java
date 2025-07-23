package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Commande;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  CommandeService permits CommandeServiceImpl {
	   @TransactionalWrite
	   Commande  save(Commande commande) ;
	   @TransactionalWrite
	   Commande  saveAndFlush(Commande commande) ;
	   @TransactionalWrite
	   void remove(Commande commande);
	   @TransactionalReadOnly
	   Commande loadCommandeById (Long id);
	   @TransactionalReadOnly
	   List<Commande> listCommandes();
	   @TransactionalReadOnly
	   List<Commande> findTypeByCriteria(String commandeNumero,  String commandeDate); 
	   long getCommandCount();   
	   @TransactionalReadOnly
	   Commande  loadCommandeByNumeroCommande(String commandeNumero); 
	   
}
