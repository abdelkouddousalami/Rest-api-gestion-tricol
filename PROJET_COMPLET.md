# 🎯 Tricol - Application de Gestion des Fournisseurs

## ✅ Résumé du Projet

J'ai créé une **application Java Spring complète** pour la gestion des fournisseurs de l'entreprise Tricol, incluant toutes les fonctionnalités demandées et utilisant les technologies suivantes:

### 🛠️ Technologies Implémentées

- ✅ **Java 17**
- ✅ **Spring Core 6.1.14** (IoC Container, Dependency Injection)
- ✅ **Spring MVC** (REST API)
- ✅ **Spring Data JPA** (Persistance)
- ✅ **Hibernate 6.4.1** (ORM)
- ✅ **MySQL 8** (Base de données)
- ✅ **HikariCP** (Connection pooling)
- ✅ **Bean Validation** (Validation des données)
- ✅ **Jackson** (Sérialisation JSON)

## 📁 Structure du Projet

```
springmvc/
├── src/main/java/com/youcode/
│   ├── config/                          # Configurations Spring
│   │   ├── WebConfig.java              # Config Spring MVC (Java)
│   │   └── PersistenceConfig.java      # Config JPA/Hibernate (Java)
│   │
│   ├── entities/                        # Entités JPA
│   │   └── Fournisseur.java            # Entité Fournisseur avec validations
│   │
│   ├── repositories/                    # Couche d'accès aux données
│   │   └── FournisseurRepository.java  # Repository Spring Data JPA
│   │
│   ├── services/                        # Logique métier
│   │   ├── FournisseurService.java     # Interface Service
│   │   └── impl/
│   │       └── FournisseurServiceImpl.java  # Implémentation Service
│   │
│   ├── controllers/                     # Contrôleurs REST
│   │   └── FournisseurController.java  # REST API Controller
│   │
│   ├── dto/                            # Data Transfer Objects
│   │   └── ApiResponse.java           # Format de réponse standardisé
│   │
│   ├── exceptions/                      # Exceptions personnalisées
│   │   ├── ResourceNotFoundException.java
│   │   └── ResourceAlreadyExistsException.java
│   │
│   └── ApplicationDemo.java            # Classe de démonstration
│
├── src/main/resources/
│   ├── application.properties          # Configuration application
│   └── applicationContext.xml          # Configuration Spring XML
│
├── src/main/webapp/
│   └── WEB-INF/
│       └── web.xml                     # Configuration Servlet
│
├── database/
│   └── init_database.sql              # Script d'initialisation DB
│
├── pom.xml                             # Configuration Maven
├── README.md                           # Documentation principale
├── GUIDE_COMPLET.md                    # Guide détaillé
└── Tricol_API_Collection.postman_collection.json  # Collection Postman
```

## ✨ Fonctionnalités Implémentées

### 1. Gestion des Fournisseurs (CRUD Complet)

✅ **Créer un fournisseur** (`POST /api/v1/fournisseurs`)
- Validation complète des données
- Vérification de l'unicité de l'email et ICE
- Génération automatique des timestamps

✅ **Modifier un fournisseur** (`PUT /api/v1/fournisseurs/{id}`)
- Mise à jour avec validation
- Vérification de l'existence
- Gestion des conflits

✅ **Supprimer un fournisseur** (`DELETE /api/v1/fournisseurs/{id}`)
- Suppression sécurisée
- Vérification de l'existence

✅ **Consulter la liste des fournisseurs** (`GET /api/v1/fournisseurs`)
- Liste complète
- **Tri par nom** (`?sortBy=nom`)
- **Tri par ville** (`?sortBy=ville`)
- **Filtrage par ville** (`?ville=Casablanca`)
- **Recherche par mot-clé** (`?search=textile`)

✅ **Consulter un fournisseur** (`GET /api/v1/fournisseurs/{id}`)

✅ **Compter les fournisseurs** (`GET /api/v1/fournisseurs/count`)
- Total global
- Par ville (`?ville=Casablanca`)

### 2. Validation des Données

Chaque champ est validé avec Bean Validation:

- **société**: Obligatoire, 2-100 caractères
- **adresse**: Obligatoire, max 255 caractères
- **contact**: Obligatoire, max 100 caractères
- **email**: Obligatoire, format email valide, unique
- **téléphone**: Obligatoire, pattern `^\\+?[0-9]{10,15}$`
- **ville**: Obligatoire, max 100 caractères
- **ice**: Obligatoire, exactement 15 chiffres, unique

## 🏗️ Architecture en Couches

### Couche 1: Controllers (Présentation)
```java
@RestController
@RequestMapping("/api/v1/fournisseurs")
public class FournisseurController {
    // Gestion des requêtes HTTP
    // Validation des entrées
    // Gestion des exceptions
}
```

### Couche 2: Services (Logique Métier)
```java
@Service
@Transactional
public class FournisseurServiceImpl implements FournisseurService {
    // Logique applicative
    // Règles métier
    // Transactions
}
```

### Couche 3: Repositories (Accès aux Données)
```java
@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    // Méthodes CRUD automatiques
    // Query Methods personnalisées
    // Requêtes @Query
}
```

