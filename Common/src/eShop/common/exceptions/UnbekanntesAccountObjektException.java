package eShop.common.exceptions;

public class UnbekanntesAccountObjektException extends Exception{

    public UnbekanntesAccountObjektException(){
        super("Account Objekt ist kein Kunde oder Mitarbeiter");
    }

}
