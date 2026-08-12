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
@Table(name = "SELL_DETAILS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class SellDetails extends AbstractEntity {

    @Column(name = "DISCOUNT", nullable = false)
    private Integer discount;

    @Column(name = "PRICE", precision = 38, scale = 2)
    private BigDecimal price;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    /**
     * Lot réellement vendu (s7_pilotage).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BATCH_ID")
    private Batch batch;

    /**
     * Coût unitaire figé au moment de la vente (souvent issu du dernier mouvement de stock).
     */
    @Column(name = "UNIT_COST_AT_SALE", precision = 38, scale = 4)
    private BigDecimal unitCostAtSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELL_ID")
    private Sell sell;
}
