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
@Table(name = "SNAPSHOT_INDICATEUR")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class SnapshotIndicateur extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID")
    private Site site;

    @Column(name = "MONTANT_VENTES", precision = 38, scale = 2)
    private BigDecimal montantVentes;

    @Column(name = "MONTANT_ACHATS", precision = 38, scale = 2)
    private BigDecimal montantAchats;

    @Column(name = "MARGE_BRUTE", precision = 38, scale = 2)
    private BigDecimal margeBrute;

    @Column(name = "RESULTAT", precision = 38, scale = 2)
    private BigDecimal resultat;

    @Column(name = "PERIODE_DEBUT")
    private LocalDateTime periodeDebut;

    @Column(name = "PERIODE_FIN")
    private LocalDateTime periodeFin;

    @Column(name = "CALCULE_LE")
    private LocalDateTime calculeLe;
}
