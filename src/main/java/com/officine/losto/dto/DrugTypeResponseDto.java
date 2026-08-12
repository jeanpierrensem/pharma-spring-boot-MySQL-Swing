package com.officine.losto.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrugTypeResponseDto {
    private Long id;
    private String code;
    private String description;
}
