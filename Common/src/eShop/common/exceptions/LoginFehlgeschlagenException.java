package eShop.common.exceptions;

/**
 * Eine Ausnahme, die geworfen wird, wenn ein Login-Versuch fehlschlägt.
 */
public class LoginFehlgeschlagenException extends Exception {

    /**
     * Konstruktor für die LoginFehlgeschlagenException.
     *
     * @param benutzername Der Benutzername, der beim fehlgeschlagenen Login versucht wurde
     * @param passwort Das Passwort, das beim fehlgeschlagenen Login versucht wurde
     */
    public LoginFehlgeschlagenException(String benutzername, String passwort) {
        super("Login fehlgeschlagen! Entweder Benutzername " + benutzername + " oder Passwort " + passwort + " falsch.");
    }
}
