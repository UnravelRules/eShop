package eShop.local.domain.exceptions;

import eShop.local.entities.Mitarbeiter;

public class MitarbeiterExistiertBereitsException extends Exception{
    public MitarbeiterExistiertBereitsException(Mitarbeiter mitarbeiter){
        super("Mitarbeiter " + mitarbeiter.getBenutzername() + " existiert schon");
    }
}
