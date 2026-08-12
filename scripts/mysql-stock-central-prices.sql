-- ---------------------------------------------------------------------------
-- Stock central : prix d'achat et prix de vente par ligne (produit + lot)
-- ---------------------------------------------------------------------------
-- MySQL / MariaDB
-- ---------------------------------------------------------------------------

ALTER TABLE stock_central
    ADD COLUMN IF NOT EXISTS cost_price DECIMAL(12, 2) NULL AFTER qte_disponible,
    ADD COLUMN IF NOT EXISTS sell_price DECIMAL(12, 2) NULL AFTER cost_price;

-- Renseigne des prix aléatoires pour les lignes existantes sans prix
UPDATE stock_central
SET cost_price = ROUND(0.5 + RAND() * 27.5, 2),
    sell_price = ROUND((0.5 + RAND() * 27.5) * (1.12 + RAND() * 0.47), 2)
WHERE cost_price IS NULL OR sell_price IS NULL;
