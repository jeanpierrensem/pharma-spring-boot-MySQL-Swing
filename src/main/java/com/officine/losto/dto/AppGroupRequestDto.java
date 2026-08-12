package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppGroupRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    private boolean selected;

    /**
     * Liste des menus à associer au groupe (remplacement complet si non null).
     */
    private List<Long> menuIds;
}
