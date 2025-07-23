package com.officine.losto.backend.services;

import java.util.List;

import com.officine.losto.backend.entity.Article;
import com.officine.losto.backend.entity.Vente;
import com.officine.losto.util.annotation.TransactionalReadOnly;
import com.officine.losto.util.annotation.TransactionalWrite;

public sealed interface  ArticleService permits ArticleServiceImpl {
	   @TransactionalWrite
	   Article  save(Article article) ;
	   @TransactionalWrite
	   Article  saveAndFlush(Article article) ;
	   @TransactionalWrite
	   void remove(Article article);
	   @TransactionalReadOnly
	   Article loadArticleByName (String articleName);
	   @TransactionalReadOnly
	   Article findArticleById (long articleId);
	   @TransactionalReadOnly
	   List<Article> listArticles();
	   @TransactionalReadOnly
	   List<Article> findArticleByCriteria(String ArticleName , String codeBarre ); 
	   @TransactionalReadOnly
	   Article loadByArticleCodeBarre(String codebarre); 
	   @TransactionalWrite
	   void incrementProductWarehouseQuantity(Vente vente); 
	   @TransactionalWrite
	   void decrementProductWarehouseQuantity(Vente vente); 
}
