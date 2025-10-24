# 🚀 Quick Start Guide - Tricol Application

## Démarrage Rapide en 5 Minutes

### Étape 1: Vérifier les prérequis (1 minute)

```bash
# Vérifier Java
java -version
# Doit afficher: Java 17 ou supérieur

# Vérifier Maven
mvn -version
# Doit afficher: Maven 3.6 ou supérieur

# Vérifier MySQL
mysql --version
# Doit afficher: MySQL 8.0 ou supérieur
```

### Étape 2: Configurer MySQL (1 minute)

```bash
# Démarrer MySQL
# Windows: Ouvrir MySQL Workbench ou services
# Linux: sudo systemctl start mysql
# Mac: brew services start mysql

# Se connecter à MySQL
mysql -u root -p

# Créer la base de données
CREATE DATABASE tricol_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EXIT;
```

**OU** Utiliser le script fourni:
```bash
mysql -u root -p < database/init_database.sql
```

### Étape 3: Configurer l'application (30 secondes)

Éditer `src/main/resources/application.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/tricol_db?createDatabaseIfNotExist=true&serverTimezone=UTC
db.username=root
db.password=VOTRE_MOT_DE_PASSE_ICI
```

### Étape 4: Compiler le projet (1 minute)

```bash
# Se positionner dans le dossier du projet
cd springmvc

# Compiler avec Maven
mvn clean install
```

Vous devriez voir: **BUILD SUCCESS**

### Étape 5: Déployer sur Tomcat (2 minutes)

#### Option A: Déploiement manuel

```bash
# Copier le WAR dans Tomcat
cp target/springmvc-rest.war C:/path/to/tomcat/webapps/

# Démarrer Tomcat
cd C:/path/to/tomcat/bin
startup.bat  # Windows
./startup.sh # Linux/Mac
```

#### Option B: Avec IntelliJ IDEA

1. Cliquer sur **Add Configuration**
2. Sélectionner **Tomcat Server** > **Local**
3. Configurer Tomcat Home
4. Dans **Deployment**, ajouter **springmvc:war exploded**
5. Cliquer sur **Run** ▶️

#### Option C: Avec Eclipse

1. Clic droit sur le projet > **Run As** > **Run on Server**
2. Sélectionner Tomcat
3. Cliquer sur **Finish**

### Étape 6: Tester l'application (30 secondes)

Ouvrir un navigateur ou utiliser cURL:

```bash
# Test 1: Récupérer tous les fournisseurs
curl http://localhost:8080/springmvc-rest/api/v1/fournisseurs

# Test 2: Créer un fournisseur
curl -X POST http://localhost:8080/springmvc-rest/api/v1/fournisseurs \
  -H "Content-Type: application/json" \
  -d '{
    "societe": "Test Textile",
    "adresse": "123 Rue Test",
    "contact": "Ahmed Test",
    "email": "test@example.com",
    "telephone": "+212600000000",
    "ville": "Casablanca",
    "ice": "001234567890999"
  }'
```

## ✅ Résultat Attendu

Si tout fonctionne, vous devriez voir une réponse JSON:

```json
{
  "success": true,
  "message": "Fournisseur créé avec succès",
  "data": {
    "id": 1,
    "societe": "Test Textile",
    "adresse": "123 Rue Test",
    "contact": "Ahmed Test",
    "email": "test@example.com",
    "telephone": "+212600000000",
    "ville": "Casablanca",
    "ice": "001234567890999",
    "createdAt": "2025-10-23T...",
    "updatedAt": "2025-10-23T..."
  },
  "errors": null
}
```

## 🧪 Tester avec Postman

1. **Ouvrir Postman**
2. **Importer la collection**: `File` > `Import` > Sélectionner `Tricol_API_Collection.postman_collection.json`
3. **Modifier la variable** `baseUrl` si nécessaire
4. **Exécuter les requêtes** 🚀

## 🎯 Endpoints Disponibles

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `http://localhost:8080/springmvc-rest/api/v1/fournisseurs` | Liste tous |
| GET | `http://localhost:8080/springmvc-rest/api/v1/fournisseurs?sortBy=nom` | Tri par nom |
| GET | `http://localhost:8080/springmvc-rest/api/v1/fournisseurs/{id}` | Un fournisseur |
| POST | `http://localhost:8080/springmvc-rest/api/v1/fournisseurs` | Créer |
| PUT | `http://localhost:8080/springmvc-rest/api/v1/fournisseurs/{id}` | Modifier |
| DELETE | `http://localhost:8080/springmvc-rest/api/v1/fournisseurs/{id}` | Supprimer |

## 🐛 Problèmes Courants

### Erreur: Port 8080 déjà utilisé
```bash
# Windows: Trouver et tuer le processus
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill
```

### Erreur: Connection refused (MySQL)
```bash
# Vérifier que MySQL est démarré
# Windows: services.msc > MySQL
# Linux: sudo systemctl status mysql
# Mac: brew services list
```

### Erreur: Access denied for user
- Vérifier `db.username` et `db.password` dans `application.properties`
- Vérifier les permissions MySQL:
```sql
GRANT ALL PRIVILEGES ON tricol_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

### Erreur: 404 Not Found
- Vérifier que l'URL contient bien `/springmvc-rest/`
- Vérifier que Tomcat est démarré
- Vérifier les logs Tomcat: `tomcat/logs/catalina.out`

## 📚 Documentation Complète

- **README.md** - Documentation principale
- **GUIDE_COMPLET.md** - Guide détaillé avec tous les concepts
- **PROJET_COMPLET.md** - Résumé technique complet

## 🎓 Prochaines Étapes

1. Explorer la collection Postman
2. Lire le code source avec les commentaires
3. Modifier et étendre l'application
4. Consulter GUIDE_COMPLET.md pour comprendre les concepts

## 💡 Astuces

- Utiliser **Postman** pour tester facilement
- Activer **hibernate.show_sql=true** pour voir les requêtes SQL
- Consulter les logs Tomcat en cas d'erreur
- Le script SQL insère 8 fournisseurs de test automatiquement

## ✨ Félicitations!

Votre application Tricol est maintenant opérationnelle! 🎉

Pour plus d'informations, consultez la documentation complète.
