package com.officine.losto.entity;

import com.fasterxml.jackson.annotation.*;
import com.officine.losto.security.*;
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
@Table(name = "APP_USER")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public final class AppUser extends AbstractEntity {

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "LOGIN", unique = true)
    private String login;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "PHOTO_FILENAME")
    private String photoFilename;
    @Column(name = "PROFIL_METIER", length = 32)
    private String profilMetier;

    @Column(name = "COMPTE_ACTIF")
    private Boolean compteActif;

    @Column(name = "ENABLED", nullable = false)
    @lombok.Builder.Default
    private Boolean enabled = true;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private AppGroup group;

    @PrePersist
    void onCreate() {
        normalizeLogin();
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (enabled == null) {
            enabled = true;
        }
        if (compteActif == null) {
            compteActif = enabled;
        }
    }

    @PreUpdate
    void onUpdate() {
        normalizeLogin();
    }

    private void normalizeLogin() {
        login = LoginNormalizer.normalize(login);
    }

}
