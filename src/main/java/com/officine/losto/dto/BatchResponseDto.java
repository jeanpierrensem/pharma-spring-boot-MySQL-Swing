package com.officine.losto.dto;

import lombok.*;

import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchResponseDto {
    private Long id;
    private String number;
    private LocalDate expiredDate;
    private int quantity;
    private EntityRefDto provider;
}
