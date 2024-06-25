package eShop.local.domain.exceptions;

import eShop.local.entities.Artikel;

public class NegativerBestandException extends Exception {
    public NegativerBestandException(Artikel artikel, int neuerBestand) {
        super("Neue Anzahl " + neuerBestand + " überschreitet den Bestand von " + artikel.getBezeichnung() + ": " + artikel.getBestand());
    }
}
