package eShop.common.exceptions;

public class LoginFehlgeschlagenException extends Exception {
    public LoginFehlgeschlagenException(String benutzername, String passwort){
        super("Login fehlgeschlagen! Entweder Benutzername " + benutzername + " oder Passwort " + passwort + " falsch.");
    }
}
