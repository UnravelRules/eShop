package eShop.local.domain.exceptions;

import eShop.local.entities.Kunde;

public class KundeExistiertBereitsException extends Exception {
    
    public KundeExistiertBereitsException(Kunde kunde){
        super("Kunde " + kunde.getBenutzername() + " existiert bereits");
    }
    
}
