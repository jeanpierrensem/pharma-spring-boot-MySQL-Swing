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
@Table(name = "MAGASIN_CENTRAL")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class MagasinCentral extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID", nullable = false, unique = true)
    private Site site;

    @Column(name = "CODE", nullable = false, length = 64)
    private String code;

    @Column(name = "LIBELLE", nullable = false, length = 255)
    private String libelle;
}
