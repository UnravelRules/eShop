package eShop.common.exceptions;

import eShop.common.entities.Mitarbeiter;

/**
 * Eine Ausnahme, die geworfen wird, wenn ein Mitarbeiter bereits existiert.
 */
public class MitarbeiterExistiertBereitsException extends Exception {

    private Mitarbeiter mitarbeiter;

    /**
     * Konstruktor für die MitarbeiterExistiertBereitsException.
     *
     * @param mitarbeiter Das Mitarbeiterobjekt, das bereits existiert
     */
    public MitarbeiterExistiertBereitsException(Mitarbeiter mitarbeiter) {
        super("Mitarbeiter " + mitarbeiter.getBenutzername() + " existiert schon");
        this.mitarbeiter = mitarbeiter;
    }

    /**
     * Gibt das Mitarbeiterobjekt zurück, für das die Ausnahme ausgelöst wurde.
     *
     * @return Das Mitarbeiterobjekt, das bereits existiert
     */
    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }
}
