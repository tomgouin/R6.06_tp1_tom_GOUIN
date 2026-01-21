# TP 1 - Module JUnit 5 : Comptes Bancaires
## Récapitulatif de réalisation

---

## ✅ Question 1 : Spécification de la classe Compte

### Fichier créé
- `app/src/main/java/org/example/Compte.java`

### Attributs définis
1. **Attributs finals (immuables)**
   - `numeroCompte` (int) : Numéro unique du compte
   - `nom` (String) : Nom du titulaire
   - `prenom` (String) : Prénom du titulaire
   - `adresse` (String) : Adresse du titulaire

2. **Attributs modifiables**
   - `solde` (double) : Solde actuel en euros
   - `decouvertMaxAutorise` (double) : Découvert maximal en euros
   - `debitMaxAutorise` (double) : Débit maximal autorisé en euros

3. **Constantes**
   - `DECOUVERT_MAX_PAR_DEFAUT = 800.0`
   - `DEBIT_MAX_PAR_DEFAUT = 1000.0`

### Constructeurs
1. **Constructeur complet** : 7 paramètres avec validations
2. **Constructeur simplifié** : 4 paramètres (utilise valeurs par défaut)

### Méthodes publiques (18 au total)
- **Getters** : 7 méthodes (numeroCompte, nom, prenom, adresse, solde, decouvertMaxAutorise, debitMaxAutorise)
- **Setters** : 2 méthodes (setDecouvertMaxAutorise, setDebitMaxAutorise)
- **Métier** : 9 méthodes (getDecouvert, estADecouvert, getDebitAutorise, crediter, debiter, virement, toString)

### Documentation
✅ Javadoc complète pour chaque méthode et constructeur

---

## ✅ Question 2 : Tests JUnit 5

### Fichier créé
- `app/src/test/java/org/example/CompteTest.java`

### Organisation des tests
Tests organisés en **9 classes imbriquées** (@Nested) :

1. **TestConstructeurs** (7 tests)
   - Construction valide (2 tests)
   - Validations des paramètres invalides (5 tests)

2. **TestGetters** (7 tests)
   - Vérification de tous les accesseurs

3. **TestSetters** (5 tests)
   - Modification des limites avec validations

4. **TestDecouvert** (4 tests)
   - Calcul du découvert selon le solde
   - Vérification de l'état du compte

5. **TestDebitAutorise** (5 tests)
   - Calcul du débit autorisé selon différentes situations

6. **TestCrediter** (4 tests)
   - Opérations de crédit valides et invalides

7. **TestDebiter** (9 tests)
   - Débits valides et limites
   - Exceptions pour opérations invalides

8. **TestVirement** (9 tests)
   - Virements valides et invalides
   - Virement avec découvert
   - Validations des paramètres

9. **TestScenariosComplexes** (2 tests)
   - Scénarios complets multi-opérations
   - Modification des limites en cours d'utilisation

### Annotations JUnit 5 utilisées
- `@DisplayName` : Noms descriptifs pour chaque test
- `@Nested` : Organisation hiérarchique des tests
- `@BeforeEach` : Initialisation avant chaque test
- `@Test` : Marquage des méthodes de test

### Assertions utilisées
- `assertEquals()` : Vérification des valeurs
- `assertTrue()` / `assertFalse()` : Vérification des booléens
- `assertThrows()` : Vérification des exceptions

### Total : **52 tests unitaires**

---

## ✅ Question 3 : Implémentation

### Implémentation complète
Toutes les méthodes de la classe `Compte` ont été implémentées avec :
- Validations des paramètres
- Levée d'exceptions `IllegalArgumentException` pour opérations invalides
- Logique métier correcte

### Règles métier implémentées
1. ✅ Numéro de compte > 0
2. ✅ Découvert et débit max ≥ 0
3. ✅ Solde initial ≥ -découvert max
4. ✅ Montants d'opérations > 0
5. ✅ Débit ≤ débit maximal autorisé
6. ✅ Solde après débit ≥ -découvert maximal
7. ✅ Compte bénéficiaire ≠ null

