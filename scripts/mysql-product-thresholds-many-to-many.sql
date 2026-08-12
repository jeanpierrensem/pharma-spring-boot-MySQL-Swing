-- Migration : passage du seuil produit (FK unique) à une association n-n via product_threshold.
-- À exécuter sur MySQL avant redémarrage avec un schéma aligné sur l’entité Product (sans colonne threshold_id).
-- 1) Vérifier le nom exact de la contrainte FK sur product.threshold_id : SHOW CREATE TABLE product;

CREATE TABLE IF NOT EXISTS product_threshold (
    product_id   BIGINT NOT NULL,
    threshold_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, threshold_id),
    CONSTRAINT fk_product_threshold_product FOREIGN KEY (product_id) REFERENCES product (id),
    CONSTRAINT fk_product_threshold_threshold FOREIGN KEY (threshold_id) REFERENCES threshold (id)
);

INSERT INTO product_threshold (product_id, threshold_id)
SELECT id, threshold_id
FROM product
WHERE threshold_id IS NOT NULL;

-- Remplacer XXX par le nom réel de la contrainte FK sur la colonne threshold_id :
-- ALTER TABLE product DROP FOREIGN KEY XXX;
-- ALTER TABLE product DROP COLUMN threshold_id;
