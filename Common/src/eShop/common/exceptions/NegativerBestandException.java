package eShop.common.exceptions;

import eShop.common.entities.Artikel;

public class NegativerBestandException extends Exception {
    public NegativerBestandException(Artikel artikel, int neuerBestand) {
        super("Neue Anzahl " + neuerBestand + " überschreitet den Bestand von " + artikel.getBezeichnung() + ": " + artikel.getBestand());
    }
}
