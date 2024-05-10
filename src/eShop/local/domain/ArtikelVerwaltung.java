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
