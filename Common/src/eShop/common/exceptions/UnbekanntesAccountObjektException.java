package eShop.common.exceptions;

/**
 * Eine Ausnahme, die geworfen wird, wenn ein Account-Objekt weder ein Kunde noch ein Mitarbeiter ist.
 */
public class UnbekanntesAccountObjektException extends Exception {

    /**
     * Konstruktor für die UnbekanntesAccountObjektException.
     * Erzeugt eine neue Instanz dieser Ausnahme mit einer Standardfehlermeldung.
     */
    public UnbekanntesAccountObjektException() {
        super("Account Objekt ist kein Kunde oder Mitarbeiter");
    }
}
