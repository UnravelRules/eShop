package eShop.common.exceptions;

import eShop.common.entities.Mitarbeiter;

public class MitarbeiterExistiertBereitsException extends Exception{
    private Mitarbeiter mitarbeiter;

    public MitarbeiterExistiertBereitsException(Mitarbeiter mitarbeiter){
        super("Mitarbeiter " + mitarbeiter.getBenutzername() + " existiert schon");
        this.mitarbeiter = mitarbeiter;
    }

    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }
}
