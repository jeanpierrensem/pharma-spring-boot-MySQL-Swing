package com.officine.losto.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormResponseDto {
    private Long id;
    private String code;
    private String description;
}
