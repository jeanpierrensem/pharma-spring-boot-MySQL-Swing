package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
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
@Table(
        name = "STOCK_CENTRAL",
        uniqueConstraints = @UniqueConstraint(
                name = "UK_STOCK_CENTRAL_MAGASIN_PRODUCT_BATCH",
                columnNames = {"MAGASIN_CENTRAL_ID", "PRODUCT_ID", "BATCH_ID"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class StockCentral extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID")
    private Site site;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MAGASIN_CENTRAL_ID", nullable = false)
    private MagasinCentral magasinCentral;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BATCH_ID", nullable = false)
    private Batch batch;

    @Column(name = "QTE_DISPONIBLE")
    private Integer qteDisponible;

    @Column(name = "COST_PRICE", precision = 12, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "SELL_PRICE", precision = 12, scale = 2)
    private BigDecimal sellPrice;

    @Column(name = "QTE_RESERVEE")
    private Integer qteReservee;

    @Column(name = "QTE_SEUIL_ALERTE")
    private Integer qteSeuilAlerte;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
