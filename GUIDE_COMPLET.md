# Guide Complet - Tricol Application de Gestion des Fournisseurs

## Table des matières
1. [Vue d'ensemble](#vue-densemble)
2. [Configuration requise](#configuration-requise)
3. [Installation](#installation)
4. [Configuration Spring](#configuration-spring)
5. [Architecture détaillée](#architecture-détaillée)
6. [Utilisation de l'API](#utilisation-de-lapi)
7. [Spring Data JPA - Query Methods](#spring-data-jpa---query-methods)
8. [Résolution des problèmes](#résolution-des-problèmes)

## Vue d'ensemble

Cette application est développée pour **Tricol**, une entreprise spécialisée dans la conception et la fabrication de vêtements professionnels. Le système permet une gestion complète des fournisseurs.

### Fonctionnalités principales
- ✅ Création de fournisseurs
- ✅ Modification de fournisseurs
- ✅ Suppression de fournisseurs
- ✅ Consultation de la liste des fournisseurs
- ✅ Tri et recherche avancée
- ✅ Validation des données
- ✅ Gestion des erreurs

## Configuration requise

### Logiciels nécessaires
- **JDK 17** ou supérieur
- **Apache Maven 3.6+**
- **MySQL 8.0+**
- **Apache Tomcat 10+** ou tout serveur compatible Jakarta EE 10
- **Postman** (optionnel, pour tester l'API)

### Dépendances Maven principales
```xml
<!-- Spring Framework -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>6.1.14</version>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-jpa</artifactId>
    <version>3.2.0</version>
</dependency>

<!-- Hibernate ORM -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.1.Final</version>
</dependency>
```

## Installation

### Étape 1: Cloner le projet
```bash
git clone <repository-url>
cd springmvc
```

### Étape 2: Configurer MySQL
1. Démarrer le serveur MySQL
2. Exécuter le script SQL:
```bash
mysql -u root -p < database/init_database.sql
```

Ou créer manuellement la base de données:
```sql
CREATE DATABASE tricol_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Étape 3: Configurer application.properties
Modifier `src/main/resources/application.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/tricol_db?createDatabaseIfNotExist=true&serverTimezone=UTC
db.username=root
db.password=VOTRE_MOT_DE_PASSE
```

### Étape 4: Compiler le projet
```bash
mvn clean install
```

### Étape 5: Déployer sur Tomcat

#### Option A: Déploiement manuel
```bash
# Copier le WAR dans Tomcat
cp target/springmvc-rest.war /path/to/tomcat/webapps/

# Démarrer Tomcat
cd /path/to/tomcat/bin
./startup.sh  # Linux/Mac
startup.bat   # Windows
```

#### Option B: Déploiement avec IDE
- Configurer Tomcat dans votre IDE (IntelliJ IDEA, Eclipse)
- Déployer le projet directement depuis l'IDE

### Étape 6: Tester l'installation
```bash
curl http://localhost:8080/springmvc-rest/api/v1/fournisseurs
```

## Configuration Spring

### 1. Configuration Java (@Configuration)

Le projet utilise principalement la **configuration Java** avec annotations:

#### WebConfig.java
```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.youcode")
@Import(PersistenceConfig.class)
public class WebConfig implements WebMvcConfigurer {
    // Configuration Spring MVC
}
```

**Concepts démontrés:**
- `@Configuration`: Indique que la classe définit des beans Spring
- `@EnableWebMvc`: Active Spring MVC
- `@ComponentScan`: Scanne les packages pour détecter les composants
- `@Import`: Importe d'autres configurations
- `WebMvcConfigurer`: Interface pour personnaliser Spring MVC

#### PersistenceConfig.java
```java
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.youcode.repositories")
@PropertySource("classpath:application.properties")
public class PersistenceConfig {
    // Configuration JPA et Hibernate
}
```

**Concepts démontrés:**
- `@EnableTransactionManagement`: Active la gestion des transactions
- `@EnableJpaRepositories`: Active Spring Data JPA repositories
- `@PropertySource`: Charge les propriétés externes
- `@Value`: Injection de valeurs depuis les propriétés

### 2. Configuration par Annotations

#### Services
```java
@Service
@Transactional
public class FournisseurServiceImpl implements FournisseurService {
    private final FournisseurRepository repository;
    
    @Autowired
    public FournisseurServiceImpl(FournisseurRepository repository) {
        this.repository = repository;
    }
}
```

**Concepts démontrés:**
- `@Service`: Marque la classe comme service Spring
- `@Transactional`: Gère les transactions automatiquement
- `@Autowired`: Injection de dépendances par constructeur

#### Repositories
```java
@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    // Spring Data JPA génère l'implémentation automatiquement
}
```

**Concepts démontrés:**
- `@Repository`: Marque l'interface comme repository Spring
- `JpaRepository`: Fournit des méthodes CRUD automatiques

#### Controllers
```java
@RestController
@RequestMapping("/api/v1/fournisseurs")
public class FournisseurController {
    @GetMapping
    public ResponseEntity<ApiResponse<List<Fournisseur>>> getAllFournisseurs() {
        // ...
    }
}
```

**Concepts démontrés:**
- `@RestController`: Combine @Controller et @ResponseBody
- `@RequestMapping`: Définit le chemin de base
- `@GetMapping`, `@PostMapping`, etc.: Mapping des requêtes HTTP

### 3. Configuration XML

Le projet inclut également une **configuration XML** (`applicationContext.xml`):

```xml
<beans>
    <!-- Component scanning -->
    <context:component-scan base-package="com.youcode"/>
    
    <!-- Transaction management -->
    <tx:annotation-driven transaction-manager="transactionManager"/>
    
    <!-- JPA repositories -->
    <jpa:repositories base-package="com.youcode.repositories"/>
    
    <!-- DataSource bean -->
    <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
        <property name="jdbcUrl" value="${db.url}"/>
        <!-- ... -->
    </bean>
    
    <!-- Bean avec scope Singleton -->
    <bean id="fournisseurService" 
          class="com.youcode.services.impl.FournisseurServiceImpl" 
          scope="singleton"/>
    
    <!-- Bean avec scope Prototype -->
    <bean id="prototypeBean" 
          class="com.youcode.services.impl.FournisseurServiceImpl" 
          scope="prototype"/>
</beans>
```

## Architecture détaillée

### Couche Entity (Modèle)

```java
@Entity
@Table(name = "fournisseurs")
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "La société est obligatoire")
    @Size(min = 2, max = 100)
    private String societe;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

**Concepts JPA:**
- `@Entity`: Marque la classe comme entité JPA
- `@Table`: Spécifie le nom de la table
- `@Id`: Définit la clé primaire
- `@GeneratedValue`: Stratégie de génération de l'ID
- `@Column`: Personnalise les colonnes
- `@PrePersist`, `@PreUpdate`: Callbacks du cycle de vie

### Couche Repository

```java
@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    // Méthodes générées automatiquement par Spring Data JPA
    // findAll(), findById(), save(), delete(), count(), etc.
    
    // Query Methods personnalisées
    Optional<Fournisseur> findBySociete(String societe);
    List<Fournisseur> findByVille(String ville);
    List<Fournisseur> findBySocieteContainingIgnoreCase(String societe);
    
    // Requêtes @Query personnalisées
    @Query("SELECT f FROM Fournisseur f WHERE f.ville = :ville ORDER BY f.societe")
    List<Fournisseur> findFournisseursByVilleOrderedBySociete(@Param("ville") String ville);
}
```

### Couche Service

```java
@Service
@Transactional
public class FournisseurServiceImpl implements FournisseurService {
    private final FournisseurRepository repository;
    
    @Override
    public Fournisseur createFournisseur(Fournisseur fournisseur) {
        // Validation métier
        if (repository.existsByEmail(fournisseur.getEmail())) {
            throw new ResourceAlreadyExistsException("Email déjà utilisé");
        }
        return repository.save(fournisseur);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Fournisseur> getAllFournisseurs() {
        return repository.findAll();
    }
}
```

### Couche Controller

```java
@RestController
@RequestMapping("/api/v1/fournisseurs")
public class FournisseurController {
    
    @PostMapping
    public ResponseEntity<ApiResponse<Fournisseur>> createFournisseur(
            @Valid @RequestBody Fournisseur fournisseur,
            BindingResult bindingResult) {
        // Validation et traitement
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(
            ResourceNotFoundException ex) {
        // Gestion des exceptions
    }
}
```

## Utilisation de l'API

### Exemples avec cURL

#### 1. Créer un fournisseur
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

#### 2. Récupérer tous les fournisseurs
```bash
curl http://localhost:8080/springmvc-rest/api/v1/fournisseurs
```

#### 3. Récupérer avec tri
```bash
curl "http://localhost:8080/springmvc-rest/api/v1/fournisseurs?sortBy=nom"
```

#### 4. Filtrer par ville
```bash
curl "http://localhost:8080/springmvc-rest/api/v1/fournisseurs?ville=Casablanca"
```

#### 5. Rechercher
```bash
curl "http://localhost:8080/springmvc-rest/api/v1/fournisseurs?search=textile"
```

#### 6. Récupérer par ID
```bash
curl http://localhost:8080/springmvc-rest/api/v1/fournisseurs/1
```

#### 7. Mettre à jour
```bash
curl -X PUT http://localhost:8080/springmvc-rest/api/v1/fournisseurs/1 \
  -H "Content-Type: application/json" \
  -d '{
    "societe": "Textile Pro SA Updated",
    "adresse": "456 Boulevard Zerktouni",
    "contact": "Ahmed Alami",
    "email": "contact@textilepro.ma",
    "telephone": "+212600000000",
    "ville": "Casablanca",
    "ice": "001234567890123"
  }'
```

#### 8. Supprimer
```bash
curl -X DELETE http://localhost:8080/springmvc-rest/api/v1/fournisseurs/1
```

### Format de réponse

Toutes les réponses suivent ce format:
```json
{
  "success": true,
  "message": "Message descriptif",
  "data": { ... },
  "errors": null
}
```

## Spring Data JPA - Query Methods

### Méthodes automatiques (héritées de JpaRepository)
```java
// CRUD de base
findAll()                    // Récupère tous les enregistrements
findById(Long id)           // Récupère par ID
save(Fournisseur f)         // Crée ou met à jour
deleteById(Long id)         // Supprime par ID
delete(Fournisseur f)       // Supprime l'entité
count()                     // Compte tous les enregistrements
existsById(Long id)         // Vérifie l'existence
```

### Query Methods personnalisées

Spring Data JPA génère automatiquement l'implémentation à partir du nom de la méthode:

```java
// Recherche simple
findBySociete(String societe)
findByVille(String ville)
findByIce(String ice)

// Recherche avec conditions
findByEmailEndingWith(String domain)      // WHERE email LIKE '%@domain'
findBySocieteContaining(String text)      // WHERE societe LIKE '%text%'
findBySocieteContainingIgnoreCase(...)    // Insensible à la casse

// Comptage
countByVille(String ville)                // COUNT(*) WHERE ville = ?

// Vérification d'existence
existsByEmail(String email)               // EXISTS WHERE email = ?
existsByIce(String ice)

// Tri
findAllByOrderBySocieteAsc()              // ORDER BY societe ASC
findAllByOrderByVilleAscSocieteAsc()      // ORDER BY ville, societe
```

### Requêtes @Query personnalisées

Pour des requêtes plus complexes:

```java
@Query("SELECT f FROM Fournisseur f WHERE f.ville = :ville ORDER BY f.societe")
List<Fournisseur> findByVilleOrdered(@Param("ville") String ville);

@Query("SELECT f FROM Fournisseur f WHERE " +
       "LOWER(f.societe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
       "LOWER(f.ville) LIKE LOWER(CONCAT('%', :keyword, '%'))")
List<Fournisseur> search(@Param("keyword") String keyword);
```

## Résolution des problèmes

### Problème: Erreur de connexion à MySQL
**Solution:**
```properties
# Vérifier application.properties
db.url=jdbc:mysql://localhost:3306/tricol_db?createDatabaseIfNotExist=true
db.username=root
db.password=votre_mot_de_passe
```

### Problème: Tomcat ne démarre pas
**Solution:**
- Vérifier que le port 8080 n'est pas déjà utilisé
- Vérifier les logs: `tomcat/logs/catalina.out`

### Problème: Erreur 404 sur les endpoints
**Solution:**
- URL correcte: `http://localhost:8080/springmvc-rest/api/v1/fournisseurs`
- Vérifier que le WAR est bien déployé dans `webapps`

### Problème: Erreur de validation
**Solution:**
Les champs suivants sont obligatoires:
- société (2-100 caractères)
- adresse
- contact
- email (format valide)
- téléphone (10-15 chiffres)
- ville
- ice (15 chiffres exactement)

### Problème: Conflit (409) lors de la création
**Solution:**
- Vérifier que l'email n'existe pas déjà
- Vérifier que l'ICE n'existe pas déjà

## Tests avec Postman

Importer la collection: `Tricol_API_Collection.postman_collection.json`

La collection contient:
- 12 requêtes pré-configurées
- Variables d'environnement
- Tests automatiques
- Documentation intégrée

## Conclusion

Cette application démontre:
- ✅ Spring Core (IoC, DI, Beans, Scopes)
- ✅ Spring MVC (REST Controllers)
- ✅ Spring Data JPA (Repositories, Query Methods)
- ✅ Hibernate (ORM, Persistance)
- ✅ Configuration Spring (Java, Annotations, XML)
- ✅ Architecture en couches
- ✅ Validation des données
- ✅ Gestion des exceptions
- ✅ API REST complète

Pour plus d'informations, consulter la documentation officielle:
- [Spring Framework](https://spring.io/projects/spring-framework)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Hibernate](https://hibernate.org/orm/)
