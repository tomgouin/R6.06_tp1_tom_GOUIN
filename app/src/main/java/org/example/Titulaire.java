package org.example;

public class Titulaire {
    
    private final String nom;
    private final String prenom;
    private final String adresse;
    
    public Titulaire(String nom, String prenom, String adresse) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
    }
    
    public String getNom() {
        return nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}
