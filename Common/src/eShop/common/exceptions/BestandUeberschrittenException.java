package eShop.common.exceptions;

import eShop.common.entities.Artikel;

/**
 * Eine Ausnahme, die geworfen wird, wenn versucht wird, den Bestand eines Artikels zu erhöhen,
 * der bereits einen niedrigeren aktuellen Bestand hat.
 */
public class BestandUeberschrittenException extends Exception {
    private Artikel artikel;
    private int neuerBestand;

    /**
     * Konstruktor für die BestandUeberschrittenException.
     *
     * @param artikel Der Artikel, dessen Bestand überschritten wurde
     * @param neuerBestand Die neue Anzahl, die den aktuellen Bestand des Artikels überschreitet
     */
    public BestandUeberschrittenException(Artikel artikel, int neuerBestand) {
        super("Neue Anzahl " + neuerBestand + " überschreitet den Bestand von " + artikel.getBezeichnung() + ": " + artikel.getBestand());
        this.artikel = artikel;
        this.neuerBestand = neuerBestand;
    }

    /**
     * Gibt den Artikel zurück, dessen Bestand überschritten wurde.
     *
     * @return Der betroffene Artikel
     */
    public Artikel getArtikel() {
        return artikel;
    }

    /**
     * Gibt die neue Anzahl zurück, die den aktuellen Bestand des Artikels überschreitet.
     *
     * @return Die neue Anzahl
     */
    public int getNeuerBestand() {
        return neuerBestand;
    }
}
