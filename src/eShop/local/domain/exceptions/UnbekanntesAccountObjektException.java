package eShop.local.domain.exceptions;

import eShop.local.entities.Benutzer;

public class UnbekanntesAccountObjektException extends Exception{

    public UnbekanntesAccountObjektException(){
        super("Account Objekt ist kein Kunde oder Mitarbeiter");
    }

}
