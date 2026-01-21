package org.example;

public class Compte {
    
    private final int numeroCompte;
    private final Titulaire titulaire;
    private double solde;
    private double decouvertMaxAutorise;
    private double debitMaxAutorise;
    
    public static final double DECOUVERT_MAX_PAR_DEFAUT = 800.0;
    public static final double DEBIT_MAX_PAR_DEFAUT = 1000.0;
    
    public Compte(int numeroCompte, Titulaire titulaire,
                  double soldeInitial, double decouvertMaxAutorise, double debitMaxAutorise) {
        if (numeroCompte <= 0) {
            throw new IllegalArgumentException("Le numéro de compte doit être un entier positif");
        }
        if (decouvertMaxAutorise < 0) {
            throw new IllegalArgumentException("Le découvert maximal autorisé ne peut pas être négatif");
        }
        if (debitMaxAutorise < 0) {
            throw new IllegalArgumentException("Le débit maximal autorisé ne peut pas être négatif");
        }
        if (soldeInitial < -decouvertMaxAutorise) {
            throw new IllegalArgumentException(
                "Le solde initial ne peut pas être inférieur au découvert maximal autorisé");
        }
        
        this.numeroCompte = numeroCompte;
        this.titulaire = titulaire;
        this.solde = soldeInitial;
        this.decouvertMaxAutorise = decouvertMaxAutorise;
        this.debitMaxAutorise = debitMaxAutorise;
    }
    
    public Compte(int numeroCompte, Titulaire titulaire) {
        this(numeroCompte, titulaire, 0.0, 
             DECOUVERT_MAX_PAR_DEFAUT, DEBIT_MAX_PAR_DEFAUT);
    }
    
    public int getNumeroCompte() {
        return numeroCompte;
    }
    
    public Titulaire getTitulaire() {
        return titulaire;
    }
    
    public String getNom() {
        return titulaire.getNom();
    }
    
    public String getPrenom() {
        return titulaire.getPrenom();
    }
    
    public String getAdresse() {
        return titulaire.getAdresse();
    }
    
    public double getSolde() {
        return solde;
    }
    
    public double getDecouvertMaxAutorise() {
        return decouvertMaxAutorise;
    }
    
    public double getDebitMaxAutorise() {
        return debitMaxAutorise;
    }
    
    public void setDecouvertMaxAutorise(double decouvertMaxAutorise) {
        if (decouvertMaxAutorise < 0) {
            throw new IllegalArgumentException("Le découvert maximal autorisé ne peut pas être négatif");
        }
        if (solde < -decouvertMaxAutorise) {
            throw new IllegalArgumentException(
                "Le solde actuel est inférieur au nouveau découvert maximal autorisé");
        }
        this.decouvertMaxAutorise = decouvertMaxAutorise;
    }
    
    public void setDebitMaxAutorise(double debitMaxAutorise) {
        if (debitMaxAutorise < 0) {
            throw new IllegalArgumentException("Le débit maximal autorisé ne peut pas être négatif");
        }
        this.debitMaxAutorise = debitMaxAutorise;
    }
    
    public double getDecouvert() {
        if (solde < 0) {
            return -solde;
        }
        return 0.0;
    }
    
    public boolean estADecouvert() {
        return solde < 0;
    }
    
    public double getDebitAutorise() {
        double debitPossible = solde + decouvertMaxAutorise;
        return Math.min(debitPossible, debitMaxAutorise);
    }
    
    public void crediter(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant à créditer doit être strictement positif");
        }
        solde += montant;
    }
    
    public void debiter(double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant à débiter doit être strictement positif");
        }
        if (montant > debitMaxAutorise) {
            throw new IllegalArgumentException(
                "Le montant à débiter dépasse le débit maximal autorisé (" + debitMaxAutorise + " €)");
        }
        double soldeResultant = solde - montant;
        if (soldeResultant < -decouvertMaxAutorise) {
            throw new IllegalArgumentException(
                "Cette opération dépasserait le découvert maximal autorisé (" + decouvertMaxAutorise + " €)");
        }
        solde -= montant;
    }
    
    public void virement(double montant, Compte compteBeneficiaire) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant du virement doit être strictement positif");
        }
        if (compteBeneficiaire == null) {
            throw new IllegalArgumentException("Le compte bénéficiaire ne peut pas être null");
        }
        debiter(montant);
        compteBeneficiaire.crediter(montant);
    }
    
    @Override
    public String toString() {
        return String.format("Compte n°%d - Titulaire: %s - Solde: %.2f€", 
                           numeroCompte, titulaire, solde);
    }
}
