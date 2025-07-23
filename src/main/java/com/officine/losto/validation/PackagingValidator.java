package com.officine.losto.validation;

import java.util.Optional;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.commons.validation.ValidationSupport;
import com.officine.losto.backend.commons.validation.Validator;
import com.officine.losto.backend.entity.AppRole;
import com.officine.losto.backend.entity.Packaging;
import com.officine.losto.params.constant.ConstMessagesEN;

@Component
public class PackagingValidator extends ValidationSupport implements Validator<Packaging> {

	@Override
	public Optional<ValidationError> validate(Packaging packaging) {
		if (isNullOrEmptyString(packaging.getPackagingName()))

		{
			return Optional
					.of(new ValidationError(ConstMessagesEN.ValidationMessages.REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA));
		}
		return Optional.empty();
	}

}
