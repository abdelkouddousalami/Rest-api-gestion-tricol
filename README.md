# Tricol - Gestion des Fournisseurs

Application de gestion des fournisseurs pour l'entreprise Tricol, spécialisée dans la conception et la fabrication de vêtements professionnels.

## 🎯 Objectif

Développer un module robuste de gestion des fournisseurs en utilisant Spring Core, Spring MVC, Hibernate et Spring Data JPA avec une architecture permettant l'extension future du système.

## 🏗️ Architecture

### Architecture en couches
```
├── Controllers     (Couche présentation - REST API)
├── Services        (Couche métier - Logique applicative)
├── Repositories    (Couche d'accès aux données - Spring Data JPA)
└── Entities        (Modèle de données)
```

### Technologies utilisées

- **Java 17**
- **Spring Framework 6.1.14**
  - Spring Core (IoC Container, Dependency Injection)
  - Spring MVC (REST API)
  - Spring Data JPA (Persistance)
  - Spring ORM (Intégration Hibernate)
- **Hibernate 6.4.1** (ORM)
- **MySQL 8** (Base de données)
- **HikariCP** (Pool de connexions)
- **Maven** (Gestion des dépendances)

## 📋 Fonctionnalités

### Gestion des Fournisseurs

1. **Ajouter un fournisseur**
   - Société, Adresse, Contact, Email, Téléphone, Ville, ICE

2. **Modifier un fournisseur**
   - Mise à jour des informations existantes

3. **Supprimer un fournisseur**
   - Suppression d'un fournisseur du système

4. **Consulter la liste des fournisseurs**
   - Affichage de tous les fournisseurs
   - Tri par nom de société
   - Tri par ville
   - Recherche par mot-clé

## 🔌 API REST Endpoints

### Fournisseurs

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/v1/fournisseurs` | Récupérer tous les fournisseurs |
| GET | `/api/v1/fournisseurs?sortBy=nom` | Récupérer tous les fournisseurs triés par nom |
| GET | `/api/v1/fournisseurs?ville=Casablanca` | Filtrer les fournisseurs par ville |
| GET | `/api/v1/fournisseurs?search=textile` | Rechercher des fournisseurs |
| GET | `/api/v1/fournisseurs/{id}` | Récupérer un fournisseur par ID |
| POST | `/api/v1/fournisseurs` | Créer un nouveau fournisseur |
| PUT | `/api/v1/fournisseurs/{id}` | Mettre à jour un fournisseur |
| DELETE | `/api/v1/fournisseurs/{id}` | Supprimer un fournisseur |
| GET | `/api/v1/fournisseurs/count` | Compter le nombre de fournisseurs |

## 📝 Exemples de requêtes

### 1. Créer un fournisseur

```bash
POST http://localhost:8080/springmvc-rest/api/v1/fournisseurs
Content-Type: application/json

{
  "societe": "Textile Pro SA",
  "adresse": "123 Boulevard Mohammed V",
  "contact": "Ahmed Alami",
  "email": "contact@textilepro.ma",
  "telephone": "+212600000000",
  "ville": "Casablanca",
  "ice": "001234567890123"
}
```

### 2. Récupérer tous les fournisseurs

```bash
GET http://localhost:8080/springmvc-rest/api/v1/fournisseurs
```

### 3. Récupérer un fournisseur par ID

```bash
GET http://localhost:8080/springmvc-rest/api/v1/fournisseurs/1
```

### 4. Mettre à jour un fournisseur

```bash
PUT http://localhost:8080/springmvc-rest/api/v1/fournisseurs/1
Content-Type: application/json

{
  "societe": "Textile Pro SA",
  "adresse": "456 Boulevard Zerktouni",
  "contact": "Ahmed Alami",
  "email": "contact@textilepro.ma",
  "telephone": "+212600000000",
  "ville": "Casablanca",
  "ice": "001234567890123"
}
```

### 5. Supprimer un fournisseur

```bash
DELETE http://localhost:8080/springmvc-rest/api/v1/fournisseurs/1
```

## 🔧 Configuration

### Configuration Spring

Le projet utilise les **trois approches de configuration Spring** :

#### 1. Configuration Java (@Configuration)
Fichiers : `WebConfig.java`, `PersistenceConfig.java`

```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.youcode")
public class WebConfig implements WebMvcConfigurer {
    // Configuration Java
}
```

#### 2. Configuration par Annotations (@Component, @Service, @Repository)
Fichiers : Tous les composants Spring

```java
@Service
@Transactional
public class FournisseurServiceImpl implements FournisseurService {
    // Service avec annotations
}
```

#### 3. Configuration XML
Fichier : `applicationContext.xml`

```xml
<bean id="fournisseurService" 
      class="com.youcode.services.impl.FournisseurServiceImpl" 
      scope="singleton">
    <constructor-arg ref="fournisseurRepository"/>
