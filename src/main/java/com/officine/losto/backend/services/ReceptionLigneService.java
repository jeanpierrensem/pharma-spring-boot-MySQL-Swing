package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.ReceptionLigne;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  ReceptionLigneService permits ReceptionLigneServiceImpl  {
	   @TransactionalWrite
	   ReceptionLigne  save(ReceptionLigne receptionLigne) ;
	   @TransactionalWrite
	    void saveAll(List<ReceptionLigne> receptionLigne) ;
	   @TransactionalWrite
	    List<ReceptionLigne> saveAllAndFlush(List<ReceptionLigne> receptionLignes) ;
	   @TransactionalWrite
	   ReceptionLigne  saveAndFlush(ReceptionLigne receptionLigne) ;
	   @TransactionalWrite
	   void remove(ReceptionLigne receptionLigne);
	   @TransactionalReadOnly
	   ReceptionLigne loadReceptionLigneById (Long id);
	   @TransactionalReadOnly
	   List<ReceptionLigne> listReceptionLignes();
	   //@TransactionalReadOnly
	   List<ReceptionLigne> findReceptionsByCommandeLigne(CommandeLigne commandeLigne); 
	   /*@TransactionalReadOnly
	   List<ReceptionLigne> loadByReceptionLigneId(long id); */
}
