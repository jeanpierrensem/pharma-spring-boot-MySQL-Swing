package com.officine.losto.dto;

import lombok.*;

import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDetailsResponseDto {
    private Long id;
    private int receivedQuantity;
    private int missingQuantity;
    private LocalDateTime date;
    private String observation;
    private EntityRefDto user;
    private EntityRefDto ordersDetails;
}
