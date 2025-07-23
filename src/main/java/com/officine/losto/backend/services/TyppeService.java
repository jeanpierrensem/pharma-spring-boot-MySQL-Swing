package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Typpe;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  TyppeService permits TyppeServiceImpl {
	   @TransactionalWrite
	   Typpe  save(Typpe Type) ;
	   @TransactionalWrite
	   Typpe  saveAndFlush(Typpe Type) ;
	   @TransactionalWrite
	   void remove(Typpe Type);
	   @TransactionalReadOnly
	   Typpe loadTypeByName (String typeName);
	   @TransactionalReadOnly
	   Typpe loadTypeById (Long id);
	   @TransactionalReadOnly
	   List<Typpe> listTypes();
	   @TransactionalReadOnly
	   List<Typpe> findTypeByCriteria(String typeDescription, String typeName); 
}
