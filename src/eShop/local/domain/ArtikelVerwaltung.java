package eShop.local.domain;

import eShop.local.entities.Artikel;

import java.util.ArrayList;

public class ArtikelVerwaltung {
    ArrayList artikelBestand = new ArrayList();

    public void anlegen(Artikel artikel){
        if(artikelBestand.contains(artikel)){
            System.out.println("Buch existiert bereits!");
            // statt print eine Exception thrown
        }

        artikelBestand.add(artikel);
    }

    public void entfernen(Artikel artikel){
        artikelBestand.remove(artikel);
    }

    public void bestandVeraendern(Artikel artikel, int anzahl){
        // den Bestand eines Artikels verändern
        artikel.setBestand(anzahl);
    }

    public ArrayList artikelSuchen(String bezeichnung){
        ArrayList suchergebnisse = new ArrayList();

        for (Object a : artikelBestand) {
            Artikel artikel = (Artikel) a;
            if (artikel.getBezeichnung().equals(bezeichnung)) {
                suchergebnisse.add(artikel);
            }
        }
        return suchergebnisse;
    }

    public ArrayList getArtikelBestand(){
        return new ArrayList(artikelBestand);
    }
}
