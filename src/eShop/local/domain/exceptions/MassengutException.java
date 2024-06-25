package eShop.local.domain.exceptions;

import eShop.local.entities.Massengutartikel;

public class MassengutException extends Exception {
    public MassengutException(int bestand, int packungsgroesse){
        super("Der Bestand " + bestand + " ist nicht durch die Packungsgröße " + packungsgroesse + " teilbar!");
    }

}