package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Fournisseur;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  FournisseurService permits  FournisseurServiceImpl {
	   @TransactionalWrite
	   Fournisseur  save(Fournisseur fournisseur) ;
	   @TransactionalWrite
	   Fournisseur  saveAndFlush(Fournisseur fournisseur) ;
	   @TransactionalWrite
	   void remove(Fournisseur fournisseur);
	   @TransactionalReadOnly
	   Fournisseur loadFournisseurByName (String fournisseur);
	   @TransactionalReadOnly
	   Fournisseur loadFournisseurById (long id);
	   @TransactionalReadOnly
	   List<Fournisseur> listFournisseurs();
	   @TransactionalReadOnly
	   List<Fournisseur> findFournisseurByCriteria(String FournisseurName, String FournisseurAdresse); 
}
