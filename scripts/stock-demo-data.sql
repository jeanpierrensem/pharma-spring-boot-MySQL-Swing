-- ---------------------------------------------------------------------------
-- Données de démonstration : STOCK_CENTRAL, STOCK_PDV, MOUVEMENT_STOCK
-- ---------------------------------------------------------------------------
-- Prérequis (exemple cohérent avec le seed applicatif dev + organisation-demo-data.sql) :
--   • Magasin central : code DEMO-MC-PARIS
--   • Points de vente : DEMO-PDV-PARIS-CAISSE, DEMO-PDV-PARIS-ORTHO
--   • Produits : code-barres 3770012345678 (Paracétamol), 3770012345685 (Ibuprofène), 3770099990001 (Vit. D3)
--   • Lots : au moins 2 enregistrements dans batch (voir seed applicatif ou scripts batch)
--   • Colonne batch_id sur stock_central (voir mysql-stock-central-product-batch-key.sql)
--   • Optionnel : premier utilisateur (id=1) pour APP_USER_ID sur les mouvements
--
-- MySQL / MariaDB — clé stock central = (magasin_central_id, product_id, batch_id)
-- ---------------------------------------------------------------------------

START TRANSACTION;

SET @mc   = (SELECT id FROM magasin_central WHERE code = 'DEMO-MC-PARIS' LIMIT 1);
SET @site = (SELECT site_id FROM magasin_central WHERE id = @mc);
SET @pdv1 = (SELECT id FROM point_de_vente WHERE code = 'DEMO-PDV-PARIS-CAISSE' LIMIT 1);
SET @pdv2 = (SELECT id FROM point_de_vente WHERE code = 'DEMO-PDV-PARIS-ORTHO' LIMIT 1);
SET @p1   = (SELECT id FROM product WHERE code_bar = '3770012345678' LIMIT 1);
SET @p2   = (SELECT id FROM product WHERE code_bar = '3770012345685' LIMIT 1);
SET @p3   = (SELECT id FROM product WHERE code_bar = '3770099990001' LIMIT 1);
SET @lot1 = (SELECT id FROM batch ORDER BY id ASC LIMIT 1);
SET @lot2 = (SELECT id FROM batch ORDER BY id ASC LIMIT 1 OFFSET 1);
SET @user = (SELECT id FROM app_user ORDER BY id ASC LIMIT 1);

-- Paracétamol : 2 lots distincts
INSERT INTO stock_central (site_id, magasin_central_id, product_id, batch_id, qte_disponible, qte_reservee, qte_seuil_alerte, updated_at)
SELECT @site, @mc, @p1, @lot1, 80, 3, 25, CURRENT_TIMESTAMP
FROM DUAL WHERE @mc IS NOT NULL AND @p1 IS NOT NULL AND @lot1 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM stock_central WHERE magasin_central_id = @mc AND product_id = @p1 AND batch_id = @lot1);

INSERT INTO stock_central (site_id, magasin_central_id, product_id, batch_id, qte_disponible, qte_reservee, qte_seuil_alerte, updated_at)
SELECT @site, @mc, @p1, @lot2, 40, 2, 25, CURRENT_TIMESTAMP
FROM DUAL WHERE @mc IS NOT NULL AND @p1 IS NOT NULL AND @lot2 IS NOT NULL AND @lot2 <> @lot1
  AND NOT EXISTS (SELECT 1 FROM stock_central WHERE magasin_central_id = @mc AND product_id = @p1 AND batch_id = @lot2);

-- Ibuprofène : lot 1 (stock bas)
INSERT INTO stock_central (site_id, magasin_central_id, product_id, batch_id, qte_disponible, qte_reservee, qte_seuil_alerte, updated_at)
SELECT @site, @mc, @p2, @lot1, 8, 2, 15, CURRENT_TIMESTAMP
FROM DUAL WHERE @mc IS NOT NULL AND @p2 IS NOT NULL AND @lot1 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM stock_central WHERE magasin_central_id = @mc AND product_id = @p2 AND batch_id = @lot1);

