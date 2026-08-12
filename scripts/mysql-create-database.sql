-- Création de la base MySQL / MariaDB pour le profil dev Spring Boot.
-- Usage : mysql -u root -p < scripts/mysql-create-database.sql

CREATE DATABASE IF NOT EXISTS officine
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Optionnel : utilisateur dédié (adapter le mot de passe)
-- CREATE USER IF NOT EXISTS 'officine'@'localhost' IDENTIFIED BY 'officine';
-- GRANT ALL PRIVILEGES ON officine.* TO 'officine'@'localhost';
-- FLUSH PRIVILEGES;
