package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Packaging;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  PackagingService permits PackagingServiceImpl {
	   @TransactionalWrite
	   Packaging  save(Packaging packaging) ;
	   @TransactionalWrite
	   Packaging  saveAndFlush(Packaging packaging) ;
	   @TransactionalWrite
	   void remove(Packaging packaging);
	   @TransactionalReadOnly
	   Packaging loadPackagingByName (String packagingName);
	   @TransactionalReadOnly
	   Packaging loadPackagingById (Long id);
	   @TransactionalReadOnly
	   List<Packaging> listPackagings();
	   @TransactionalReadOnly
	   List<Packaging> findPackagingByCriteria(String PackagingDescription, String PackagingName); 
}
