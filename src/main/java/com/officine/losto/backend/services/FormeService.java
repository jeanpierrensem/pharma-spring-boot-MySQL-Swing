package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Forme;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  FormeService permits FormeServiceImpl {
	   @TransactionalWrite
	   Forme  save(Forme forme) ;
	   @TransactionalWrite
	   Forme  saveAndFlush(Forme forme) ;
	   @TransactionalWrite
	   void remove(Forme forme);
	   @TransactionalReadOnly
	   Forme loadFormeByName (String formeName);
	   @TransactionalReadOnly
	   Forme loadFormeById (Long id);
	   @TransactionalReadOnly
	   List<Forme> listFormes();
	   @TransactionalReadOnly
	   List<Forme> findFormeByCriteria(String formeDescription, String formeName); 
}
