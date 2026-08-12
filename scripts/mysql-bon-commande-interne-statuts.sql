-- Alignement des statuts des bons de commande interne avec StatutBonCommandeInterne (JPA EnumType.STRING).
-- À exécuter sur MySQL si la colonne STATUT contient d’anciennes valeurs libres (ex. SOUMIS, EN_COURS).

UPDATE BON_COMMANDE_INTERNE SET STATUT = 'ENVOYE' WHERE UPPER(TRIM(STATUT)) IN ('SOUMIS', 'EN_COURS', 'SOUMISE');

UPDATE BON_COMMANDE_INTERNE SET STATUT = 'BROUILLON' WHERE STATUT IS NULL OR TRIM(STATUT) = '';

-- Valeurs hors ensemble attendu → BROUILLON (à ajuster manuellement si besoin)
UPDATE BON_COMMANDE_INTERNE
SET STATUT = 'BROUILLON'
WHERE STATUT NOT IN ('BROUILLON', 'ENVOYE', 'PARTIEL', 'TRAITE', 'ANNULE');
