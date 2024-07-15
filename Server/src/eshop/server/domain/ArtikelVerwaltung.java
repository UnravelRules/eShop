package eshop.server.domain;

import eShop.common.exceptions.ArtikelExistiertBereitsException;
import eShop.common.exceptions.ArtikelExistiertNichtException;
import eShop.common.exceptions.MassengutException;
import eShop.common.entities.Artikel;
import eShop.common.entities.Massengutartikel;
import eshop.server.persistence.FilePersistenceManager;
import eshop.server.persistence.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;

/** in der Artikel-Verwaltung sollte
 * Artikel Hinzufügen, Artikel Löschen, Artikel Aktualisieren und Artikel Suchen platziert werden.
 * */
public class ArtikelVerwaltung {
    ArrayList<Artikel> artikelBestand = new ArrayList<Artikel>();

    private PersistenceManager pm = new FilePersistenceManager();

    /**
     * Liest die Daten aus einer Datei
     * @param datei Datei
     * @throws IOException Fehler beim Lesen
     */
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

    /**
     * Speichert die Daten in einer Datei
     * @param datei Datei
     * @throws IOException Fehler beim Schreiben
     */
    public void schreibeDaten(String datei) throws IOException {
        pm.openForWriting(datei);

        for (Artikel artikel : artikelBestand){
            pm.speichereArtikel(artikel);
        }

        pm.close();
    }

    /**
     * Fügt einen neuen Artikel dem Bestand hinzu.
     * @param artikel Artikel
     * @throws ArtikelExistiertBereitsException Artikel existiert bereits
     */
    public void artikelHinzufuegen(Artikel artikel) throws ArtikelExistiertBereitsException {
        if(artikelBestand.contains(artikel)) {
            throw new ArtikelExistiertBereitsException(artikel.getArtikelnummer(), artikel.getBezeichnung());
        }
        artikelBestand.add(artikel);
    }

    /**
     * Fügt einen Massengutartikel hinzu
     * @param massengutartikel Massengutartikel
     * @throws ArtikelExistiertBereitsException Artikel existiert bereits
     */
    public void massengutartikelHinzufuegen(Massengutartikel massengutartikel) throws ArtikelExistiertBereitsException {
        if(artikelBestand.contains(massengutartikel)){
            throw new ArtikelExistiertBereitsException(massengutartikel.getArtikelnummer(), massengutartikel.getBezeichnung());
        }

        artikelBestand.add(massengutartikel);
    }

    /**
     * Methode zum Aendern des Bestands eines Artikels
     * @param artikel_nummer Artikelnummer
     * @param neuer_bestand Neuer Bestand
     * @return neuer_bestand
     * @throws ArtikelExistiertNichtException Artikel wurde nicht gefunden
     */
    public int bestandAendern(int artikel_nummer, int neuer_bestand) throws ArtikelExistiertNichtException, MassengutException {
        Artikel bestanditem = getArtikelMitNummer(artikel_nummer);
        bestanditem.setBestand(neuer_bestand);
        return neuer_bestand;
    }

    /**
     * Methode zum Entfernen eines Artikels
     * @param nummer Artikelnummer
     * @param bezeichnung Bezeichnung
     */
    public void artikelEntfernen(int nummer, String bezeichnung) {
        artikelBestand.removeIf(a -> a.getArtikelnummer() == nummer && a.getBezeichnung().equals(bezeichnung));
    }

    /**
     * Methode zum Suchen von Artikeln mit gleicher Bezeichnung
     * Gibt eine Arrayliste von Artikel zurück, dessen Beschreibung zutrifft.
     * @param bezeichnung Bezeichnung
     * @return ArrayList suchergebnisse
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
     * @param artikelNummer Artikelnummer
     * @return Artikelobjekt
     * @throws ArtikelExistiertNichtException Artikel konnte nicht gefunden werden
     */
    public Artikel getArtikelMitNummer(int artikelNummer) throws ArtikelExistiertNichtException {
        for(Artikel bestand_item : artikelBestand){
            if(bestand_item.getArtikelnummer() == artikelNummer) {
                return bestand_item;
            }
        }
        throw new ArtikelExistiertNichtException(Integer.toString(artikelNummer));
    }

    /**
     * Findet ein Artikel anhand der Bezeichnung
     * @param bezeichnung Bezeichnung
     * @return Artikel
     * @throws ArtikelExistiertNichtException Artikel konnte nicht gefunden werden
     */
    public Artikel getArtikelMitBezeichnung(String bezeichnung) throws ArtikelExistiertNichtException{
        for(Artikel bestand_item : artikelBestand){
            if(bestand_item.getBezeichnung().equals(bezeichnung)){
                return bestand_item;
            }
        }
        throw new ArtikelExistiertNichtException(bezeichnung);
    }

    /**
     * Gibt den Bestand der Artikelverwaltung als Arraylist zurück
     * @return Artikelbestand
     */
    public ArrayList<Artikel> getArtikelBestand(){
        return new ArrayList<>(artikelBestand);
    }
}
