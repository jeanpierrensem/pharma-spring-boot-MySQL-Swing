package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 100)
    private String login;

    /**
     * Plain password for create/update; omit or null to leave unchanged on update.
     */
    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 255)
    private String password;

    @Size(max = 50)
    private String phoneNumber;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 255)
    private String name;

    @Email
    @Size(max = 255)
    private String email;

    @Positive
    private Long groupId;
}
