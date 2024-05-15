package eShop.local.domain;

import eShop.local.domain.exceptions.ArtikelExistiertBereitsException;
import eShop.local.domain.exceptions.ArtikelExistiertNichtException;
import eShop.local.entities.Artikel;

import java.util.ArrayList;

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

    /**
     * Fügt einene neune Artikel dem Bestand hinzu.
     * @param artikel
     * @throws ArtikelExistiertBereitsException
     */
    public void artikelHinzufuegen(Artikel artikel) throws ArtikelExistiertBereitsException {
        if(artikelBestand.contains(artikel)) {
            throw new ArtikelExistiertBereitsException();
        }

        artikelBestand.add(artikel);
    }

    /**
     * Methode zum Aendern des Bestands eines Artikels
     * @param artikel_nummer
     * @param neuer_bestand
     * @return neuer_bestand
     * @throws ArtikelExistiertNichtException
     */
    public int bestandAendern(int artikel_nummer, int neuer_bestand) throws ArtikelExistiertNichtException {
        Artikel bestanditem = getArtikelMitNummer(artikel_nummer);
        bestanditem.setBestand(neuer_bestand);
        return neuer_bestand;
    }

    /**
     * Methode zum Entfernen eines Artikels
     * @param nummer
     * @param bezeichnung
     */
    public void artikelEntfernen(int nummer, String bezeichnung) {
        artikelBestand.removeIf(a -> a.getArtikelnummer() == nummer && a.getBezeichnung().equals(bezeichnung));
    }

    /**
     * Methode zum Suchen von Artikeln mit gleicher Bezeichnung
     * Gibt eine Arrayliste von Artikel zurück, dessen Beschreibung zutrifft.
     * @param bezeichnung
     * @return ArrayList<Artikel> suchergebnisse
     */
    public ArrayList<Artikel> artikelSuchen(String bezeichnung){
        ArrayList<Artikel> suchergebnisse = new ArrayList<Artikel>();

        for (Artikel a : artikelBestand) {
            if (a.getBezeichnung().equals(bezeichnung)) {
                suchergebnisse.add(a);
            }
        }
        return suchergebnisse;
    }

    /**
     * Sucht im Bestand nach dem ersten Objekt mit der Artikelnummer und gibt ein Artikelobjekt zurück
     * @param artikelNummer
     * @return Artikelobjekt
     * @throws ArtikelExistiertNichtException
     */
    public Artikel getArtikelMitNummer(int artikelNummer) throws ArtikelExistiertNichtException {
        for(Artikel bestand_item : artikelBestand){
            if(bestand_item.getArtikelnummer() == artikelNummer) {
                return bestand_item;
            }
        }
        throw new ArtikelExistiertNichtException();
    }

    /**
     * Gibt den Bestand der Artikelverwaltung als Arraylist<Artikel> zurück
     * @return Artikelbestand
     */
    public ArrayList<Artikel> getArtikelBestand(){
        return new ArrayList<>(artikelBestand);
    }
}
