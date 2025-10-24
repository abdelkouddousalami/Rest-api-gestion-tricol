package com.youcode.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "fournisseurs")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La société est obligatoire")
    @Size(min = 2, max = 100, message = "La société doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String societe;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 255, message = "L'adresse ne peut pas dépasser 255 caractères")
    @Column(nullable = false)
    private String adresse;

    @NotBlank(message = "Le contact est obligatoire")
    @Size(max = 100, message = "Le contact ne peut pas dépasser 100 caractères")
    @Column(nullable = false, length = 100)
    private String contact;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Le téléphone doit être valide (10-15 chiffres)")
    @Column(nullable = false, length = 20)
    private String telephone;

    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 100, message = "La ville ne peut pas dépasser 100 caractères")
    @Column(nullable = false, length = 100)
    private String ville;

    @NotBlank(message = "L'ICE est obligatoire")
    @Pattern(regexp = "^[0-9]{15}$", message = "L'ICE doit contenir exactement 15 chiffres")
    @Column(nullable = false, unique = true, length = 15)
    private String ice;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Fournisseur() {
    }

    public Fournisseur(String societe, String adresse, String contact, String email, 
                       String telephone, String ville, String ice) {
        this.societe = societe;
        this.adresse = adresse;
        this.contact = contact;
        this.email = email;
        this.telephone = telephone;
        this.ville = ville;
        this.ice = ice;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id=" + id +
                ", societe='" + societe + '\'' +
                ", adresse='" + adresse + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", ville='" + ville + '\'' +
                ", ice='" + ice + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
