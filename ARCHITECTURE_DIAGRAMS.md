# Architecture Tricol - Diagrammes

## 1. Architecture Globale

```
┌─────────────────────────────────────────────────────────────┐
│                        CLIENT (Browser/Postman)              │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTP Requests (JSON)
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    SPRING MVC (Web Layer)                    │
│  ┌──────────────────────────────────────────────────────┐  │
│  │           FournisseurController                       │  │
│  │   @RestController @RequestMapping                     │  │
│  │   ├─ GET    /api/v1/fournisseurs                     │  │
│  │   ├─ GET    /api/v1/fournisseurs/{id}               │  │
│  │   ├─ POST   /api/v1/fournisseurs                     │  │
│  │   ├─ PUT    /api/v1/fournisseurs/{id}               │  │
│  │   └─ DELETE /api/v1/fournisseurs/{id}               │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                 SERVICE LAYER (Business Logic)               │
│  ┌──────────────────────────────────────────────────────┐  │
│  │        FournisseurServiceImpl                         │  │
│  │   @Service @Transactional                            │  │
│  │   ├─ createFournisseur()                             │  │
│  │   ├─ updateFournisseur()                             │  │
│  │   ├─ deleteFournisseur()                             │  │
│  │   ├─ getFournisseurById()                            │  │
│  │   ├─ getAllFournisseurs()                            │  │
│  │   └─ searchFournisseurs()                            │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              REPOSITORY LAYER (Data Access)                  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │        FournisseurRepository                          │  │
│  │   @Repository extends JpaRepository                   │  │
│  │   ├─ findAll()                                       │  │
│  │   ├─ findById()                                      │  │
│  │   ├─ save()                                          │  │
│  │   ├─ delete()                                        │  │
│  │   ├─ findBySociete()          [Query Method]        │  │
│  │   ├─ findByVille()             [Query Method]        │  │
│  │   └─ searchFournisseurs()     [@Query]              │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              HIBERNATE (ORM) / Spring Data JPA               │
│                   Entity Manager / Session                   │
└────────────────────────┬────────────────────────────────────┘
                         │ SQL Queries
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    MySQL Database                            │
│                  Table: fournisseurs                         │
└─────────────────────────────────────────────────────────────┘
```

## 2. Flux de Données - Création d'un Fournisseur

```
1. Client (Postman)
   │
   │ POST /api/v1/fournisseurs
   │ Body: { "societe": "...", "email": "...", ... }
   │
   ▼
2. FournisseurController
   │
   │ @PostMapping
   │ @Valid @RequestBody Fournisseur
   │
   ├─ Validation (Bean Validation)
   │  └─ @NotBlank, @Email, @Pattern, etc.
   │
   ▼
3. FournisseurServiceImpl
   │
   │ @Transactional
   │ createFournisseur(fournisseur)
   │
   ├─ Vérification email unique
   │  └─ repository.existsByEmail()
   │
   ├─ Vérification ICE unique
   │  └─ repository.existsByIce()
   │
   ├─ Si erreur → ResourceAlreadyExistsException
   │
   ▼
4. FournisseurRepository
   │
   │ save(fournisseur)
   │
   ▼
5. Spring Data JPA / Hibernate
   │
   │ Génération SQL
   │ INSERT INTO fournisseurs (...)
   │
   ├─ @PrePersist callback
   │  └─ createdAt = now()
   │  └─ updatedAt = now()
   │
   ▼
6. MySQL Database
   │
   │ Enregistrement dans la table
   │ Attribution de l'ID auto-incrémenté
   │
   ▼
7. Retour au Client
   │
   └─ Response 201 Created
      {
        "success": true,
        "message": "Fournisseur créé avec succès",
        "data": { id: 1, societe: "...", ... }
      }
```

## 3. Configuration Spring - Les 3 Approches

