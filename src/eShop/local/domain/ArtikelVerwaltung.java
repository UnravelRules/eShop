package eShop.local.domain;

import java.io.IOException;
import java.util.ArrayList;

import eShop.local.entities.Artikel;
import eShop.local.entities.Mitarbeiter;
import eShop.local.entities.Kunde;

/** in der Artikel-Verwaltung sollte
 * Artikel Hinzufügen, Artikel Löschen, Artikel Aktualisieren und Artikel Suchen platziert werden.
 * */
public class ArtikelVerwaltung {
    ArrayList<Artikel> artikelBestand = new ArrayList<Artikel>();

    public void artikelHinzufuegen(Artikel artikel) {
        if(artikelBestand.contains(artikel)) {
            System.out.println("Artikel existiert bereits!");
            // statt print eine Exception thrown
        }

        artikelBestand.add(artikel);
    }


    // Methode zum Löschen des Artikel aus der Liste.
    public void artikelEntfernen(Artikel artikel) {
        artikelBestand.remove(artikel);
    }

    // Methode zum Update des Artikels in der Liste.
    public void artikelAktualisieren(Artikel artikel, int neuerBestand, String neueBezeichnung) {

/**den Bestand eines Artikels verändern oder aktualisieren
 * ich konnte noch nicht lösen */

/*        for (artikel : artikelBestand) {
            if (artikel.getArtikelnummer() == artikel.getArtikelnummer)
                artikel.setBestand(neuerBestand);
            artikel.setBezeichnung(neueBezeichnung);
        }*/


    }

    // Methode zum Suchen einen Artikel in der Liste.
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
