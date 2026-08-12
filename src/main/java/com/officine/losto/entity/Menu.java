package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;

import java.util.*;

/**
 * Table <code>MENU</code> : habilitation hiérarchique (profondeur 3 : menu → onglet → action).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@Entity
@Table(name = "MENU")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class Menu extends AbstractEntity {

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NAME")
    private String name;

    /**
     * Clé stable pour synchroniser le catalogue (refresh après évolution des IHM).
     */
    @Column(name = "PATH_CODE", unique = true, length = 190)
    private String pathCode;

    /**
     * 0 = menu principal, 1 = onglet, 2 = action formulaire.
     */
    @Column(name = "TREE_LEVEL")
    private Integer treeLevel;

    @Column(name = "SORT_ORDER", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Menu parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC, id ASC")
    @Builder.Default
    private List<Menu> children = new ArrayList<>();

    @ManyToMany(mappedBy = "menus", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<AppGroup> groups = new HashSet<>();
}
