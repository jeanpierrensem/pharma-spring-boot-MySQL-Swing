-- ---------------------------------------------------------------------------
-- Données de démonstration : SITE, MAGASIN_CENTRAL, POINT_DE_VENTE
-- ---------------------------------------------------------------------------
-- Usage :
--   • À exécuter sur une base où ces tables existent déjà (schéma JPA / Flyway, etc.).
--   • Adapter les noms de tables si votre dialecte les a en majuscules (ex. SITE vs site).
--   • Les codes DEMO-* sont uniques (contraintes métier) : ne pas dupliquer le script tel quel.
--
-- MySQL / MariaDB : variables de session pour chaîner les FK sans connaître les IDs à l’avance.
-- ---------------------------------------------------------------------------

START TRANSACTION;

INSERT INTO site (actif, code, libelle, responsable_user_id)
VALUES (TRUE, 'DEMO-PARIS', 'Pharmacie démo — Paris Centre', NULL);
SET @site_paris = LAST_INSERT_ID();

INSERT INTO site (actif, code, libelle, responsable_user_id)
VALUES (TRUE, 'DEMO-LYON', 'Pharmacie démo — Lyon Part-Dieu', NULL);
SET @site_lyon = LAST_INSERT_ID();

INSERT INTO magasin_central (site_id, code, libelle)
VALUES (@site_paris, 'DEMO-MC-PARIS', 'Magasin central Paris');

INSERT INTO magasin_central (site_id, code, libelle)
VALUES (@site_lyon, 'DEMO-MC-LYON', 'Magasin central Lyon');

INSERT INTO point_de_vente (site_id, code, libelle, adresse, telephone, actif)
VALUES (@site_paris, 'DEMO-PDV-PARIS-CAISSE', 'Caisse principale',
        '12 rue de la Paix, 75002 Paris', '0142000001', TRUE);

INSERT INTO point_de_vente (site_id, code, libelle, adresse, telephone, actif)
VALUES (@site_paris, 'DEMO-PDV-PARIS-ORTHO', 'Espace orthopédie',
        '12 rue de la Paix, 75002 Paris', '0142000002', TRUE);

INSERT INTO point_de_vente (site_id, code, libelle, adresse, telephone, actif)
VALUES (@site_lyon, 'DEMO-PDV-LYON-CAISSE', 'Caisse hall A',
        '17 bd Vivier Merle, 69003 Lyon', '0478000001', TRUE);

COMMIT;

-- Vérifications rapides :
-- SELECT * FROM site WHERE code LIKE 'DEMO-%';
-- SELECT * FROM magasin_central WHERE code LIKE 'DEMO-%';
-- SELECT * FROM point_de_vente WHERE code LIKE 'DEMO-%';
