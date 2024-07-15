package eShop.common.exceptions;

/**
 * Eine Ausnahme, die geworfen wird, wenn ein Mitarbeiter nicht gefunden wird.
 */
public class MitarbeiterExistiertNichtException extends Exception {

    /**
     * Konstruktor für die MitarbeiterExistiertNichtException.
     *
     * @param mitarbeiter Der Benutzername des Mitarbeiters, der nicht gefunden wurde
     */
    public MitarbeiterExistiertNichtException(String mitarbeiter) {
        super("Benutzername " + mitarbeiter + " konnte nicht als Mitarbeiter gefunden werden");
    }
}