### Résultats
✅ **52/52 tests passent avec succès**

---

## ✅ Question 4 : Couverture de code

### Configuration Jacoco
- Plugin Jacoco ajouté dans `build.gradle`
- Génération automatique après les tests
- Rapports XML et HTML configurés

### Rapports générés
1. **Rapport HTML** : `app/build/reports/jacoco/test/html/index.html`
2. **Rapport XML** : `app/build/reports/jacoco/test/jacocoTestReport.xml`

### Couverture obtenue pour la classe Compte

| Métrique            | Couverture        | Détails      |
|---------------------|-------------------|--------------|
| **Instructions**    | **100%** ✅       | 270/270      |
| **Branches**        | **100%** ✅       | 30/30        |
| **Lignes**          | **100%** ✅       | 64/64        |
| **Méthodes**        | **100%** ✅       | 18/18        |
| **Complexité**      | **100%** ✅       | 33/33        |

### Analyse
🎯 **Couverture parfaite : 100% sur tous les critères**

Chaque ligne de code, chaque branche conditionnelle et chaque méthode sont testées.

---

## 📋 Commandes Gradle

```bash
# Compiler le projet
gradlew.bat build

# Exécuter les tests
gradlew.bat test

# Générer le rapport de couverture
gradlew.bat jacocoTestReport

# Tout exécuter
gradlew.bat clean test jacocoTestReport
```

---

## 📦 Fichiers à rendre

### Structure du projet (sans dossier build)
```
tp_compte/
├── app/
│   ├── src/
│   │   ├── main/java/org/example/
│   │   │   └── Compte.java
│   │   └── test/java/org/example/
│   │       └── CompteTest.java
│   └── build.gradle
├── gradle/
│   └── libs.versions.toml
├── .gitignore
├── README.md
├── RECAPITULATIF_TP.md (ce fichier)
├── settings.gradle
├── gradlew
└── gradlew.bat
```

### Fichiers à exclure
- Dossier `build/` (généré automatiquement)
- Dossier `.gradle/` (cache Gradle)

---

## 🎓 Compétences validées

### JUnit 5
✅ Écriture de tests unitaires  
✅ Organisation avec @Nested  
✅ Utilisation des assertions  
✅ Tests d'exceptions  
✅ @BeforeEach pour l'initialisation  
✅ @DisplayName pour la lisibilité  

### Conception objet
✅ Attributs finals (immuabilité)  
✅ Encapsulation (private + getters/setters)  
✅ Constructeurs multiples  
✅ Validations des données  
✅ Gestion des exceptions  
✅ Documentation Javadoc  

### Gradle
✅ Configuration du projet  
✅ Gestion des dépendances  
✅ Configuration JUnit 5  
✅ Configuration Jacoco  
✅ Automatisation des rapports  

### Qualité de code
✅ Couverture 100%  
✅ Tests exhaustifs  
✅ Gestion des cas limites  
✅ Messages d'erreur explicites  

---

## 📊 Statistiques finales

- **Classe principale** : 1 (Compte)
- **Lignes de code** : ~340 (classe + tests)
- **Tests unitaires** : 52
- **Taux de réussite** : 100% (52/52)
- **Couverture** : 100%
- **Temps de test** : ~3 secondes

---

## ✨ Points remarquables

1. **Couverture exceptionnelle** : 100% sur tous les critères
2. **Tests exhaustifs** : Tous les cas (valides, limites, invalides) testés
3. **Organisation claire** : Tests groupés par fonctionnalité
4. **Documentation complète** : Javadoc + README + ce récapitulatif
5. **Règles métier respectées** : Toutes les contraintes du cahier des charges implémentées

---

**TP réalisé avec succès ! 🎉**

Module JUnit 5  
Université du Mans  
Date : Janvier 2026
