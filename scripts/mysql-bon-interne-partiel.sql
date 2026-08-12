-- Bon interne : livraison partielle (statut PARTIEL + quantité livrée cumulée par ligne).
-- Statut PARTIEL : au moins une ligne avec quantité livrée < quantité commandée après traitement magasin.
-- La colonne STATUT de BON_COMMANDE_INTERNE accepte la valeur 'PARTIEL' (VARCHAR ou ENUM étendu).

ALTER TABLE LIGNE_BON_COMMANDE_INTERNE
    ADD COLUMN QUANTITY_DELIVERED INT NULL AFTER QUANTITY;
