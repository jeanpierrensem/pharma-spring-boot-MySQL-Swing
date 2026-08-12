package com.officine.losto.dto.auth;

import com.officine.losto.dto.EntityRefDto;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserResponseDto {

	private Long id;
	private String login;
	private String name;
	private String email;
	private boolean enabled;
	private Instant createdAt;
	private EntityRefDto group;
	private List<String> roles;
	/** pathCode habilités explicites du groupe (catalogue menu — affichage IHM). */
	private List<String> menuPathCodes;
	/** pathCode effectifs pour les appels API (explicites + droits implicites en lecture). */
	private List<String> apiPathCodes;
}
