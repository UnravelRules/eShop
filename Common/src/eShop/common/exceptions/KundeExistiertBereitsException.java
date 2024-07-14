package eShop.common.exceptions;

import eShop.common.entities.Kunde;

public class KundeExistiertBereitsException extends Exception {
    
    public KundeExistiertBereitsException(String benutzername){
        super("Kunde " + benutzername + " existiert bereits");
    }
    
}
