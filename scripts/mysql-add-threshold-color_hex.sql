-- Production MySQL : ajouter la colonne couleur des seuils (profil avec spring.jpa.hibernate.ddl-auto=validate).
-- Exécuter une fois sur la base cible. Adapter le nom de table si votre stratégie de nommage diffère.

ALTER TABLE threshold
    ADD COLUMN color_hex VARCHAR(32) NULL COMMENT 'Couleur seuil (#RGB ou #RRGGBB)';