</bean>
```

### Scopes des Beans

- **Singleton** (par défaut) : Une seule instance pour toute l'application
- **Prototype** : Une nouvelle instance à chaque injection
- Exemples dans `applicationContext.xml`

### Spring Data JPA

#### Méthodes automatiques
```java
findAll()
findById(Long id)
save(Fournisseur fournisseur)
deleteById(Long id)
count()
```

#### Query Methods personnalisées
```java
findBySociete(String societe)
findByEmailEndingWith(String emailDomain)
findByVille(String ville)
findByIce(String ice)
findBySocieteContainingIgnoreCase(String societe)
countByVille(String ville)
existsByEmail(String email)
existsByIce(String ice)
```

#### Requêtes @Query personnalisées
```java
@Query("SELECT f FROM Fournisseur f WHERE f.ville = :ville ORDER BY f.societe ASC")
List<Fournisseur> findFournisseursByVilleOrderedBySociete(@Param("ville") String ville);
```

## 🗄️ Base de données

### Configuration MySQL

Créer la base de données (ou elle sera créée automatiquement) :

```sql
CREATE DATABASE tricol_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Structure de la table `fournisseurs`

| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGINT (PK, AUTO_INCREMENT) | Identifiant unique |
| societe | VARCHAR(100) | Nom de la société |
| adresse | VARCHAR(255) | Adresse complète |
| contact | VARCHAR(100) | Nom du contact |
| email | VARCHAR(100) | Email (unique) |
| telephone | VARCHAR(20) | Numéro de téléphone |
| ville | VARCHAR(100) | Ville |
| ice | VARCHAR(15) | ICE (unique) |
| created_at | DATETIME | Date de création |
| updated_at | DATETIME | Date de modification |

### Configuration dans `application.properties`

```properties
db.url=jdbc:mysql://localhost:3306/tricol_db?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSL=false
db.username=root
db.password=
db.driver=com.mysql.cj.jdbc.Driver

hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
hibernate.hbm2ddl.auto=update
hibernate.show_sql=true
hibernate.format_sql=true
```

## 🚀 Installation et démarrage

### Prérequis

- JDK 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+
- Tomcat 10+ (ou tout serveur compatible Jakarta EE 10)

### Étapes d'installation

1. **Cloner le projet**
   ```bash
   git clone <repository-url>
   cd springmvc
   ```

2. **Configurer la base de données**
   - Démarrer MySQL
   - Modifier `src/main/resources/application.properties` si nécessaire

3. **Compiler le projet**
   ```bash
   mvn clean install
   ```

4. **Déployer sur Tomcat**
   - Copier `target/springmvc-rest.war` dans le dossier `webapps` de Tomcat
   - Ou utiliser votre IDE pour déployer directement

5. **Accéder à l'application**
   ```
   http://localhost:8080/springmvc-rest/api/v1/fournisseurs
   ```

## 📦 Structure du projet

```
springmvc/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── youcode/
│   │   │           ├── config/              # Configurations Spring
│   │   │           │   ├── WebConfig.java
│   │   │           │   └── PersistenceConfig.java
│   │   │           ├── controllers/         # REST Controllers
│   │   │           │   └── FournisseurController.java
│   │   │           ├── services/            # Services métier
│   │   │           │   ├── FournisseurService.java
│   │   │           │   └── impl/
│   │   │           │       └── FournisseurServiceImpl.java
│   │   │           ├── repositories/        # Repositories JPA
│   │   │           │   └── FournisseurRepository.java
│   │   │           ├── entities/            # Entités JPA
│   │   │           │   └── Fournisseur.java
│   │   │           ├── dto/                 # Data Transfer Objects
│   │   │           │   └── ApiResponse.java
│   │   │           └── exceptions/          # Exceptions personnalisées
│   │   │               ├── ResourceNotFoundException.java
│   │   │               └── ResourceAlreadyExistsException.java
│   │   ├── resources/
│   │   │   ├── application.properties       # Configuration application
│   │   │   └── applicationContext.xml       # Configuration XML Spring
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── web.xml                  # Configuration Servlet
│   └── test/
└── pom.xml                                   # Configuration Maven
```

## 🧪 Tests avec Postman/cURL

### Collection Postman

Importer la collection suivante pour tester l'API :

```json
{
  "info": {
    "name": "Tricol API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Get All Fournisseurs",
      "request": {
        "method": "GET",
        "url": "http://localhost:8080/springmvc-rest/api/v1/fournisseurs"
      }
    }
  ]
}
```

## 🎓 Concepts Spring mis en œuvre

### 1. IoC Container
- ApplicationContext
- BeanFactory
- Injection de dépendances par constructeur

### 2. Spring Beans
- @Component, @Service, @Repository
- @Configuration
- Bean scopes (Singleton, Prototype)

### 3. Component Scanning
- @ComponentScan
- Détection automatique des composants

### 4. Spring MVC
- @RestController, @RequestMapping
- @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
- @PathVariable, @RequestParam, @RequestBody

### 5. Spring Data JPA
- JpaRepository
- Query Methods
- @Query personnalisées
- @Transactional

### 6. Validation
- @Valid
- Bean Validation (@NotBlank, @Email, @Pattern, etc.)

## 📈 Extensions futures

- Gestion des produits
- Gestion des commandes
- Gestion des stocks
- Authentification et autorisation
- Pagination et filtres avancés
- Export de données (PDF, Excel)
- Tableau de bord analytique

## 👨‍💻 Auteur

Projet développé pour l'entreprise Tricol

## 📄 Licence

Ce projet est destiné à un usage interne de l'entreprise Tricol.
