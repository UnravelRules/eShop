package eShop.common.exceptions;

/**
 * Eine Ausnahme, die geworfen wird, wenn kein Kunde mit einem bestimmten Benutzernamen gefunden wurde.
 */
public class KundeExistiertNichtException extends Exception {

    /**
     * Konstruktor für die KundeExistiertNichtException.
     *
     * @param nutzername Der Benutzername des nicht gefundenen Kunden
     */
    public KundeExistiertNichtException(String nutzername) {
        super("Keinen Kunden mit Benutzername " + nutzername + " gefunden");
    }
}
