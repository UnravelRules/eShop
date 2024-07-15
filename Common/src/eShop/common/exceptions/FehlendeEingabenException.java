package eShop.common.exceptions;

public class FehlendeEingabenException extends Exception{
    private String action;


    public FehlendeEingabenException(String action){
        super("Fehlende Eingabe bei Action: "+ action);
        this.action = action;
    }

    public String getAction() {
        return action;
    }

}
