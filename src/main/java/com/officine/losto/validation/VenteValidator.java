package com.officine.losto.validation;


import java.util.Optional;

import org.springframework.stereotype.Component;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.commons.validation.ValidationSupport;
import com.officine.losto.backend.commons.validation.Validator;
import com.officine.losto.backend.entity.Vente;
import com.officine.losto.params.constant.ConstMessagesEN;

@Component
public class VenteValidator extends ValidationSupport implements Validator<Vente> {
	@Override
	public Optional<ValidationError> validate(Vente vente) {
		if (!isInteger(vente.getMontantPaye().toString())

				|| (isNullOrEmptyString(vente.getClient())
				|| (vente.getMontantTotal().subtract(vente.getMontantPaye())).intValue() > 0)
		)
		{
			return Optional
					.of(new ValidationError(ConstMessagesEN.ValidationMessages.REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA));
		}
		return Optional.empty();
	}
}
