package com.officine.losto.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

	@NotBlank(message = "Le login est obligatoire")
	private String username;

	@NotBlank(message = "Le mot de passe est obligatoire")
	private String password;

	private boolean rememberMe;
}