```
┌─────────────────────────────────────────────────────────────┐
│               SPRING IoC CONTAINER                           │
│                 (ApplicationContext)                         │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │  1. CONFIGURATION JAVA (@Configuration)            │    │
│  │                                                     │    │
│  │  WebConfig.java                                    │    │
│  │  ├─ @Configuration                                 │    │
│  │  ├─ @EnableWebMvc                                  │    │
│  │  ├─ @ComponentScan                                 │    │
│  │  └─ @Bean objectMapper()                           │    │
│  │                                                     │    │
│  │  PersistenceConfig.java                            │    │
│  │  ├─ @Configuration                                 │    │
│  │  ├─ @EnableJpaRepositories                         │    │
│  │  ├─ @Bean dataSource()                             │    │
│  │  ├─ @Bean entityManagerFactory()                   │    │
│  │  └─ @Bean transactionManager()                     │    │
│  └────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │  2. CONFIGURATION ANNOTATIONS                       │    │
│  │                                                     │    │
│  │  @Service                                          │    │
│  │  ├─ FournisseurServiceImpl (Singleton)            │    │
│  │  └─ @Autowired constructor injection              │    │
│  │                                                     │    │
│  │  @Repository                                       │    │
│  │  └─ FournisseurRepository (Proxy JPA)             │    │
│  │                                                     │    │
│  │  @RestController                                   │    │
│  │  └─ FournisseurController (Singleton)             │    │
│  │                                                     │    │
│  │  @Entity                                           │    │
│  │  └─ Fournisseur (not a bean, managed by JPA)      │    │
│  └────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │  3. CONFIGURATION XML                               │    │
│  │                                                     │    │
│  │  applicationContext.xml                            │    │
│  │  ├─ <context:component-scan>                       │    │
│  │  ├─ <tx:annotation-driven>                         │    │
│  │  ├─ <jpa:repositories>                             │    │
│  │  ├─ <bean id="dataSource">                         │    │
│  │  └─ <bean id="fournisseurService" scope="singleton">│   │
│  └────────────────────────────────────────────────────┘    │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

## 4. Spring Beans Scopes

```
┌──────────────────────────────────────────────────────────────┐
│                     BEAN SCOPES                              │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  SINGLETON (par défaut)                                     │
│  ┌────────────────────────────────────────────────┐        │
│  │  Une seule instance par ApplicationContext     │        │
│  │                                                 │        │
│  │  ApplicationContext.getBean("service")         │        │
│  │         │                                       │        │
│  │         ├─── Instance A ◄─────┐               │        │
│  │         │                      │               │        │
│  │  ApplicationContext.getBean("service")         │        │
│  │         │                      │               │        │
│  │         └─── Instance A ◄─────┘               │        │
│  │              (même instance)                    │        │
│  └────────────────────────────────────────────────┘        │
│                                                              │
│  PROTOTYPE                                                  │
│  ┌────────────────────────────────────────────────┐        │
│  │  Nouvelle instance à chaque injection          │        │
│  │                                                 │        │
│  │  ApplicationContext.getBean("prototype")       │        │
│  │         │                                       │        │
│  │         ├─── Instance A                        │        │
│  │         │                                       │        │
│  │  ApplicationContext.getBean("prototype")       │        │
│  │         │                                       │        │
│  │         └─── Instance B                        │        │
│  │              (nouvelle instance)                │        │
│  └────────────────────────────────────────────────┘        │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

## 5. Spring Data JPA - Query Methods

```
┌──────────────────────────────────────────────────────────────┐
│           SPRING DATA JPA QUERY METHODS                      │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  Interface Repository                                        │
│  ┌────────────────────────────────────────────────┐        │
│  │  public interface FournisseurRepository        │        │
│  │      extends JpaRepository<Fournisseur, Long> │        │
│  │  {                                              │        │
│  │      // Spring génère automatiquement          │        │
│  │      // l'implémentation à partir du nom       │        │
│  │                                                 │        │
│  │      findBySociete(String societe)             │        │
│  │         ↓                                       │        │
│  │      SELECT f FROM Fournisseur f                │        │
│  │      WHERE f.societe = :societe                │        │
│  │                                                 │        │
│  │      findByVille(String ville)                 │        │
│  │         ↓                                       │        │
│  │      SELECT f FROM Fournisseur f                │        │
│  │      WHERE f.ville = :ville                    │        │
│  │                                                 │        │
│  │      findBySocieteContainingIgnoreCase(...)    │        │
│  │         ↓                                       │        │
│  │      SELECT f FROM Fournisseur f                │        │
│  │      WHERE LOWER(f.societe) LIKE LOWER(...)    │        │
│  │                                                 │        │
│  │      @Query("SELECT f FROM Fournisseur f ...")  │        │
│  │      searchFournisseurs(String keyword)        │        │
│  │         ↓                                       │        │
│  │      Requête JPQL personnalisée                │        │
│  │  }                                              │        │
│  └────────────────────────────────────────────────┘        │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

## 6. Transaction Management

```
┌──────────────────────────────────────────────────────────────┐
│                  TRANSACTION FLOW                            │
└──────────────────────────────────────────────────────────────┘

