package com.officine.losto.validation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.commons.validation.ValidationSupport;
import com.officine.losto.backend.commons.validation.Validator;
import com.officine.losto.backend.entity.Article;
import com.officine.losto.params.constant.ConstMessagesEN;


@Component
public class ArticleValidator extends ValidationSupport implements Validator<Article> {

	@Override
	public Optional<ValidationError> validate(Article article ) {
		if (isNullOrEmptyString(article.getArticleCodeBarre())

			|| (isNullOrEmptyString(article.getArticleForme().getFormeName())
		    || (isNullOrEmptyString(article.getArticlePackaging().getPackagingName())
			|| (isNullOrEmptyString(article.getArticleTyppe().getTyppeName())
			|| (isNullOrEmptyString(article.getArticleCategorie().getCategorieName())
			|| (isNullOrEmptyString(article.getArticlePackaging().getPackagingName())
			|| (isNullOrEmptyString(article.getArticleDosage().toString())
			|| (isNullOrEmptyString(article.getArticleName())
					
		))))))))
			
		{
			return Optional
					.of(new ValidationError(ConstMessagesEN.ValidationMessages.REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA));
		}
		return Optional.empty();
	}

}
