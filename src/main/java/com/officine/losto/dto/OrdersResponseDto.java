package com.officine.losto.dto;

import lombok.*;

import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponseDto {
    private Long id;
    private String number;
    private LocalDate orderDate;
    private String description;
    private String statut;
    private EntityRefDto provider;
    private EntityRefDto user;
}
