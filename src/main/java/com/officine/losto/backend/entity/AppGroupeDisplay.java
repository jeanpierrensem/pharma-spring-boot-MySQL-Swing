package com.officine.losto.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class AppGroupeDisplay {

	private Long id;
	private String groupeCode;
	private String groupeName;
	private String groupeDescription;

}
