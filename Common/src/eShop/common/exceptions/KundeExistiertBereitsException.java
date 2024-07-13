package eShop.common.exceptions;

import eShop.common.entities.Kunde;

public class KundeExistiertBereitsException extends Exception {
    
    public KundeExistiertBereitsException(Kunde kunde){
        super("Kunde " + kunde.getBenutzername() + " existiert bereits");
    }
    
}
