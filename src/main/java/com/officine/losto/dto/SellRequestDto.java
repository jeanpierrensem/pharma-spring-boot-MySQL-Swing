package com.officine.losto.dto;

import com.officine.losto.validation.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.*;
import java.time.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellRequestDto {
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.OnCreate.class)
    @Size(max = 100)
    private String number;

    @NotNull(groups = ValidationGroups.OnCreate.class)
    private LocalDate date;

    @Size(max = 255)
    private String seller;

    @Size(max = 255)
    private String client;

    @Size(max = 50)
    private String sellType;

    @Size(max = 50)
    private String paymentMode;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal totalPrice;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal amountReceived;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal changeGiven;

    @Size(max = 2000)
    private String remark;

    /**
     * Périmètre organisation (site) — col. {@code SITE_ID}.
     */
    @NotNull(groups = ValidationGroups.OnCreate.class)
    private Long siteId;

    /**
     * Point de caisse — col. {@code POINT_DE_VENTE_ID}.
     */
    @NotNull(groups = ValidationGroups.OnCreate.class)
    private Long pointDeVenteId;

    /**
     * Vendeur / utilisateur qui enregistre la vente — col. {@code EFFECTUEE_PAR_USER_ID}.
     */
    @NotNull(groups = ValidationGroups.OnCreate.class)
    private Long effectueeParUserId;

    @Valid
    @NotEmpty(groups = ValidationGroups.OnCreate.class)
    @NotNull(groups = ValidationGroups.OnUpdate.class)
    private List<SellLineRequestDto> lines;
}
