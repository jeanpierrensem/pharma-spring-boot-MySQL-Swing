package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.time.*;

/**
 * Commande fournisseur (table H2 <code>ORDERS</code>).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "ORDERS")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class Orders extends AbstractEntity {

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "ORDER_DATE")
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUT", length = 32)
    private Statut statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVIDER_ID")
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID")
    private Site site;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAGASIN_CENTRAL_ID")
    private MagasinCentral magasinCentral;
}
