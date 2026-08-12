package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "POINT_DE_VENTE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class PointDeVente extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID", nullable = false)
    private Site site;

    @Column(name = "CODE", nullable = false, length = 64)
    private String code;

    @Column(name = "LIBELLE", nullable = false, length = 255)
    private String libelle;

    @Column(name = "ADRESSE", length = 500)
    private String adresse;

    @Column(name = "TELEPHONE", length = 64)
    private String phone;

    @Column(name = "ACTIF", nullable = false)
    @Builder.Default
    private boolean actif = true;
}
