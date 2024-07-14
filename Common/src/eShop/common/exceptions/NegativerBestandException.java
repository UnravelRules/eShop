package eShop.common.exceptions;

import eShop.common.entities.Artikel;

public class NegativerBestandException extends Exception {
    private Artikel artikel;
    private int neuerBestand;

    public NegativerBestandException(Artikel artikel, int neuerBestand) {
        super("Neue Anzahl " + neuerBestand + " überschreitet den Bestand von " + artikel.getBezeichnung() + ": " + artikel.getBestand());
        this.artikel = artikel;
        this.neuerBestand = neuerBestand;
    }
    public Artikel getArtikel() {
        return artikel;
    }

    public int getNeuerBestand() {
        return neuerBestand;
    }
}
