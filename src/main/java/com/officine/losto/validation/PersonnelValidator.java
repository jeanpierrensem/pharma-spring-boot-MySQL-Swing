package com.officine.losto.validation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.commons.validation.ValidationSupport;
import com.officine.losto.backend.commons.validation.Validator;
import com.officine.losto.backend.entity.AppUser;
import com.officine.losto.params.constant.ConstMessagesEN;

@Component
public class PersonnelValidator extends ValidationSupport implements Validator<AppUser> {

	@Override
	public Optional<ValidationError> validate(AppUser  appUser ) {
		if (isNullOrEmptyString( appUser.getMatricule() )

				|| (isNullOrEmptyString(appUser.getNom())
				|| (isNullOrEmptyString(appUser.getAppGroupe().getGroupeName())
				|| (isNullOrEmptyString(appUser.getUsername())
				|| (isNullOrEmptyString(appUser.getPassword())
		)))))
		{
			return Optional
					.of(new ValidationError(ConstMessagesEN.ValidationMessages.REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA));
		}
		return Optional.empty();
	}

}
