package eShop.local.domain;

import eShop.local.entities.Artikel;

import java.util.ArrayList;

public class ArtikelVerwaltung {
    ArrayList<Artikel> artikelBestand = new ArrayList<Artikel>();

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

    public ArrayList<Artikel> artikelSuchen(String bezeichnung){
        ArrayList<Artikel> suchergebnisse = new ArrayList<Artikel>();

        for (Artikel a : artikelBestand) {
            if (a.getBezeichnung().equals(bezeichnung)) {
                suchergebnisse.add(a);
            }
        }
        return suchergebnisse;
    }

    public ArrayList<Artikel> getArtikelBestand(){
        return new ArrayList<Artikel>(artikelBestand);
    }
}
