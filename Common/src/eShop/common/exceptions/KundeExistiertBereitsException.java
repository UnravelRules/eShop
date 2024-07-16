package eShop.common.exceptions;

/**
 * Eine Ausnahme, die geworfen wird, wenn ein Kunde mit einem bestimmten Benutzernamen bereits existiert.
 */
public class KundeExistiertBereitsException extends Exception {

    /**
     * Konstruktor für die KundeExistiertBereitsException.
     *
     * @param benutzername Der Benutzername des existierenden Kunden
     */
    public KundeExistiertBereitsException(String benutzername) {
        super("Kunde " + benutzername + " existiert bereits");
    }
}
