package com.youcode.services.impl;

import com.youcode.entities.Fournisseur;
import com.youcode.exceptions.ResourceAlreadyExistsException;
import com.youcode.exceptions.ResourceNotFoundException;
import com.youcode.repositories.FournisseurRepository;
import com.youcode.services.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FournisseurServiceImpl implements FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    @Autowired
    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @Override
    public Fournisseur createFournisseur(Fournisseur fournisseur) {
        // Vérifier si l'email existe déjà
        if (fournisseurRepository.existsByEmail(fournisseur.getEmail())) {
            throw new ResourceAlreadyExistsException("Un fournisseur avec cet email existe déjà");
        }

        // Vérifier si l'ICE existe déjà
        if (fournisseurRepository.existsByIce(fournisseur.getIce())) {
            throw new ResourceAlreadyExistsException("Un fournisseur avec cet ICE existe déjà");
        }

        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public Fournisseur updateFournisseur(Long id, Fournisseur fournisseur) {
        Fournisseur existingFournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id));

        // Vérifier si l'email est modifié et s'il existe déjà
        if (!existingFournisseur.getEmail().equals(fournisseur.getEmail()) &&
                fournisseurRepository.existsByEmail(fournisseur.getEmail())) {
            throw new ResourceAlreadyExistsException("Un fournisseur avec cet email existe déjà");
        }

        // Vérifier si l'ICE est modifié et s'il existe déjà
        if (!existingFournisseur.getIce().equals(fournisseur.getIce()) &&
                fournisseurRepository.existsByIce(fournisseur.getIce())) {
            throw new ResourceAlreadyExistsException("Un fournisseur avec cet ICE existe déjà");
        }

        // Mettre à jour les informations
        existingFournisseur.setSociete(fournisseur.getSociete());
        existingFournisseur.setAdresse(fournisseur.getAdresse());
        existingFournisseur.setContact(fournisseur.getContact());
        existingFournisseur.setEmail(fournisseur.getEmail());
        existingFournisseur.setTelephone(fournisseur.getTelephone());
        existingFournisseur.setVille(fournisseur.getVille());
        existingFournisseur.setIce(fournisseur.getIce());

        return fournisseurRepository.save(existingFournisseur);
    }

    @Override
    public void deleteFournisseur(Long id) {
        if (!fournisseurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id);
        }
        fournisseurRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fournisseur> getFournisseurById(Long id) {
        return fournisseurRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fournisseur> getAllFournisseursSortedByName() {
        return fournisseurRepository.findAllByOrderBySocieteAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fournisseur> getAllFournisseursSortedByVilleAndName() {
        return fournisseurRepository.findAllByOrderByVilleAscSocieteAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fournisseur> findBySociete(String societe) {
        return fournisseurRepository.findBySociete(societe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fournisseur> findByVille(String ville) {
        return fournisseurRepository.findByVille(ville);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fournisseur> findByIce(String ice) {
        return fournisseurRepository.findByIce(ice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fournisseur> searchBySociete(String societe) {
        return fournisseurRepository.findBySocieteContainingIgnoreCase(societe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fournisseur> searchFournisseurs(String keyword) {
        return fournisseurRepository.searchFournisseurs(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public long countFournisseurs() {
        return fournisseurRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByVille(String ville) {
        return fournisseurRepository.countByVille(ville);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return fournisseurRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIce(String ice) {
        return fournisseurRepository.existsByIce(ice);
    }
}
