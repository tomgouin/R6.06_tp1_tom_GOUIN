package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la classe Compte")
public class CompteTest {
    
    private Compte compte;
    private Compte compteBeneficiaire;
    private Titulaire titulaire;
    
    @Nested
    @DisplayName("Tests des constructeurs")
    class TestConstructeurs {
        
        @DisplayName("Constructeur complet")
        @Test
        void testConstructeurComplet() {
            //Given
            int numero = 12345;
            double soldeInitial = 500.0;
            titulaire = new Titulaire("Dupont", "Jean", "10 rue de Paris");
            //When
            compte = new Compte(numero, titulaire, soldeInitial, 1000.0, 1500.0);
            //Then
            assertEquals(numero, compte.getNumeroCompte());
            assertEquals(soldeInitial, compte.getSolde(), 0.001);
            assertEquals(1000.0, compte.getDecouvertMaxAutorise(), 0.001);
        }
        
        @DisplayName("Constructeur simplifié avec valeurs par défaut")
        @Test
        void testConstructeurSimple() {
            //Given
            int numero = 67890;
            titulaire = new Titulaire("Martin", "Sophie", "25 avenue");
            //When
            compte = new Compte(numero, titulaire);
            //Then
            assertEquals(numero, compte.getNumeroCompte());
            assertEquals(0.0, compte.getSolde(), 0.001);
            assertEquals(800.0, compte.getDecouvertMaxAutorise(), 0.001);
            assertEquals(1000.0, compte.getDebitMaxAutorise(), 0.001);
        }
        
