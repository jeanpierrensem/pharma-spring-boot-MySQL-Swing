package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.time.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(
        name = "STOCK_PDV",
        uniqueConstraints = @UniqueConstraint(
                name = "UK_STOCK_PDV_PDV_PRODUCT",
                columnNames = {"POINT_DE_VENTE_ID", "PRODUCT_ID"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class StockPdv extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POINT_DE_VENTE_ID", nullable = false)
    private PointDeVente pointDeVente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Column(name = "QTE_DISPONIBLE")
    private Integer qteDisponible;

    @Column(name = "QTE_RESERVEE")
    private Integer qteReservee;

    @Column(name = "QTE_SEUIL_ALERTE")
    private Integer qteSeuilAlerte;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}