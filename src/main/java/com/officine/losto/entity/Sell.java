package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.math.*;
import java.time.*;
import java.util.*;

/**
 * Vente (table H2 <code>SELL</code>). Colonne date : en base <code>DATE</code> (type DATE).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "SELL")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class Sell extends AbstractEntity {

    @Column(name = "CHANGE_GIVEN", precision = 38, scale = 2)
    private BigDecimal changeGiven;

    @Column(name = "AMOUNT_RECEIVED", precision = 38, scale = 2)
    private BigDecimal amountReceived;

    @Column(name = "CLIENT")
    private String client;

    @Column(name = "DATE", nullable = false)
    private LocalDate dateVente;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "PAYMENT_MODE")
    private String paymentMode;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "SELL_TYPE")
    private String sellType;

    @Column(name = "SELLER")
    private String seller;

    @Column(name = "TOTAL_PRICE", precision = 38, scale = 2)
    private BigDecimal totalPrice;

    /**
     * Cycle de vie encaissement : {@code A_ENCAISSER} (ticket émis), {@code PAYE}, {@code REMBOURSE}.
     */
    @Column(name = "PAIEMENT_STATUT", length = 32)
    private String paymentStatus;

    @OneToMany(mappedBy = "sell", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SellDetails> lignes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID")
    private Site site;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINT_DE_VENTE_ID")
    private PointDeVente pointDeVente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EFFECTUEE_PAR_USER_ID")
    private AppUser effectueePar;
}
