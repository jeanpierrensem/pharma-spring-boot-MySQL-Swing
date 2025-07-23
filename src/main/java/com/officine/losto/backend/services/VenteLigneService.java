package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Vente;
import com.officine.losto.backend.entity.VenteLigne;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  VenteLigneService permits VenteLigneServiceImpl  {
	   @TransactionalWrite
	   VenteLigne  save(VenteLigne venteLigne) ;
	   
	   @TransactionalWrite
	    void saveAll(List<VenteLigne> venteLignes) ;
	   
	   @TransactionalWrite
	    List<VenteLigne> saveAllAndFlush(List<VenteLigne> venteLignes) ;
	   
	   @TransactionalWrite
	   VenteLigne  saveAndFlush(VenteLigne venteLigne) ;
	   
	   @TransactionalWrite
	   void remove(VenteLigne venteLigne);
	   
	   //@TransactionalWrite
	   //void deleteByLigneVente(LigneVente ligneVente); 
	   //@TransactionalReadOnly
	   //VenteLigne loadVenteLigneByName (String VenteLigneName);
	   @TransactionalReadOnly
	   VenteLigne loadVenteLigneById (Long id);
	   
	   @TransactionalReadOnly
	   List<VenteLigne> listVenteLignes();
	   
	   //@TransactionalReadOnly
	   List<VenteLigne> loadByVente(Vente  vente); 	   
	   
	   @TransactionalReadOnly
	   List<VenteLigne> loadByVenteLigneId(long id); 
}
