package com.officine.losto.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponseDto {
    private Long id;
    private String code;
    private String designation;
    private String address;
    private String phoneNumber;
    private String email;
}
