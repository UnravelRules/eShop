package eShop.local.domain.exceptions;

import eShop.local.entities.Massengutartikel;

public class MassengutException extends Exception{

    public MassengutException(Massengutartikel ma){
        super("Der Massengutartikel "+ ma.getBezeichnung() +" kann nur in Vielfachen von " + ma.getPackungsgroesse() + " ausgewählt werden");
    }

}