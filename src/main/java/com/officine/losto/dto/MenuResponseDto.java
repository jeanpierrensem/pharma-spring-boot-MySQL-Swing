package com.officine.losto.dto;

import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDto {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private String pathCode;
    private Long parentId;
    private Integer treeLevel;
    private Integer sortOrder;
    /**
     * Groupes auxquels ce menu est lié (vide si aucun).
     */
    private List<Long> groupIds;
}
