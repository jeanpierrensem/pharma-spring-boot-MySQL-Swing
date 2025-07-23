package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Vente;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  VenteService permits VenteServiceImpl {
	   @TransactionalWrite
	   Vente  save(Vente vente) ;
	   @TransactionalWrite
	   Vente  saveAndFlush(Vente vente) ;
	   @TransactionalWrite
	   void remove(Vente vente);
	   @TransactionalReadOnly
	   Vente loadVenteById (Long id);
	   @TransactionalReadOnly
	   List<Vente> listVentes();
	   @TransactionalReadOnly
	   List<Vente> findTypeByCriteria(String venteNumero,  String VvnteDate); 
	   long getVenteCount();   
	   @TransactionalReadOnly
	   Vente  loadVenteByNumero(String venteNumero); 
	   
}
