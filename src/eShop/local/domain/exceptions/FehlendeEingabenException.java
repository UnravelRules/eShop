package eShop.local.domain.exceptions;

public class FehlendeEingabenException extends Exception{

    public FehlendeEingabenException(String action){
        super("Fehlende Eingabe bei Action: "+ action);
    }

}