### Couche 4: Entities (Modèle)
```java
@Entity
@Table(name = "fournisseurs")
public class Fournisseur {
    // Mapping objet-relationnel
    // Validations
    // Lifecycle callbacks
}
```

## 🔧 Configuration Spring - Les 3 Approches

### 1️⃣ Configuration Java (`@Configuration`)

**WebConfig.java** - Configuration Spring MVC
```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.youcode")
@Import(PersistenceConfig.class)
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ObjectMapper objectMapper() { ... }
}
```

**PersistenceConfig.java** - Configuration JPA/Hibernate
```java
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.youcode.repositories")
@PropertySource("classpath:application.properties")
public class PersistenceConfig {
    @Bean public DataSource dataSource() { ... }
    @Bean public LocalContainerEntityManagerFactoryBean entityManagerFactory() { ... }
    @Bean public PlatformTransactionManager transactionManager() { ... }
}
```

### 2️⃣ Configuration par Annotations

**Stéréotypes Spring:**
```java
@Service        // Services métier
@Repository     // Repositories
@RestController // Controllers REST
@Configuration  // Classes de configuration
```

**Component Scanning:**
```java
@ComponentScan(basePackages = "com.youcode")
```

**Injection de dépendances:**
```java
@Autowired  // Injection par constructeur (recommandé)
@Value      // Injection de valeurs depuis properties
```

### 3️⃣ Configuration XML

**applicationContext.xml**
```xml
<beans>
    <context:component-scan base-package="com.youcode"/>
    <tx:annotation-driven transaction-manager="transactionManager"/>
    <jpa:repositories base-package="com.youcode.repositories"/>
    
    <bean id="fournisseurService" 
          class="com.youcode.services.impl.FournisseurServiceImpl" 
          scope="singleton"/>
</beans>
```

## 📊 Spring Data JPA - Query Methods

### Méthodes Automatiques (héritées de JpaRepository)
```java
findAll()               // Liste tous
findById(Long id)       // Cherche par ID
save(T entity)          // Crée/Met à jour
deleteById(Long id)     // Supprime
count()                 // Compte
existsById(Long id)     // Vérifie l'existence
```

### Query Methods Personnalisées (Génération Automatique)
```java
// Recherche simple
findBySociete(String societe)
findByVille(String ville)
findByIce(String ice)

// Recherche avec conditions
findByEmailEndingWith(String domain)
findBySocieteContainingIgnoreCase(String societe)

// Tri
findAllByOrderBySocieteAsc()
findAllByOrderByVilleAscSocieteAsc()

// Comptage et existence
countByVille(String ville)
existsByEmail(String email)
existsByIce(String ice)
```

### Requêtes @Query Personnalisées
```java
@Query("SELECT f FROM Fournisseur f WHERE f.ville = :ville ORDER BY f.societe")
List<Fournisseur> findFournisseursByVilleOrderedBySociete(@Param("ville") String ville);

@Query("SELECT f FROM Fournisseur f WHERE " +
       "LOWER(f.societe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(f.ville) LIKE LOWER(CONCAT('%', :keyword, '%'))")
List<Fournisseur> searchFournisseurs(@Param("keyword") String keyword);
```

## 🎯 Concepts Spring Démontrés

### IoC Container & Dependency Injection
✅ ApplicationContext
✅ BeanFactory
✅ Constructor-based DI
✅ Field injection avec @Autowired

### Spring Beans
✅ @Component, @Service, @Repository, @Controller
✅ @Configuration et @Bean
✅ Component Scanning
✅ Bean Scopes (Singleton, Prototype)

### Spring MVC
✅ @RestController
✅ @RequestMapping, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
✅ @PathVariable, @RequestParam, @RequestBody
✅ @Valid pour la validation
✅ @ExceptionHandler pour la gestion d'erreurs
✅ ResponseEntity pour les réponses HTTP

### Spring Data JPA
✅ JpaRepository
✅ Query Methods (findByX, countByX, existsByX)
✅ @Query personnalisées
✅ @Transactional

### Configuration
✅ Java Config (@Configuration)
✅ Annotation Config (@Component, @Service, etc.)
✅ XML Config (applicationContext.xml)
✅ Property files (application.properties)

## 🔌 API REST - Endpoints Complets

| Méthode | Endpoint | Description | Paramètres |
|---------|----------|-------------|------------|
| GET | `/api/v1/fournisseurs` | Liste tous les fournisseurs | `?sortBy=nom/ville`, `?ville=X`, `?search=X` |
| GET | `/api/v1/fournisseurs/{id}` | Récupère un fournisseur | - |
| POST | `/api/v1/fournisseurs` | Crée un fournisseur | Body: JSON |
| PUT | `/api/v1/fournisseurs/{id}` | Met à jour un fournisseur | Body: JSON |
| DELETE | `/api/v1/fournisseurs/{id}` | Supprime un fournisseur | - |
| GET | `/api/v1/fournisseurs/count` | Compte les fournisseurs | `?ville=X` (optionnel) |

## 📝 Exemple d'Utilisation

