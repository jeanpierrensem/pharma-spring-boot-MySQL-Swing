-- ---------------------------------------------------------------------------
-- Stock central : jusqu'à 2 lignes par produit (lots distincts), quantités aléatoires,
-- réserve 0, seuil d’alerte = niveau THR-STOCK-BAS, liaison product_threshold.
-- ---------------------------------------------------------------------------
-- Prérequis :
--   • Au moins un magasin_central et au moins 2 lots dans batch
--   • Seuil THR-STOCK-BAS dans threshold
--   • Colonne batch_id sur stock_central (voir mysql-stock-central-product-batch-key.sql)
--
-- MySQL / MariaDB — recrée le stock central du premier magasin.
-- ---------------------------------------------------------------------------

START TRANSACTION;

SET @mc    = (SELECT id FROM magasin_central ORDER BY id ASC LIMIT 1);
SET @site  = (SELECT site_id FROM magasin_central WHERE id = @mc);
SET @thr   = (SELECT id FROM threshold WHERE code = 'THR-STOCK-BAS' LIMIT 1);
SET @level = (SELECT level FROM threshold WHERE id = @thr);
SET @lot1  = (SELECT id FROM batch ORDER BY id ASC LIMIT 1);
SET @lot2  = (SELECT id FROM batch ORDER BY id ASC LIMIT 1 OFFSET 1);

INSERT INTO product_threshold (product_id, threshold_id)
SELECT p.id, @thr
FROM product p
WHERE @thr IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM product_threshold pt
    WHERE pt.product_id = p.id AND pt.threshold_id = @thr
  );

DELETE FROM stock_central WHERE magasin_central_id = @mc;

-- Lot 1 pour chaque produit (prix achat aléatoire 0,50–28 €, marge ~12–55 %)
INSERT INTO stock_central (site_id, magasin_central_id, product_id, batch_id, qte_disponible, cost_price, sell_price, qte_reservee, qte_seuil_alerte, updated_at)
SELECT @site, @mc, p.id, @lot1,
       FLOOR(1 + RAND() * 500),
       ROUND(0.5 + RAND() * 27.5, 2),
       ROUND((0.5 + RAND() * 27.5) * (1.12 + RAND() * 0.47), 2),
       0,
       COALESCE(@level, 10),
       CURRENT_TIMESTAMP
FROM product p
WHERE @mc IS NOT NULL AND @site IS NOT NULL AND @lot1 IS NOT NULL;

-- Lot 2 pour chaque produit (si un second lot existe)
INSERT INTO stock_central (site_id, magasin_central_id, product_id, batch_id, qte_disponible, cost_price, sell_price, qte_reservee, qte_seuil_alerte, updated_at)
SELECT @site, @mc, p.id, @lot2,
       FLOOR(1 + RAND() * 500),
       ROUND(0.5 + RAND() * 27.5, 2),
       ROUND((0.5 + RAND() * 27.5) * (1.12 + RAND() * 0.47), 2),
       0,
       COALESCE(@level, 10),
       CURRENT_TIMESTAMP
FROM product p
WHERE @mc IS NOT NULL AND @site IS NOT NULL AND @lot2 IS NOT NULL AND @lot2 <> @lot1;

COMMIT;

-- SELECT product_id, batch_id, qte_disponible FROM stock_central WHERE magasin_central_id = @mc ORDER BY product_id, batch_id;