-- Vit. D3 : lot 2
INSERT INTO stock_central (site_id, magasin_central_id, product_id, batch_id, qte_disponible, qte_reservee, qte_seuil_alerte, updated_at)
SELECT @site, @mc, @p3, COALESCE(@lot2, @lot1), 45, 0, 10, CURRENT_TIMESTAMP
FROM DUAL WHERE @mc IS NOT NULL AND @p3 IS NOT NULL AND COALESCE(@lot2, @lot1) IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM stock_central
    WHERE magasin_central_id = @mc AND product_id = @p3 AND batch_id = COALESCE(@lot2, @lot1)
  );

INSERT INTO stock_pdv (point_de_vente_id, product_id, qte_disponible, qte_reservee, qte_seuil_alerte, updated_at)
SELECT @pdv1, @p1, 24, 4, 8, CURRENT_TIMESTAMP
FROM DUAL WHERE @pdv1 IS NOT NULL AND @p1 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM stock_pdv WHERE point_de_vente_id = @pdv1 AND product_id = @p1);

INSERT INTO stock_pdv (point_de_vente_id, product_id, qte_disponible, qte_reservee, qte_seuil_alerte, updated_at)
SELECT @pdv1, @p2, 6, 0, 12, CURRENT_TIMESTAMP
FROM DUAL WHERE @pdv1 IS NOT NULL AND @p2 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM stock_pdv WHERE point_de_vente_id = @pdv1 AND product_id = @p2);

INSERT INTO stock_pdv (point_de_vente_id, product_id, qte_disponible, qte_reservee, qte_seuil_alerte, updated_at)
SELECT @pdv2, @p1, 15, 0, 5, CURRENT_TIMESTAMP
FROM DUAL WHERE @pdv2 IS NOT NULL AND @p1 IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM stock_pdv WHERE point_de_vente_id = @pdv2 AND product_id = @p1);

-- Mouvements (idempotent par libellé de commentaire)
INSERT INTO mouvement_stock (product_id, type_mouvement, quantite_algebrique, reference_type, reference_id, site_id, point_de_vente_id, app_user_id, date_mouvement, commentaire)
SELECT @p1, 'ENTREE', 50, 'BON_INTERNE', NULL, @site, @pdv1, @user, CURRENT_TIMESTAMP - INTERVAL 2 DAY, 'SQL demo — entrée magasin'
FROM DUAL
WHERE @p1 IS NOT NULL AND @site IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM mouvement_stock m WHERE m.commentaire = 'SQL demo — entrée magasin');

INSERT INTO mouvement_stock (product_id, type_mouvement, quantite_algebrique, reference_type, reference_id, site_id, point_de_vente_id, app_user_id, date_mouvement, commentaire)
SELECT @p1, 'SORTIE', -12, 'VENTE', NULL, @site, @pdv1, @user, CURRENT_TIMESTAMP - INTERVAL 1 DAY, 'SQL demo — sortie caisse'
FROM DUAL
WHERE @p1 IS NOT NULL AND @site IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM mouvement_stock m WHERE m.commentaire = 'SQL demo — sortie caisse');

INSERT INTO mouvement_stock (product_id, type_mouvement, quantite_algebrique, reference_type, reference_id, site_id, point_de_vente_id, app_user_id, date_mouvement, commentaire)
SELECT @p2, 'AJUSTEMENT_INVENTAIRE', -3, 'INVENTAIRE', NULL, @site, NULL, @user, CURRENT_TIMESTAMP - INTERVAL 5 HOUR, 'SQL demo — inventaire'
FROM DUAL
WHERE @p2 IS NOT NULL AND @site IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM mouvement_stock m WHERE m.commentaire = 'SQL demo — inventaire');

COMMIT;

-- Vérifications :
-- SELECT sc.*, b.number FROM stock_central sc JOIN batch b ON b.id = sc.batch_id WHERE sc.magasin_central_id = @mc;
-- SELECT * FROM stock_pdv WHERE point_de_vente_id IN (@pdv1, @pdv2);
-- SELECT * FROM mouvement_stock ORDER BY date_mouvement DESC;
