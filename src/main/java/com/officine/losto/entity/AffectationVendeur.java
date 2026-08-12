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
@Table(name = "AFFECTATION_VENDEUR")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class AffectationVendeur extends AbstractEntity {

    @Column(name = "DEBUT")
    private LocalDateTime debut;

    @Column(name = "FIN")
    private LocalDateTime fin;

    @Column(name = "ACTIF_CRENEAU")
    private Boolean actifCreneau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APP_USER_ID")
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINT_DE_VENTE_ID")
    private PointDeVente pointDeVente;
}
