package com.officine.losto.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuTreeNodeDto {
	private Long id;
	private String name;
	private String description;
	private String pathCode;
	private Integer treeLevel;
	private Integer sortOrder;
	private List<MenuTreeNodeDto> children;
}
