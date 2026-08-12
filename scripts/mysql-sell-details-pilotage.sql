-- s7_pilotage : lot et coût unitaire à la vente sur les lignes de ticket (MySQL).
-- Exécuter une fois sur les bases existantes ; Hibernate ddl-auto peut créer ces colonnes pour les nouveaux schémas.

ALTER TABLE SELL_DETAILS
  ADD COLUMN BATCH_ID BIGINT NULL COMMENT 'Lot sorti (optionnel ; sinon référence produit)' AFTER PRODUCT_ID,
  ADD COLUMN UNIT_COST_AT_SALE DECIMAL(38, 4) NULL COMMENT 'Coût unitaire à la vente (sinon PRODUCT.COST_PRICE)' AFTER BATCH_ID;

ALTER TABLE SELL_DETAILS
  ADD CONSTRAINT FK_SELL_DETAILS_BATCH FOREIGN KEY (BATCH_ID) REFERENCES BATCH (ID);
