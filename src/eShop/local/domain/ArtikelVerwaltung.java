package eShop.local.domain;

import java.util.ArrayList;

import eShop.local.domain.exceptions.ArtikelExistiertBereitsException;
import eShop.local.domain.exceptions.ArtikelExistiertNichtException;
import eShop.local.entities.Artikel;

/** in der Artikel-Verwaltung sollte
 * Artikel Hinzufügen, Artikel Löschen, Artikel Aktualisieren und Artikel Suchen platziert werden.
 * */
public class ArtikelVerwaltung {
    ArrayList<Artikel> artikelBestand = new ArrayList<Artikel>();
    public ArtikelVerwaltung(){
        Artikel a1 = new Artikel(1, "Banane", 20, 1.99F);
        Artikel a2 = new Artikel(2, "Apfel", 13, 1.30F);
        Artikel a3 = new Artikel(3, "Birne", 7, 0.99F);
        artikelBestand.add(a1);
        artikelBestand.add(a2);
        artikelBestand.add(a3);
    }

    public void artikelHinzufuegen(Artikel artikel) throws ArtikelExistiertBereitsException {
        if(artikelBestand.contains(artikel)) {
            throw new ArtikelExistiertBereitsException();
        }

        artikelBestand.add(artikel);
    }

    public int bestandAendern(int artikel_nummer, int neuer_bestand) throws ArtikelExistiertNichtException {
        for (Artikel bestand_item : artikelBestand){
            if (bestand_item.getArtikelnummer() == artikel_nummer){
                bestand_item.setBestand(neuer_bestand);
                return bestand_item.getBestand();
            }
        }
        // Falls kein Artikel mit der Nummer gefunden wird
        throw new ArtikelExistiertNichtException();
    }
    // Methode zum Löschen des Artikel aus der Liste.
    public void artikelEntfernen(int nummer, String bezeichnung) {
        artikelBestand.removeIf(a -> a.getArtikelnummer() == nummer && a.getBezeichnung().equals(bezeichnung));
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
