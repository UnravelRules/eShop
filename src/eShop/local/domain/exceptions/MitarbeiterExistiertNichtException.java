package eShop.local.domain.exceptions;

public class MitarbeiterExistiertNichtException extends Exception{

    public MitarbeiterExistiertNichtException(String mitarbeiter){
        super("Benutzername " + mitarbeiter + " konnte nicht als Mitarbeiter gefunden werden");
    }

}
