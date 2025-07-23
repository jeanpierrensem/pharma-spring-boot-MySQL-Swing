package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Lot;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  LotService permits LotServiceImpl {
	   @TransactionalWrite
	   Lot  save(Lot lot) ;
	   @TransactionalWrite
	   Lot  saveAndFlush(Lot lot) ;
	   @TransactionalWrite
	   void remove(Lot lot);
	   @TransactionalReadOnly
	   Lot loadLotByName (String lotName);
	   @TransactionalReadOnly
	   Lot loadLotById (Long id);
	   @TransactionalReadOnly
	   List<Lot> listLots();
	   @TransactionalReadOnly
	   List<Lot> findLotByCriteria(String numeroLot); 
}
