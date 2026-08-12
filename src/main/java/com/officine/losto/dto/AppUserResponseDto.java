package com.officine.losto.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponseDto {
    private Long id;
    private String login;
    private String phoneNumber;
    private String name;
    private String email;
    private EntityRefDto group;
    /**
     * Relative to API root (e.g. {@code users/12/photo}); join with base URL {@code http://host:port/api/}.
     */
    private String profilePhotoUrl;
}
