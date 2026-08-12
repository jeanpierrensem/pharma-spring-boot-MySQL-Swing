-- Stock central : clé métier (magasin_central_id, product_id, batch_id)
-- Permet plusieurs lignes pour le même produit avec des lots différents.
-- MySQL / MariaDB — à exécuter sur une base existante avant redémarrage de l'appli.

START TRANSACTION;

-- 1) Colonne lot (nullable le temps de la migration)
ALTER TABLE stock_central
    ADD COLUMN IF NOT EXISTS batch_id BIGINT NULL AFTER product_id;

-- 2) Lot par défaut pour lignes existantes sans lot
SET @default_batch = (SELECT id FROM batch ORDER BY id ASC LIMIT 1);

UPDATE stock_central
SET batch_id = @default_batch
WHERE batch_id IS NULL AND @default_batch IS NOT NULL;

-- 3) Rendre batch_id obligatoire
ALTER TABLE stock_central
    MODIFY batch_id BIGINT NOT NULL;

-- 4) Contrainte FK lot (ajuster le nom si déjà présente)
-- ALTER TABLE stock_central ADD CONSTRAINT fk_stock_central_batch FOREIGN KEY (batch_id) REFERENCES batch(id);

-- 5) Remplacer l'ancienne contrainte d'unicité produit seul
ALTER TABLE stock_central DROP INDEX IF EXISTS UK_STOCK_CENTRAL_MAGASIN_PRODUCT;
ALTER TABLE stock_central DROP INDEX IF EXISTS uk_stock_central_magasin_product;

ALTER TABLE stock_central
    ADD CONSTRAINT UK_STOCK_CENTRAL_MAGASIN_PRODUCT_BATCH
        UNIQUE (magasin_central_id, product_id, batch_id);

COMMIT;

-- Recréer le jeu de démo (optionnel — profil dev le fait au démarrage si central-stock-reset=true) :
-- DELETE FROM stock_central WHERE magasin_central_id = (SELECT id FROM magasin_central ORDER BY id LIMIT 1);
