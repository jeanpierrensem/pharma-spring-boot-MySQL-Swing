package com.officine.losto.backend.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.officine.losto.backend.entity.Article;
import com.officine.losto.backend.entity.Vente;
import com.officine.losto.backend.entity.VenteLigne;
import com.officine.losto.backend.repository.ArticleRepository;

@Service
public non-sealed class ArticleServiceImpl implements ArticleService {
	private ArticleRepository articleRepository;

	public ArticleServiceImpl(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	@Override
	public Article save(Article article) {
		return articleRepository.save(article);
	}

	@Override
	public Article loadArticleByName(String ArticleName) {
		return articleRepository.findByArticleName(ArticleName);
	}

	@Override
	public List<Article> listArticles() {
		return articleRepository.findAll();
	}

	@Override
	public void remove(Article groupe) {
		articleRepository.delete(groupe);

	}

	@Override
	public Article saveAndFlush(Article Article) {

		return articleRepository.saveAndFlush(Article);
	}

	@Override
	public List<Article> findArticleByCriteria(String articleName, String articleCodeBarre) {

		return articleRepository.findByArticleNameContainingOrArticleCodeBarreContaining(articleName, articleCodeBarre);
	}

	@Override
	public Article findArticleById(long articleId) {

		return articleRepository.findById(articleId).get();
	}

	@Override
	public Article loadByArticleCodeBarre(String codebarre) {

		return articleRepository.findByArticleCodeBarre(codebarre);
	}

	@Override
	public void incrementProductWarehouseQuantity(Vente vente) {
		List<Article> articlesToUpdate = new ArrayList<Article>();
		for (VenteLigne venteLigne : vente.getLignes()) {

			Article article = venteLigne.getArticle();
			article.setArticleQuantite_stock(article.getArticleQuantite_stock() +
					venteLigne.getQuantite()
			);
			articlesToUpdate.add(article);
		}
		articleRepository.saveAllAndFlush(articlesToUpdate);
	}
	
	@Override
	public void decrementProductWarehouseQuantity(Vente vente) {
		List<Article> articlesToUpdate = new ArrayList<Article>();
		for (VenteLigne venteLigne : vente.getLignes()) {

			Article article = venteLigne.getArticle();
			article.setArticleQuantite_stock(article.getArticleQuantite_stock() - 
					venteLigne.getQuantite()
			);

			articlesToUpdate.add(article);
		}
		articleRepository.saveAllAndFlush(articlesToUpdate);

	}

}
