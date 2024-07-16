package eShop.common.exceptions;

/**
 * Eine Ausnahme, die geworfen wird, wenn für eine bestimmte Aktion fehlende Eingaben festgestellt werden.
 */
public class FehlendeEingabenException extends Exception {
    private String action;

    /**
     * Konstruktor für die FehlendeEingabenException.
     *
     * @param action Die Aktion, für die fehlende Eingaben festgestellt wurden
     */
    public FehlendeEingabenException(String action) {
        super("Fehlende Eingabe bei Action: " + action);
        this.action = action;
    }

    /**
     * Gibt die Aktion zurück, für die fehlende Eingaben festgestellt wurden.
     *
     * @return Die betroffene Aktion
     */
    public String getAction() {
        return action;
    }
}
