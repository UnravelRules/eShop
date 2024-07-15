package eShop.local.domain.exceptions;

import eShop.local.entities.Artikel;

public class BestandUeberschrittenException extends Exception {
    public BestandUeberschrittenException(Artikel artikel, int neuerBestand) {
        super("Neue Anzahl " + neuerBestand + " überschreitet den Bestand von " + artikel.getBezeichnung() + ": " + artikel.getBestand());
    }
}