@Service
@Transactional  ← Toute la classe est transactionnelle
public class FournisseurServiceImpl {

    @Override
    public Fournisseur createFournisseur(Fournisseur f) {
        
        ┌─────────────────────────────────────────┐
        │ BEGIN TRANSACTION                       │ ← Automatique
        └─────────────────────────────────────────┘
        
        // Vérifications
        if (existsByEmail(f.getEmail())) {
            throw new ResourceAlreadyExistsException(...);
            
            ┌─────────────────────────────────────────┐
            │ ROLLBACK                                │ ← Exception
            └─────────────────────────────────────────┘
        }
        
        // Sauvegarde
        Fournisseur saved = repository.save(f);
        
        ┌─────────────────────────────────────────┐
        │ COMMIT                                  │ ← Succès
        └─────────────────────────────────────────┘
        
        return saved;
    }
}
```

## 7. Structure de la Base de Données

```
┌─────────────────────────────────────────────────────────────┐
│                    TABLE: fournisseurs                       │
├──────────────┬──────────────┬──────────────┬───────────────┤
│ Colonne      │ Type         │ Contrainte   │ Index         │
├──────────────┼──────────────┼──────────────┼───────────────┤
│ id           │ BIGINT       │ PRIMARY KEY  │ AUTO_INCREMENT│
│ societe      │ VARCHAR(100) │ NOT NULL     │ INDEX         │
│ adresse      │ VARCHAR(255) │ NOT NULL     │               │
│ contact      │ VARCHAR(100) │ NOT NULL     │               │
│ email        │ VARCHAR(100) │ UNIQUE       │ INDEX         │
│ telephone    │ VARCHAR(20)  │ NOT NULL     │               │
│ ville        │ VARCHAR(100) │ NOT NULL     │ INDEX         │
│ ice          │ VARCHAR(15)  │ UNIQUE       │ INDEX         │
│ created_at   │ DATETIME     │              │               │
│ updated_at   │ DATETIME     │              │               │
└──────────────┴──────────────┴──────────────┴───────────────┘
```

## 8. REST API Response Format

```
┌──────────────────────────────────────────────────────────────┐
│                   API RESPONSE FORMAT                        │
└──────────────────────────────────────────────────────────────┘

SUCCESS Response:
{
  "success": true,
  "message": "Fournisseur créé avec succès",
  "data": {
    "id": 1,
    "societe": "Textile Pro SA",
    "adresse": "123 Boulevard Mohammed V",
    "contact": "Ahmed Alami",
    "email": "contact@textilepro.ma",
    "telephone": "+212600000000",
    "ville": "Casablanca",
    "ice": "001234567890123",
    "createdAt": "2025-10-23T10:30:00",
    "updatedAt": "2025-10-23T10:30:00"
  },
  "errors": null
}

ERROR Response (Validation):
{
  "success": false,
  "message": "Erreurs de validation",
  "data": null,
  "errors": {
    "email": "L'email doit être valide",
    "ice": "L'ICE doit contenir exactement 15 chiffres"
  }
}

ERROR Response (Not Found):
{
  "success": false,
  "message": "Fournisseur non trouvé avec l'ID: 99",
  "data": null,
  "errors": null
}
```

## Légende

- `│` : Séparation verticale
- `├─` : Branchement
- `└─` : Fin de branche
- `▼` : Flux descendant
- `◄─` : Référence/Lien
- `@` : Annotation Spring
