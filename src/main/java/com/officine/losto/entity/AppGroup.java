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
@Table(name = "APP_GROUP")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class AppGroup extends AbstractEntity {

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SELECTED", nullable = false)
    private Boolean selected;

    @ManyToMany
    @JoinTable(name = "group_menu", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "menu_id"))
    @Builder.Default
    private Set<Menu> menus = new HashSet<>();

}
