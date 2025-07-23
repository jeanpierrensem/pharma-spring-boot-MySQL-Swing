package com.officine.losto.validation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.commons.validation.ValidationSupport;
import com.officine.losto.backend.commons.validation.Validator;
import com.officine.losto.backend.entity.ReceptionLigne;
import com.officine.losto.params.constant.ConstMessagesEN;

@Component
public class ReceptionLigneValidator extends ValidationSupport implements Validator<ReceptionLigne> {

	@Override
	public Optional<ValidationError> validate(ReceptionLigne receptionLigne) {
		if (isNullOrEmptyString("" + receptionLigne.getQuantiteRecue())

		) {
			return Optional
					.of(new ValidationError(ConstMessagesEN.ValidationMessages.REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA));
		}
		return Optional.empty();
	}

}
