package eShop.local.domain.exceptions;

public class KundeExistiertNichtException extends Exception{
    public KundeExistiertNichtException(String nutzername){
        super("Keinen Kunden mit Benutzername "+ nutzername+ " gefunden");
    }

}
