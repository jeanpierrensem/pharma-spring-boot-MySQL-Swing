package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.time.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "BON_COMMANDE_INTERNE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class BonCommandeInterne extends AbstractEntity {

    @Column(name = "NUMBER", length = 64)
    private String number;

    @Column(name = "ORDER_DATE")
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUT", length = 32)
    private StatutBonCommandeInterne statut;

    @Column(name = "COMMENTAIRE", length = 2000)
    private String commentaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINT_DE_VENTE_ID")
    private PointDeVente pointDeVente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID")
    private Site site;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAGASIN_CENTRAL_ID")
    private MagasinCentral magasinCentral;

    @OneToMany(mappedBy = "bon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LigneBonCommandeInterne> lignes = new ArrayList<>();
}