### Créer un fournisseur
```bash
curl -X POST http://localhost:8080/springmvc-rest/api/v1/fournisseurs \
  -H "Content-Type: application/json" \
  -d '{
    "societe": "Textile Pro SA",
    "adresse": "123 Boulevard Mohammed V",
    "contact": "Ahmed Alami",
    "email": "contact@textilepro.ma",
    "telephone": "+212600000000",
    "ville": "Casablanca",
    "ice": "001234567890123"
  }'
```

### Réponse
```json
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
```

## 🗄️ Base de Données

### Table: fournisseurs
```sql
CREATE TABLE fournisseurs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    societe VARCHAR(100) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    contact VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telephone VARCHAR(20) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    ice VARCHAR(15) NOT NULL UNIQUE,
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_societe (societe),
    INDEX idx_ville (ville),
    INDEX idx_ice (ice),
    INDEX idx_email (email)
);
```

## 📦 Fichiers de Configuration

### pom.xml
- Toutes les dépendances Maven configurées
- Spring Framework 6.1.14
- Spring Data JPA 3.2.0
- Hibernate 6.4.1
- MySQL Connector
- Validation API

### application.properties
```properties
db.url=jdbc:mysql://localhost:3306/tricol_db?createDatabaseIfNotExist=true
db.username=root
db.password=
hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
hibernate.hbm2ddl.auto=update
hibernate.show_sql=true
```

### web.xml
- Configuration du DispatcherServlet
- Mapping des URLs
- Context configuration

## 🧪 Tests

### Collection Postman Incluse
- 12 requêtes pré-configurées
- Variables d'environnement
- Tests pour tous les endpoints
- Documentation intégrée

### Script SQL de test
- Création de la base de données
- Insertion de 8 fournisseurs de démonstration
- Requêtes de test

## 📚 Documentation

### README.md
- Vue d'ensemble du projet
- Guide d'installation
- Documentation de l'API
- Exemples d'utilisation

### GUIDE_COMPLET.md
- Guide détaillé complet
- Explication des concepts Spring
- Architecture détaillée
- Résolution des problèmes

### Ce fichier (PROJET_COMPLET.md)
- Résumé technique complet
- Structure du projet
- Fonctionnalités implémentées

## 🚀 Comment Démarrer

1. **Prérequis**
   - JDK 17+
   - Maven 3.6+
   - MySQL 8.0+
   - Tomcat 10+

2. **Configuration**
   ```bash
   # Créer la base de données
   mysql -u root -p < database/init_database.sql
   
   # Modifier application.properties si nécessaire
   ```

3. **Compilation**
   ```bash
   mvn clean install
   ```

4. **Déploiement**
   ```bash
   # Copier le WAR dans Tomcat
   cp target/springmvc-rest.war $TOMCAT_HOME/webapps/
   
   # Démarrer Tomcat
   $TOMCAT_HOME/bin/startup.sh
   ```

5. **Test**
   ```bash
   curl http://localhost:8080/springmvc-rest/api/v1/fournisseurs
   ```

## ✅ Conformité aux Exigences

### Exigences Fonctionnelles ✅
- [x] Ajouter un fournisseur avec tous les champs requis
- [x] Modifier un fournisseur existant
- [x] Supprimer un fournisseur
- [x] Consulter la liste des fournisseurs
- [x] Tri par nom
- [x] Filtrage et recherche

### Exigences Techniques ✅
- [x] Java avec Spring Core
- [x] Conteneur IoC
- [x] Spring beans et scopes
- [x] ApplicationContext et BeanFactory
- [x] Configuration XML, Annotations et Java Config
- [x] Component Scanning
- [x] Spring MVC
- [x] Architecture en couches (Repository, Service, Controller)
- [x] Spring Data JPA
- [x] Méthodes automatiques (findAll, findById, count, etc.)
- [x] Query Methods personnalisées (findByNom, findByEmailEndingWith, etc.)
- [x] API REST complète avec tous les endpoints demandés

## 🎓 Points Forts du Projet

1. **Architecture robuste** - Séparation claire des responsabilités
2. **Code propre** - Respect des conventions Java et Spring
3. **Validation complète** - Bean Validation sur toutes les entités
4. **Gestion d'erreurs** - ExceptionHandler personnalisés
5. **Documentation exhaustive** - README, Guide, Collection Postman
6. **Extensibilité** - Architecture permettant l'ajout de nouvelles fonctionnalités
7. **Best practices** - Utilisation des patterns reconnus
8. **Tests prêts** - Scripts SQL et Collection Postman inclus

## 🔮 Extensions Futures Possibles

- Authentification et autorisation (Spring Security)
- Pagination des résultats
- Gestion des produits
- Gestion des commandes
- Gestion des stocks
- Export de données (PDF, Excel)
- Tableau de bord analytique
- API de recherche avancée

---

## 📞 Support

Pour toute question sur le projet, consulter:
- **README.md** - Documentation principale
- **GUIDE_COMPLET.md** - Guide détaillé
- **Code source** - Commentaires dans le code

---

**Projet développé pour Tricol - Entreprise de conception et fabrication de vêtements professionnels** 🏭👔
