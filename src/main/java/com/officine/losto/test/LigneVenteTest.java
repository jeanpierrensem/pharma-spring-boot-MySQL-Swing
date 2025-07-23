package com.officine.losto.test;

public class LigneVenteTest {
	
	String libelle;
    int quantite;
    double prixUnitaire;

    public LigneVenteTest(String libelle, int quantite, double prixUnitaire) {
        this.libelle = libelle;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public double getTotal() {
        return quantite * prixUnitaire;
    }


}
