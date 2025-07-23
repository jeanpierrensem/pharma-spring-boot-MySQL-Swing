package com.officine.losto.validation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.commons.validation.ValidationSupport;
import com.officine.losto.backend.commons.validation.Validator;
import com.officine.losto.backend.entity.Commande;
import com.officine.losto.params.constant.ConstMessagesEN;

@Component
public class CommandeValidator extends ValidationSupport implements Validator<Commande> {

	@Override
	public Optional<ValidationError> validate(Commande commande) {
		if (isNullOrEmptyString("" + commande.getId())

				|| (!isValidDate(commande.getCommandeDate()))) {
			return Optional
					.of(new ValidationError(ConstMessagesEN.ValidationMessages.REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA));
		}
		return Optional.empty();
	}

}
