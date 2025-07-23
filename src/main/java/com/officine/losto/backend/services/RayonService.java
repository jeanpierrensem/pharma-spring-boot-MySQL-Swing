package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Rayon;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  RayonService permits RayonServiceImpl {
	   @TransactionalWrite
	   Rayon  save(Rayon rayon) ;
	   @TransactionalWrite
	   Rayon  saveAndFlush(Rayon rayon) ;
	   @TransactionalWrite
	   void remove(Rayon rayon);
	   @TransactionalReadOnly
	   Rayon loadRayonByName (String rayonName);
	   @TransactionalReadOnly
	   Rayon loadRayonById (Long id);
	   @TransactionalReadOnly
	   List<Rayon> listRayons();
	   @TransactionalReadOnly
	   List<Rayon> findRayonByCriteria(String rayonDescription, String rayonName); 
}
