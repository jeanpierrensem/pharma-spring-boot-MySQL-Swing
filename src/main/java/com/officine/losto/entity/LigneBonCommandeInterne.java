package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.math.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "LIGNE_BON_COMMANDE_INTERNE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class LigneBonCommandeInterne extends AbstractEntity {

    @Column(name = "QUANTITY")
    private Integer quantity;

    /**
     * Quantité réellement livrée au traitement magasin central (≤ {@link #quantity}).
     */
    @Column(name = "QUANTITY_DELIVERED")
    private Integer quantityDelivered;

    @Column(name = "UNIT_PRICE", precision = 38, scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BON_ID")
    private BonCommandeInterne bon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    /**
     * Lot choisi lors du traitement au magasin central (picking).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BATCH_ID")
    private Batch batch;
}
