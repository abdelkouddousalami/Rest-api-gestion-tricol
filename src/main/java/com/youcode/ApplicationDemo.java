package com.youcode;

import com.youcode.entities.Fournisseur;
import com.youcode.services.FournisseurService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.youcode.config.WebConfig;
import com.youcode.config.PersistenceConfig;

import java.util.List;

/**
 * Classe de démonstration pour tester l'application
 * Démontre l'utilisation de l'ApplicationContext et BeanFactory
 */
public class ApplicationDemo {

    public static void main(String[] args) {
        System.out.println("=== Tricol - Gestion des Fournisseurs ===");
        System.out.println("Démarrage de l'application...\n");

        // Démonstration 1: Configuration Java avec AnnotationConfigApplicationContext
        demonstrateJavaConfig();

        // Démonstration 2: Configuration XML avec ClassPathXmlApplicationContext
        // demonstrateXmlConfig();
    }

    /**
     * Démonstration de la configuration Java avec annotations
     */
    private static void demonstrateJavaConfig() {
        System.out.println("=== Configuration Java (Annotations) ===");

        // Créer le contexte Spring avec la configuration Java
        ApplicationContext context = new AnnotationConfigApplicationContext(
                WebConfig.class,
                PersistenceConfig.class
        );

        // Récupérer le service depuis le contexte (IoC Container)
        FournisseurService fournisseurService = context.getBean(FournisseurService.class);

        // Afficher le scope du bean
        System.out.println("Bean scope: " + 
            (context.isSingleton("fournisseurServiceImpl") ? "Singleton" : "Prototype"));

        // Créer un nouveau fournisseur
        System.out.println("\n1. Création d'un nouveau fournisseur...");
        Fournisseur fournisseur = new Fournisseur(
                "Textile Pro SA",
                "123 Boulevard Mohammed V",
                "Ahmed Alami",
                "contact@textilepro.ma",
                "+212600000000",
                "Casablanca",
                "001234567890123"
        );

        try {
            Fournisseur saved = fournisseurService.createFournisseur(fournisseur);
            System.out.println("✓ Fournisseur créé avec succès - ID: " + saved.getId());
        } catch (Exception e) {
            System.out.println("✗ Erreur lors de la création: " + e.getMessage());
        }

        // Récupérer tous les fournisseurs
        System.out.println("\n2. Liste de tous les fournisseurs:");
        List<Fournisseur> fournisseurs = fournisseurService.getAllFournisseurs();
        fournisseurs.forEach(f -> 
            System.out.println("   - " + f.getSociete() + " (" + f.getVille() + ")")
        );

        // Compter les fournisseurs
        System.out.println("\n3. Nombre total de fournisseurs: " + 
            fournisseurService.countFournisseurs());

        // Recherche par ville
        System.out.println("\n4. Fournisseurs à Casablanca:");
        List<Fournisseur> casaFournisseurs = fournisseurService.findByVille("Casablanca");
        casaFournisseurs.forEach(f -> 
            System.out.println("   - " + f.getSociete())
        );

        // Recherche par mot-clé
        System.out.println("\n5. Recherche 'Textile':");
        List<Fournisseur> searchResults = fournisseurService.searchFournisseurs("Textile");
        searchResults.forEach(f -> 
            System.out.println("   - " + f.getSociete())
        );

        // Fermer le contexte
        ((AnnotationConfigApplicationContext) context).close();
        System.out.println("\n✓ Application terminée avec succès!");
    }

    /**
     * Démonstration de la configuration XML
     */
    private static void demonstrateXmlConfig() {
        System.out.println("=== Configuration XML ===");

        // Créer le contexte Spring avec la configuration XML
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml"
        );

        // Récupérer le service depuis le contexte
        FournisseurService fournisseurService = 
            (FournisseurService) context.getBean("fournisseurService");

        System.out.println("Service récupéré depuis la configuration XML");
        System.out.println("Bean scope: " + 
            (context.isSingleton("fournisseurService") ? "Singleton" : "Prototype"));

        // Démonstration du scope Prototype
        Object prototypeBean1 = context.getBean("prototypeBean");
        Object prototypeBean2 = context.getBean("prototypeBean");
        System.out.println("\nDémonstration Prototype scope:");
        System.out.println("prototypeBean1 == prototypeBean2? " + 
            (prototypeBean1 == prototypeBean2) + " (devrait être false)");

        // Utiliser le service
        long count = fournisseurService.countFournisseurs();
        System.out.println("\nNombre de fournisseurs: " + count);

        // Fermer le contexte
        ((ClassPathXmlApplicationContext) context).close();
        System.out.println("\n✓ Application terminée avec succès!");
    }
}
