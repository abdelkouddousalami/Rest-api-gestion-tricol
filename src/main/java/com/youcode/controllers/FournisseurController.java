package com.youcode.controllers;

import com.youcode.dto.ApiResponse;
import com.youcode.entities.Fournisseur;
import com.youcode.exceptions.ResourceAlreadyExistsException;
import com.youcode.exceptions.ResourceNotFoundException;
import com.youcode.services.FournisseurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/fournisseurs")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @Autowired
    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    /**
     * GET /api/v1/fournisseurs
     * Récupère tous les fournisseurs avec option de tri
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Fournisseur>>> getAllFournisseurs(
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "ville", required = false) String ville,
            @RequestParam(name = "search", required = false) String search) {

        List<Fournisseur> fournisseurs;

        if (search != null && !search.trim().isEmpty()) {
            // Recherche par mot-clé
            fournisseurs = fournisseurService.searchFournisseurs(search);
        } else if (ville != null && !ville.trim().isEmpty()) {
            // Filtrer par ville
            fournisseurs = fournisseurService.findByVille(ville);
        } else if ("nom".equalsIgnoreCase(sortBy) || "societe".equalsIgnoreCase(sortBy)) {
            // Tri par nom de société
            fournisseurs = fournisseurService.getAllFournisseursSortedByName();
        } else if ("ville".equalsIgnoreCase(sortBy)) {
            // Tri par ville puis par nom
            fournisseurs = fournisseurService.getAllFournisseursSortedByVilleAndName();
        } else {
            // Liste par défaut
            fournisseurs = fournisseurService.getAllFournisseurs();
        }

        ApiResponse<List<Fournisseur>> response = new ApiResponse<>(
                true,
                "Fournisseurs récupérés avec succès",
                fournisseurs
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/fournisseurs/{id}
     * Récupère un fournisseur par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Fournisseur>> getFournisseurById(@PathVariable Long id) {
        Fournisseur fournisseur = fournisseurService.getFournisseurById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id));

        ApiResponse<Fournisseur> response = new ApiResponse<>(
                true,
                "Fournisseur récupéré avec succès",
                fournisseur
        );

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/fournisseurs
     * Crée un nouveau fournisseur
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Fournisseur>> createFournisseur(
            @Valid @RequestBody Fournisseur fournisseur,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            ApiResponse<Fournisseur> response = new ApiResponse<>(
                    false,
                    "Erreurs de validation",
                    null,
                    errors
            );
            return ResponseEntity.badRequest().body(response);
        }

        Fournisseur createdFournisseur = fournisseurService.createFournisseur(fournisseur);

        ApiResponse<Fournisseur> response = new ApiResponse<>(
                true,
                "Fournisseur créé avec succès",
                createdFournisseur
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/v1/fournisseurs/{id}
     * Met à jour un fournisseur existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Fournisseur>> updateFournisseur(
            @PathVariable Long id,
            @Valid @RequestBody Fournisseur fournisseur,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            ApiResponse<Fournisseur> response = new ApiResponse<>(
                    false,
                    "Erreurs de validation",
                    null,
                    errors
            );
            return ResponseEntity.badRequest().body(response);
        }

        Fournisseur updatedFournisseur = fournisseurService.updateFournisseur(id, fournisseur);

        ApiResponse<Fournisseur> response = new ApiResponse<>(
                true,
                "Fournisseur mis à jour avec succès",
                updatedFournisseur
        );

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/fournisseurs/{id}
     * Supprime un fournisseur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFournisseur(@PathVariable Long id) {
        fournisseurService.deleteFournisseur(id);

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Fournisseur supprimé avec succès",
                null
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/fournisseurs/count
     * Compte le nombre total de fournisseurs
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countFournisseurs(
            @RequestParam(name = "ville", required = false) String ville) {

        long count;
        String message;

        if (ville != null && !ville.trim().isEmpty()) {
            count = fournisseurService.countByVille(ville);
            message = "Nombre de fournisseurs dans la ville " + ville;
        } else {
            count = fournisseurService.countFournisseurs();
            message = "Nombre total de fournisseurs";
        }

        ApiResponse<Long> response = new ApiResponse<>(
                true,
                message,
                count
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Gestion des exceptions spécifiques au contrôleur
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Une erreur interne s'est produite: " + ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
