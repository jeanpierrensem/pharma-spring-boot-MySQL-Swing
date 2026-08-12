package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import com.officine.losto.s7.stocks.domain.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.math.*;
import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "MOUVEMENT_STOCK")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class MouvementStock extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    /**
     * Lot physique lié au mouvement (traçabilité bon interne, etc.).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BATCH_ID")
    private Batch batch;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_MOUVEMENT", nullable = false, length = 32)
    private TypeMouvementStock typeMouvement;

    @Column(name = "QUANTITE_ALGEBRIQUE", nullable = false)
    private Integer quantiteAlgebrique;

    /**
     * Prix d'achat au moment du mouvement (traçabilité valorisation).
     */
    @Column(name = "COST_PRICE", precision = 12, scale = 2)
    private BigDecimal costPrice;

    /**
     * Prix de vente au moment du mouvement.
     */
    @Column(name = "SELL_PRICE", precision = 12, scale = 2)
    private BigDecimal sellPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "REFERENCE_TYPE", length = 32)
    private ReferenceStockType referenceType;

    @Column(name = "REFERENCE_ID")
    private Long referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID")
    private Site site;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINT_DE_VENTE_ID")
    private PointDeVente pointDeVente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_ID")
    private AppUser appUser;

    @Column(name = "DATE_MOUVEMENT")
    private LocalDateTime dateMouvement;

    @Column(name = "COMMENTAIRE", length = 512)
    private String commentaire;
}
