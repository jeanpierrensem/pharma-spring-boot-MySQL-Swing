package com.officine.losto.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.officine.losto.backend.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Article findByArticleName(String articleName);
	Article findByArticleCodeBarre(String codebarre); 

	List<Article> findByArticleNameContainingOrArticleCodeBarreContaining(String articleName, String articleCodeBarre);

	
	
}
