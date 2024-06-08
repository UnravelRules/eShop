package eShop.local.entities;

import eShop.local.domain.exceptions.MassengutException;

/** die Klasse Artikel steht für einen Artikel im EShop
 * jeden Artikel hat eine Artikel-Nr, Bezeichnung, Bestand und Preis
  */
public class Artikel {
    // Attribute eines Artikels

    protected int artikelnummer;
    protected String bezeichnung;
    protected int bestand;
    protected float preis;

    public Artikel(int artikelNr, String bezeichnung, int bestand, float preis) {
        this.artikelnummer = artikelNr;
        this.bezeichnung = bezeichnung;
        this.bestand = bestand;
        this.preis = preis;
    }

    /**
     * Gibt einen String mit Artikelnummer, Bezeichnung, Bestand und Preis zurück
     * @return String
     */
    public String toString(){
        return String.format("Nr: " + artikelnummer + " / Bezeichnung: " + bezeichnung + " / Bestand: " + bestand + " Stk. / Preis: %.2f€", preis);
    }

    /**
     * Gibt die Artikelnummer als Integer zurück
     * @return Artikelnummer
     */
    public int getArtikelnummer() {

        return artikelnummer;
    }

    /**
     * Gibt die Bezeichnung eines Artikels als String zurück
     * @return Bezeichnung
     */
    public String getBezeichnung() {
        return bezeichnung;
    }

    /**
     * Gibt den Bestand eines Artikels als Integer zurück
     * @return Bestand
     */
    public int getBestand() {
        return bestand;
    }

    /**
     * Aendert den bestand eines Artikels
     * @param bestand
     */
    public void setBestand(int bestand) throws MassengutException {
        this.bestand = bestand;
    }

    /**
     * Gibt den Preis eines Artikel als Float zurück
     * @return Preis
     */
    public float getPreis() {
        return preis;
    }

    /**
     * Setzt die Bezeichnung eines Artikels
     * @param bezeichnung
     */
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
}