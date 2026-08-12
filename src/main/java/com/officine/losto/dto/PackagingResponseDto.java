package com.officine.losto.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackagingResponseDto {
    private Long id;
    private String code;
    private String description;
}
