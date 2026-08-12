package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "SITE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class Site extends AbstractEntity {

    @Column(name = "CODE", nullable = false, length = 64)
    private String code;

    @Column(name = "LIBELLE", nullable = false, length = 255)
    private String libelle;

    /**
     * Référence applicative vers {@link AppUser} (pas de contrainte JPA obligatoire).
     */
    @Column(name = "RESPONSABLE_USER_ID")
    private Long responsableUserId;

    @Column(name = "ACTIF", nullable = false)
    @Builder.Default
    private boolean actif = true;

    @OneToOne(mappedBy = "site", fetch = FetchType.LAZY)
    private MagasinCentral magasinCentral;

    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    @Builder.Default
    private List<PointDeVente> pointsDeVente = new ArrayList<>();
}
