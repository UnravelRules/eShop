package eShop.local.domain;

import eShop.local.domain.exceptions.ArtikelExistiertBereitsException;
import eShop.local.domain.exceptions.ArtikelExistiertNichtException;
import eShop.local.domain.exceptions.MassengutException;
import eShop.local.entities.Artikel;
import eShop.local.entities.Massengutartikel;
import eShop.local.persistence.FilePersistenceManager;
import eShop.local.persistence.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;

/** in der Artikel-Verwaltung sollte
 * Artikel Hinzufügen, Artikel Löschen, Artikel Aktualisieren und Artikel Suchen platziert werden.
 * */
public class ArtikelVerwaltung {
    ArrayList<Artikel> artikelBestand = new ArrayList<Artikel>();

    private PersistenceManager pm = new FilePersistenceManager();

    public void liesDaten(String datei) throws IOException {
        pm.openForReading(datei);
        Artikel einArtikel;

        do {
            einArtikel = pm.ladeArtikel();
            if(einArtikel != null) {
                try{
                    artikelHinzufuegen(einArtikel);
                } catch (ArtikelExistiertBereitsException e) {
                    // ...
                }
            }
        } while (einArtikel != null);
        pm.close();
    }

    public void schreibeDaten(String datei) throws IOException {
        pm.openForWriting(datei);

        for (Artikel artikel : artikelBestand){
            pm.speichereArtikel(artikel);
        }

        pm.close();
    }

    /**
     * Fügt einen neuen Artikel dem Bestand hinzu.
     * @param artikel
     * @throws ArtikelExistiertBereitsException
     */
    public void artikelHinzufuegen(Artikel artikel) throws ArtikelExistiertBereitsException {
        if(artikelBestand.contains(artikel)) {
            throw new ArtikelExistiertBereitsException();
        }

        artikelBestand.add(artikel);
    }

    public void massengutartikelHinzufuegen(Massengutartikel massengutartikel) throws ArtikelExistiertBereitsException {
        if(artikelBestand.contains(massengutartikel)){
            throw new ArtikelExistiertBereitsException();
        }

        artikelBestand.add(massengutartikel);
    }

    /**
     * Methode zum Aendern des Bestands eines Artikels
     * @param artikel_nummer
     * @param neuer_bestand
     * @return neuer_bestand
     * @throws ArtikelExistiertNichtException
     */
    public int bestandAendern(int artikel_nummer, int neuer_bestand) throws ArtikelExistiertNichtException, MassengutException {
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

    public Artikel getArtikelMitBezeichnung(String bezeichnung) throws ArtikelExistiertNichtException{
        for(Artikel bestand_item : artikelBestand){
            if(bestand_item.getBezeichnung().equals(bezeichnung)){
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
