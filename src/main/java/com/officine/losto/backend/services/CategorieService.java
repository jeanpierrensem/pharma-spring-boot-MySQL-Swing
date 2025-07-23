package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Categorie;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  CategorieService permits CategorieServiceImpl {
	   @TransactionalWrite
	   Categorie  save(Categorie categorie) ;
	   @TransactionalWrite
	   Categorie  saveAndFlush(Categorie categorie) ;
	   @TransactionalWrite
	   void remove(Categorie categorie);
	   @TransactionalReadOnly
	   Categorie loadCategorieByName (String categorieName);
	   @TransactionalReadOnly
	   Categorie loadCategorieById (Long id);
	   @TransactionalReadOnly
	   List<Categorie> listCategories();
	   @TransactionalReadOnly
	   List<Categorie> findCategorieByCriteria(String categorieDescription, String categorieName); 
}
