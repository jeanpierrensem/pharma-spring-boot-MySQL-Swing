package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Seuil;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  SeuilService permits SeuilServiceImpl {
	   @TransactionalWrite
	   Seuil  save(Seuil seuil) ;
	   @TransactionalWrite
	   Seuil  saveAndFlush(Seuil seuil) ;
	   @TransactionalWrite
	   void remove(Seuil seuil);
	   @TransactionalReadOnly
	   Seuil loadSeuilByCode (String seuilCode);
	   @TransactionalReadOnly
	   List<Seuil> listSeuils();
	   @TransactionalReadOnly
	   List<Seuil> findSeuilByCriteria(String seuilCode); 
	   

		
	
}
