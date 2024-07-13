package eShop.common.exceptions;

import eShop.common.entities.Mitarbeiter;

public class MitarbeiterExistiertBereitsException extends Exception{
    public MitarbeiterExistiertBereitsException(Mitarbeiter mitarbeiter){
        super("Mitarbeiter " + mitarbeiter.getBenutzername() + " existiert schon");
    }
}
