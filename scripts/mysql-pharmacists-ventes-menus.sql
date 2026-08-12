-- Attribue au groupe « Pharmaciens » les menus Ventes + Organisation + Approvisionnement + Pilotage
-- (utile si la base a été créée avec l’ancien seed « pilotage seul »).
-- À exécuter une fois sur la base officine (MySQL).

INSERT IGNORE INTO group_menu (group_id, menu_id)
SELECT g.id, m.id
FROM APP_GROUP g
CROSS JOIN MENU m
WHERE g.NAME = 'Pharmaciens'
  AND (
    m.PATH_CODE = 'nav.sales'
    OR m.PATH_CODE LIKE 'nav.sales.%'
    OR m.PATH_CODE = 'nav.organisation'
    OR m.PATH_CODE LIKE 'nav.organisation.%'
    OR m.PATH_CODE = 'nav.approvisionnement'
    OR m.PATH_CODE LIKE 'nav.approvisionnement.%'
    OR m.PATH_CODE = 'nav.pilotage'
    OR m.PATH_CODE LIKE 'nav.pilotage.%'
  );
