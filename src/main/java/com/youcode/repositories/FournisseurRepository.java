package com.youcode.repositories;

import com.youcode.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    // Query Methods - Spring Data JPA génère automatiquement les requêtes
    
    /**
     * Recherche un fournisseur par son nom de société
     */
    Optional<Fournisseur> findBySociete(String societe);

    /**
     * Recherche des fournisseurs dont l'email se termine par un domaine spécifique
     */
    List<Fournisseur> findByEmailEndingWith(String emailDomain);

    /**
     * Recherche des fournisseurs par ville
     */
    List<Fournisseur> findByVille(String ville);

    /**
     * Recherche un fournisseur par son ICE
     */
    Optional<Fournisseur> findByIce(String ice);

    /**
     * Recherche des fournisseurs dont le nom de société contient une chaîne (insensible à la casse)
     */
    List<Fournisseur> findBySocieteContainingIgnoreCase(String societe);

    /**
     * Compte le nombre de fournisseurs dans une ville donnée
     */
    long countByVille(String ville);

    /**
     * Vérifie si un fournisseur existe avec un email donné
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un fournisseur existe avec un ICE donné
     */
    boolean existsByIce(String ice);

    /**
     * Récupère tous les fournisseurs triés par nom de société
     */
    List<Fournisseur> findAllByOrderBySocieteAsc();

    /**
     * Récupère tous les fournisseurs triés par ville puis par nom de société
     */
    List<Fournisseur> findAllByOrderByVilleAscSocieteAsc();

    /**
     * Requête personnalisée utilisant @Query
     * Recherche des fournisseurs par ville avec tri
     */
    @Query("SELECT f FROM Fournisseur f WHERE f.ville = :ville ORDER BY f.societe ASC")
    List<Fournisseur> findFournisseursByVilleOrderedBySociete(@Param("ville") String ville);

    /**
     * Requête personnalisée pour rechercher par plusieurs critères
     */
    @Query("SELECT f FROM Fournisseur f WHERE " +
           "LOWER(f.societe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.ville) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.contact) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Fournisseur> searchFournisseurs(@Param("keyword") String keyword);
}