        @DisplayName("Constructeur avec paramètres invalides")
        @ParameterizedTest(name = "numero={0}, solde={1}, découvert={2}, débit={3}")
        @CsvSource({
            "-1, 0, 800, 1000",     // numéro négatif
            "0, 0, 800, 1000",      // numéro zéro
            "123, 0, -100, 1000",   // découvert négatif
            "123, 0, 800, -500",    // débit négatif
            "123, -1500, 1000, 1000" // solde < -découvert
        })
        void testConstructeurInvalide(int numero, double solde, double decouvert, double debit) {
            //Given
            titulaire = new Titulaire("Test", "Test", "Adresse");
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                new Compte(numero, titulaire, solde, decouvert, debit);
            });
        }
    }
    
    @Nested
    @DisplayName("Tests des accesseurs")
    class TestGetters {
        
        @BeforeEach
        void setUp() {
            titulaire = new Titulaire("Test", "User", "Adresse");
            compte = new Compte(11111, titulaire, 1000.0, 500.0, 800.0);
        }
        
        @DisplayName("Vérification des getters")
        @Test
        void testGetters() {
            //Then
            assertEquals(11111, compte.getNumeroCompte());
            assertNotNull(compte.getTitulaire());
            assertEquals(titulaire, compte.getTitulaire());
            assertEquals("Test", compte.getNom());
            assertEquals("User", compte.getPrenom());
            assertEquals("Adresse", compte.getAdresse());
            assertEquals(1000.0, compte.getSolde(), 0.001);
            assertEquals(500.0, compte.getDecouvertMaxAutorise(), 0.001);
            assertEquals(800.0, compte.getDebitMaxAutorise(), 0.001);
        }
    }
    
    @Nested
    @DisplayName("Tests des modificateurs")
    class TestSetters {
        
        @BeforeEach
        void setUp() {
            titulaire = new Titulaire("Test", "User", "Adresse");
            compte = new Compte(22222, titulaire);
        }
        
        @DisplayName("Modification du découvert maximal")
        @Test
        void testSetDecouvertMaxAutorise() {
            //Given
            double nouveauDecouvert = 1200.0;
            //When
            compte.setDecouvertMaxAutorise(nouveauDecouvert);
            //Then
            assertEquals(nouveauDecouvert, compte.getDecouvertMaxAutorise(), 0.001);
        }
        
        @DisplayName("Modification du débit maximal")
        @Test
        void testSetDebitMaxAutorise() {
            //Given
            double nouveauDebit = 2000.0;
            //When
            compte.setDebitMaxAutorise(nouveauDebit);
            //Then
            assertEquals(nouveauDebit, compte.getDebitMaxAutorise(), 0.001);
        }
        
        @DisplayName("Modification avec valeur négative")
        @Test
        void testSetDecouvertNegatif() {
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.setDecouvertMaxAutorise(-100.0);
            });
        }
        
        @DisplayName("Modification débit avec valeur négative")
        @Test
        void testSetDebitNegatif() {
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.setDebitMaxAutorise(-500.0);
            });
        }
        
        @DisplayName("Modification avec solde incompatible")
        @Test
        void testSetDecouvertIncompatible() {
            //Given
            compte.debiter(500.0); // solde = -500
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.setDecouvertMaxAutorise(300.0);
            });
        }
    }
    
    @Nested
    @DisplayName("Tests du découvert")
    class TestDecouvert {
        
        @DisplayName("Calcul du découvert")
        @ParameterizedTest(name = "solde={0} -> découvert={1}")
        @CsvSource({
            "500, 0",      // solde positif
            "0, 0",        // solde nul
            "-300, 300",   // solde négatif
            "-800, 800"    // découvert maximal
        })
        void testCalculDecouvert(double solde, double decouvertAttendu) {
            //Given
            titulaire = new Titulaire("Test", "User", "Adresse");
            compte = new Compte(33333, titulaire, solde, 800.0, 1000.0);
            //When
            double decouvert = compte.getDecouvert();
            //Then
            assertEquals(decouvertAttendu, decouvert, 0.001);
        }
        
        @DisplayName("Vérification état à découvert")
        @Test
        void testEstADecouvert() {
            //Given
            titulaire = new Titulaire("Test", "User", "Adresse");
            compte = new Compte(33334, titulaire, -100.0, 800.0, 1000.0);
            //Then
            assertTrue(compte.estADecouvert());
        }
    }
    
    @Nested
    @DisplayName("Tests du débit autorisé")
    class TestDebitAutorise {
        
        @DisplayName("Calcul du débit autorisé")
        @ParameterizedTest(name = "solde={0}, découvert={1}, débitMax={2} -> autorisé={3}")
        @CsvSource({
            "2000, 800, 1000, 1000",   // limité par débit max
            "500, 800, 1500, 1300",    // limité par découvert
            "-300, 800, 1000, 500",    // compte à découvert
            "-800, 800, 1000, 0"       // découvert maximal atteint
        })
        void testCalculDebitAutorise(double solde, double decouvert, double debitMax, double attendu) {
            //Given
            titulaire = new Titulaire("Test", "User", "Adresse");
            compte = new Compte(44444, titulaire, solde, decouvert, debitMax);
            //When
            double debitAutorise = compte.getDebitAutorise();
            //Then
            assertEquals(attendu, debitAutorise, 0.001);
        }
    }
    
    @Nested
    @DisplayName("Tests de l'opération créditer")
    class TestCrediter {
        
        @BeforeEach
        void setUp() {
            titulaire = new Titulaire("Test", "User", "Adresse");
            compte = new Compte(55555, titulaire, 1000.0, 800.0, 1000.0);
        }
        
        @DisplayName("Créditer un montant valide")
        @Test
        void testCrediterValide() {
            //Given
            double montant = 500.0;
            //When
            compte.crediter(montant);
            //Then
            assertEquals(1500.0, compte.getSolde(), 0.001);
        }
        
        @DisplayName("Créditer avec montant invalide")
        @ParameterizedTest(name = "montant={0}")
        @CsvSource({
            "0",      // montant nul
            "-100"    // montant négatif
        })
        void testCrediterInvalide(double montant) {
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.crediter(montant);
            });
        }
    }
    
    @Nested
    @DisplayName("Tests de l'opération débiter")
    class TestDebiter {
        
        @BeforeEach
        void setUp() {
            titulaire = new Titulaire("Test", "User", "Adresse");
            compte = new Compte(66666, titulaire, 1000.0, 800.0, 2000.0);
        }
        
        @DisplayName("Débiter un montant valide")
        @Test
        void testDebiterValide() {
            //Given
            double montant = 300.0;
            //When
            compte.debiter(montant);
            //Then
            assertEquals(700.0, compte.getSolde(), 0.001);
        }
        
        @DisplayName("Débiter avec mise à découvert")
        @Test
        void testDebiterAvecDecouvert() {
            //Given
            double montant = 1200.0;
            //When
            compte.debiter(montant);
            //Then
            assertEquals(-200.0, compte.getSolde(), 0.001);
            assertTrue(compte.estADecouvert());
        }
        
        @DisplayName("Débiter avec montant invalide")
        @ParameterizedTest(name = "montant={0}")
        @CsvSource({
            "0",      // montant nul
            "-50"     // montant négatif
        })
        void testDebiterInvalide(double montant) {
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.debiter(montant);
            });
        }
        
        @DisplayName("Débiter avec dépassement de limite")
        @Test
        void testDebiterDepassementLimite() {
            //Given
            double montant = 2500.0; // > débit max (2000)
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.debiter(montant);
            });
        }
        
        @DisplayName("Débiter avec dépassement découvert")
        @Test
        void testDebiterDepassementDecouvert() {
            //Given
            double montant = 1801.0; // dépasserait découvert max
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.debiter(montant);
            });
        }
        
        @DisplayName("Débiter exactement jusqu'au découvert maximal")
        @Test
        void testDebiterDecouvertMax() {
            //Given
            double montant = 1800.0;
            //When
            compte.debiter(montant);
            //Then
            assertEquals(-800.0, compte.getSolde(), 0.001);
        }
    }
    
    @Nested
    @DisplayName("Tests de l'opération virement")
    class TestVirement {
        
        @BeforeEach
        void setUp() {
            titulaire = new Titulaire("Test", "Emetteur", "Adresse1");
            Titulaire beneficiaire = new Titulaire("Test", "Beneficiaire", "Adresse2");
            compte = new Compte(77777, titulaire, 1500.0, 800.0, 2000.0);
            compteBeneficiaire = new Compte(88888, beneficiaire, 500.0, 800.0, 1000.0);
        }
        
        @DisplayName("Virement valide")
        @Test
        void testVirementValide() {
            //Given
            double montant = 600.0;
            //When
            compte.virement(montant, compteBeneficiaire);
            //Then
            assertEquals(900.0, compte.getSolde(), 0.001);
            assertEquals(1100.0, compteBeneficiaire.getSolde(), 0.001);
        }
        
        @DisplayName("Virement avec mise à découvert")
        @Test
        void testVirementAvecDecouvert() {
            //Given
            double montant = 1800.0;
            //When
            compte.virement(montant, compteBeneficiaire);
            //Then
            assertEquals(-300.0, compte.getSolde(), 0.001);
            assertEquals(2300.0, compteBeneficiaire.getSolde(), 0.001);
        }
        
        @DisplayName("Virement avec montant invalide")
        @ParameterizedTest(name = "montant={0}")
        @CsvSource({
            "0",      // montant nul
            "-100"    // montant négatif
        })
        void testVirementMontantInvalide(double montant) {
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.virement(montant, compteBeneficiaire);
            });
        }
        
        @DisplayName("Virement avec compte bénéficiaire null")
        @Test
        void testVirementBeneficiaireNull() {
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.virement(100.0, null);
            });
        }
        
        @DisplayName("Virement impossible - dépassement limite")
        @Test
        void testVirementDepassement() {
            //Given
            double montant = 2500.0;
            //Then
            assertThrows(IllegalArgumentException.class, () -> {
                //When
                compte.virement(montant, compteBeneficiaire);
            });
        }
    }
    
    @Nested
    @DisplayName("Tests de la méthode toString")
    class TestToString {
        
        @DisplayName("toString retourne une représentation correcte")
        @Test
        void testToString() {
            //Given
            titulaire = new Titulaire("Dupont", "Jean", "10 rue de Paris");
            compte = new Compte(99999, titulaire, 1234.56, 800.0, 1000.0);
            //When
            String resultat = compte.toString();
            //Then
            assertTrue(resultat.contains("99999"));
            assertTrue(resultat.contains("Jean"));
            assertTrue(resultat.contains("Dupont"));
            assertTrue(resultat.contains("1234.56") || resultat.contains("1234,56"));
        }
    }
    
    @Nested
    @DisplayName("Tests de scénarios complexes")
    class TestScenarios {
        
        @DisplayName("Scénario complet avec multiples opérations")
        @Test
        void testScenarioComplet() {
            //Given
            titulaire = new Titulaire("Dupont", "Jean", "Paris");
            Titulaire beneficiaire = new Titulaire("Martin", "Sophie", "Lyon");
            compte = new Compte(10001, titulaire, 2000.0, 1000.0, 1500.0);
            compteBeneficiaire = new Compte(10002, beneficiaire, 500.0, 800.0, 1000.0);
            
            //When & Then
            compte.debiter(300.0);
            assertEquals(1700.0, compte.getSolde(), 0.001);
            
            compte.crediter(500.0);
            assertEquals(2200.0, compte.getSolde(), 0.001);
            
            compte.virement(1000.0, compteBeneficiaire);
            assertEquals(1200.0, compte.getSolde(), 0.001);
            assertEquals(1500.0, compteBeneficiaire.getSolde(), 0.001);
            
            compte.debiter(1500.0);
            assertEquals(-300.0, compte.getSolde(), 0.001);
            assertTrue(compte.estADecouvert());
            
            compte.crediter(400.0);
            assertEquals(100.0, compte.getSolde(), 0.001);
            assertFalse(compte.estADecouvert());
        }
    }
}
