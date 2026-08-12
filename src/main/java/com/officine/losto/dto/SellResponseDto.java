package com.officine.losto.dto;

import lombok.*;

import java.math.*;
import java.time.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellResponseDto {
    private Long id;
    private String number;
    private LocalDate date;
    private String seller;
    private String client;
    private String sellType;
    private String paymentMode;
    private BigDecimal totalPrice;
    private BigDecimal amountReceived;
    private BigDecimal changeGiven;
    private String remark;
    /**
     * Périmètre site.
     */
    private EntityRefDto site;
    /**
     * Caisse / PDV.
     */
    private EntityRefDto pointDeVente;
    /**
     * Utilisateur (vendeur) ayant enregistré.
     */
    private EntityRefDto effectueePar;
    private List<SellDetailsResponseDto> lines;
}
