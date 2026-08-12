-- Supprime les logins en double (MySQL insensible à la casse : admin + Admin).
-- Conserve l'utilisateur avec le plus petit ID par login normalisé.

DELETE rt FROM REFRESH_TOKEN rt
INNER JOIN APP_USER u ON u.ID = rt.USER_ID
INNER JOIN (
    SELECT LOWER(LOGIN) AS login_key, MIN(ID) AS keep_id
    FROM APP_USER
    GROUP BY LOWER(LOGIN)
    HAVING COUNT(*) > 1
) d ON LOWER(u.LOGIN) = d.login_key AND u.ID <> d.keep_id;

DELETE u FROM APP_USER u
INNER JOIN (
    SELECT LOWER(LOGIN) AS login_key, MIN(ID) AS keep_id
    FROM APP_USER
    GROUP BY LOWER(LOGIN)
    HAVING COUNT(*) > 1
) d ON LOWER(u.LOGIN) = d.login_key AND u.ID <> d.keep_id;

UPDATE APP_USER SET LOGIN = LOWER(LOGIN) WHERE LOGIN <> LOWER(LOGIN);

-- Index unique (si absent) — peut échouer si des doublons subsistent
-- ALTER TABLE APP_USER ADD UNIQUE INDEX uk_app_user_login (LOGIN);
