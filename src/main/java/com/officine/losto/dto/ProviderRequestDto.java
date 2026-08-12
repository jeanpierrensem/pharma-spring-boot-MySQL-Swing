package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 100)
    private String code;

    @Size(max = 500)
    private String designation;

    @Size(max = 500)
    private String address;

    @Size(max = 50)
    private String phoneNumber;

    @Email
    @Size(max = 255)
    private String email;
}
