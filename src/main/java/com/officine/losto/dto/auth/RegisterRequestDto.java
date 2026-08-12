package com.officine.losto.dto.auth;

import com.officine.losto.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

	@NotBlank(message = "Le login est obligatoire")
	@Size(min = 3, max = 64, message = "Le login doit contenir entre 3 et 64 caractères")
	private String username;

	@NotBlank(message = "Le nom est obligatoire")
	@Size(max = 128, message = "Le nom ne peut pas dépasser 128 caractères")
	private String name;

	@NotBlank(message = "L'email est obligatoire")
	@Email(message = "Format d'email invalide")
	private String email;

	@NotBlank(message = "Le mot de passe est obligatoire")
	@StrongPassword
	private String password;
}
