-- =============================================================================
-- Organisation (s1) — contrainte d'unicité MAGASIN_CENTRAL.SITE_ID
-- =============================================================================
-- Contexte : un site ne doit avoir qu'un seul magasin central (relation 1–1).
-- À exécuter sur MySQL après vérification qu'il n'existe pas plusieurs lignes
-- MAGASIN_CENTRAL pour le même SITE_ID.
--
-- 1) Diagnostic des doublons (à lancer avant la migration) :
--
--   SELECT SITE_ID, COUNT(*) AS nb
--   FROM MAGASIN_CENTRAL
--   GROUP BY SITE_ID
--   HAVING COUNT(*) > 1;
--
-- 2) Si des doublons existent : les corriger manuellement (fusion, suppression,
--    réaffectation) puis relancer ce script.
--
-- 3) Application de la contrainte :
-- =============================================================================

ALTER TABLE MAGASIN_CENTRAL
    ADD CONSTRAINT UK_MAGASIN_CENTRAL_SITE UNIQUE (SITE_ID);

-- Si la contrainte existe déjà sous un autre nom, utilisez plutôt :
-- SHOW INDEX FROM MAGASIN_CENTRAL WHERE Column_name = 'SITE_ID';
