package com.youcode.services;

import com.youcode.entities.Fournisseur;

import java.util.List;
import java.util.Optional;

public interface FournisseurService {

    /**
     * Crée un nouveau fournisseur
     */
    Fournisseur createFournisseur(Fournisseur fournisseur);

    /**
     * Met à jour un fournisseur existant
     */
    Fournisseur updateFournisseur(Long id, Fournisseur fournisseur);

    /**
     * Supprime un fournisseur par son ID
     */
    void deleteFournisseur(Long id);

    /**
     * Récupère un fournisseur par son ID
     */
    Optional<Fournisseur> getFournisseurById(Long id);

    /**
     * Récupère tous les fournisseurs
     */
    List<Fournisseur> getAllFournisseurs();

    /**
     * Récupère tous les fournisseurs triés par nom de société
     */
    List<Fournisseur> getAllFournisseursSortedByName();

    /**
     * Récupère tous les fournisseurs triés par ville puis par nom
     */
    List<Fournisseur> getAllFournisseursSortedByVilleAndName();

    /**
     * Recherche un fournisseur par société
     */
    Optional<Fournisseur> findBySociete(String societe);

    /**
     * Recherche des fournisseurs par ville
     */
    List<Fournisseur> findByVille(String ville);

    /**
     * Recherche un fournisseur par ICE
     */
    Optional<Fournisseur> findByIce(String ice);

    /**
     * Recherche des fournisseurs dont le nom contient une chaîne
     */
    List<Fournisseur> searchBySociete(String societe);

    /**
     * Recherche des fournisseurs par mot-clé (société, ville, contact)
     */
    List<Fournisseur> searchFournisseurs(String keyword);

    /**
     * Compte le nombre de fournisseurs
     */
    long countFournisseurs();

    /**
     * Compte le nombre de fournisseurs dans une ville
     */
    long countByVille(String ville);

    /**
     * Vérifie si un email existe déjà
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un ICE existe déjà
     */
    boolean existsByIce(String ice);
}
