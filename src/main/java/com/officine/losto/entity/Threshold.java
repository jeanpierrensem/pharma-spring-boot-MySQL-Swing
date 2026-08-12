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
@Table(name = "THRESHOLD")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class Threshold extends AbstractEntity {

    @Column(name = "CODE")
    private String code;

    @Column(name = "COLOR_HEX", length = 32)
    private String colorHex;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LEVEL", nullable = false)
    private Integer level;
}
