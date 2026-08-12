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
@Table(name = "PRODUCT")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class Product extends AbstractEntity {

    @Column(name = "CODE_BAR")
    private String codeBar;

    @Column(name = "FAMILLE")
    private String famille;

    @Column(name = "DOSAGE")
    private String dosage;

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_ID")
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PACKAGING_ID")
    private Packaging packaging;

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "PHOTO_FILENAME")
    private String photoFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DRUG_TYPE_ID")
    private DrugType drugType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECTION_ID")
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITE_ID")
    private Site site;

    /**
     * Seuils d’alerte (0..n) — référentiel {@link Threshold}.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_threshold",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "threshold_id"))
    @Builder.Default
    private Set<Threshold> thresholds = new HashSet<>();
}
