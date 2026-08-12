package com.officine.losto.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDetailsResponseDto {
    private Long id;
    private EntityRefDto orders;
    private EntityRefDto product;
    private int quantity;
    private int unitPrice;
    private int discount;
    private int totalPrice;
}
