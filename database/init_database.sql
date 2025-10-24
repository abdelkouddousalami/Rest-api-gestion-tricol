-- ============================================
-- Script SQL pour Tricol - Gestion Fournisseurs
-- ============================================

-- Créer la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS tricol_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE tricol_db;

-- Supprimer la table si elle existe (pour tests)
-- DROP TABLE IF EXISTS fournisseurs;

-- Créer la table fournisseurs
CREATE TABLE IF NOT EXISTS fournisseurs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    societe VARCHAR(100) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    contact VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telephone VARCHAR(20) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    ice VARCHAR(15) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_societe (societe),
    INDEX idx_ville (ville),
    INDEX idx_ice (ice),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insérer des données de test
INSERT INTO fournisseurs (societe, adresse, contact, email, telephone, ville, ice) VALUES
('Textile Pro SA', '123 Boulevard Mohammed V', 'Ahmed Alami', 'contact@textilepro.ma', '+212600000000', 'Casablanca', '001234567890123'),
('Confection Elite', '45 Rue des Artisans', 'Fatima Bennani', 'f.bennani@confection-elite.ma', '+212611111111', 'Rabat', '001234567890124'),
('Maroc Tissus SARL', '78 Avenue Hassan II', 'Youssef Tazi', 'y.tazi@maroctissus.ma', '+212622222222', 'Casablanca', '001234567890125'),
('Industrie Mode', '15 Zone Industrielle Ain Sebaa', 'Karim Hafidi', 'k.hafidi@industriemode.ma', '+212633333333', 'Casablanca', '001234567890126'),
('Couture Moderne', '89 Boulevard Moulay Youssef', 'Nadia Chakir', 'n.chakir@couturemoderne.ma', '+212644444444', 'Rabat', '001234567890127'),
('Textile Nord', '234 Quartier Industriel', 'Mohamed Berrada', 'm.berrada@textilenord.ma', '+212655555555', 'Tanger', '001234567890128'),
('Fashion Group', '67 Route de Rabat', 'Laila Mansouri', 'l.mansouri@fashiongroup.ma', '+212666666666', 'Casablanca', '001234567890129'),
('Pro Vêtements', '101 Rue Allal Ben Abdellah', 'Omar Filali', 'o.filali@provetements.ma', '+212677777777', 'Fès', '001234567890130');

-- Afficher les données insérées
SELECT * FROM fournisseurs;

-- Requêtes utiles pour tester

-- 1. Compter le nombre de fournisseurs
SELECT COUNT(*) as total_fournisseurs FROM fournisseurs;

-- 2. Fournisseurs par ville
SELECT ville, COUNT(*) as nombre_fournisseurs 
FROM fournisseurs 
GROUP BY ville 
ORDER BY nombre_fournisseurs DESC;

-- 3. Rechercher un fournisseur par société
SELECT * FROM fournisseurs WHERE societe LIKE '%Textile%';

-- 4. Fournisseurs triés par nom
SELECT * FROM fournisseurs ORDER BY societe ASC;

-- 5. Fournisseurs à Casablanca
SELECT * FROM fournisseurs WHERE ville = 'Casablanca';
