package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 100)
    private String code;

    @Size(max = 1000)
    private String description;
}
