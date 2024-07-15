package eShop.common.entities;

/**
 * Oberklasse Benutzer erstellt grundlegende Attribute und Methoden eines Benutzers,
 * von welchem dann Mitarbeiter und Kunden erben können.
 */
public class Benutzer {
    protected int nummer;
    protected String name;
    protected String benutzername;
    protected String passwort;

    /**
     * Konstruktor zur Initialisierung eines Benutzers mit gegebenen Parametern.
     *
     * @param nummer Die eindeutige Nummer des Benutzers.
     * @param name Der Name des Benutzers.
     * @param benutzername Der Benutzername des Benutzers.
     * @param passwort Das Passwort des Benutzers.
     */
    public Benutzer(int nummer, String name, String benutzername, String passwort){
        this.nummer = nummer;
        this.name = name;
        this.benutzername = benutzername;
        this.passwort = passwort;
    }

    /**
     * Gibt die eindeutige Nummer des Benutzers zurück.
     *
     * @return Die Nummer des Benutzers.
     */
    public int getNummer() {
        return nummer;
    }

    /**
     * Gibt den Namen des Benutzers zurück.
     *
     * @return Der Name des Benutzers.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt den Benutzernamen des Benutzers zurück.
     *
     * @return Der Benutzername des Benutzers.
     */
    public String getBenutzername() {
        return benutzername;
    }

    /**
     * Gibt das Passwort des Benutzers zurück.
     *
     * @return Das Passwort des Benutzers.
     */
    public String getPasswort() {
        return passwort;
    }
}