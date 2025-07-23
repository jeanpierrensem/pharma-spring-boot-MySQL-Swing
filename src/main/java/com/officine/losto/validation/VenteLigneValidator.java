package com.officine.losto.validation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.commons.validation.ValidationSupport;
import com.officine.losto.backend.commons.validation.Validator;
import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.VenteLigne;
import com.officine.losto.params.constant.ConstMessagesEN;

@Component
public class VenteLigneValidator extends ValidationSupport implements Validator<VenteLigne> {

	@Override
	public Optional<ValidationError> validate(VenteLigne ligneVente) {

		/*
		 * System.out.println(
		 * "commandeLigne.getCommandeLigneArticle().getArticleName()=" +
		 * commandeLigne.getCommandeLigneArticle().getArticleName());
		 * 
		 * System.out.println("commandeLigne.getCommandeLigneReferenceArticle() =" +
		 * commandeLigne.getCommandeLigneReferenceArticle());
		 * System.out.println("commandeLigne.getCommandeLigneQuantite() =" +
		 * commandeLigne.getCommandeLigneQuantite());
		 * System.out.println("commandeLigne.getCommandeLignePrixUnitaireHT() =" +
		 * commandeLigne.getCommandeLignePrixUnitaireHT());
		 * System.out.println("commandeLigne.getCommandeLigneRemise() =" +
		 * commandeLigne.getCommandeLigneRemise());
		 * System.out.println("commandeLigne.getCommandeLignePrixTotalHT()=" +
		 * commandeLigne.getCommandeLignePrixTotalHT());
		 */
		if (isNullOrEmptyString("" + ligneVente.getId())
		/*
		 * || (isNullOrEmptyString(commandeLigne.getCommandeLigneReferenceArticle()) ||
		 * (!isInteger(""+commandeLigne.getCommandeLigneQuantite()) ||
		 * (!isInteger(""+commandeLigne.getCommandeLignePrixUnitaireHT()) ||
		 * (!isInteger(""+commandeLigne.getCommandeLigneRemise()) ||
		 * (!isInteger(""+commandeLigne.getCommandeLignePrixTotalHT())
		 * 
		 * 
		 * )))))
		 */)

		{
			return Optional
					.of(new ValidationError(ConstMessagesEN.ValidationMessages.REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA));
		}
		return Optional.empty();
	}

}