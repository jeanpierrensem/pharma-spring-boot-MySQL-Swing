package com.officine.losto.dto;

import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppGroupResponseDto {
    private Long id;
    private String name;
    private String description;
    private boolean selected;
    /**
     * Menus associés à ce groupe.
     */
    private List<EntityRefDto> menus;
}
