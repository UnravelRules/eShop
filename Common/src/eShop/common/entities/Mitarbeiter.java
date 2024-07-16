package eShop.common.entities;

import java.util.Objects;

/**
 * Diese Klasse repräsentiert einen Mitarbeiter im eShop-System.
 * Ein Mitarbeiter ist ein spezieller Benutzer mit zusätzlichen Informationen wie Mitarbeiternummer.
 */
public class Mitarbeiter extends Benutzer {
    /**
     * Konstruktor zur Initialisierung eines Mitarbeiterobjekts.
     *
     * @param nummer     Die Mitarbeiternummer des Mitarbeiters
     * @param name       Der Name des Mitarbeiters
     * @param benutzer   Der Benutzername des Mitarbeiters
     * @param pw         Das Passwort des Mitarbeiters
     */
    public Mitarbeiter(int nummer, String name, String benutzer, String pw){
        super(nummer, name, benutzer, pw);
    }

    /**
     * Gibt die Mitarbeiternummer des Mitarbeiterobjektes zurück
     * @return Mitabrbeiternummer
     */
    public int getMitarbeiterNummer(){
        return nummer;
    }

    /**
     * Gibt den Namen des Mitarbeiterobjektes zurück.
     * @return Name
     */
    public String getName(){
        return name;
    }

    /**
     * Gibt den Benutzernamen eines Mitarbeiterobjektes zurück
     * @return Benutzername
     */
    public String getBenutzername(){
        return benutzername;
    }

    /**
     * Gibt das Passwort eines Mitarbeiter Objektes zurück.
     * Diese Methode wird zum anmelden benutzt.
     * @return Passwort
     */
    public String getPasswort(){
        return passwort;
    }

    /**
     * Überschriebene equals-Methode zur Überprüfung der Gleichheit von Mitarbeiterobjekten.
     * Zwei Mitarbeiter sind gleich, wenn entweder ihre Benutzernamen oder ihre Mitarbeiternummern übereinstimmen.
     *
     * @param anderesObjekt Das andere Objekt, mit dem verglichen wird
     * @return true, wenn die Mitarbeiter gleich sind, ansonsten false
     */
    @Override
    public boolean equals(Object anderesObjekt) {
        if(anderesObjekt instanceof Mitarbeiter andererMitarbeiter){
            return (this.benutzername.equals(andererMitarbeiter.getBenutzername())
                    || this.nummer == andererMitarbeiter.getMitarbeiterNummer());
        } else {
            return false;
        }
    }

    /**
     * Überschriebene hashCode-Methode zur Berechnung des Hashcodes für Mitarbeiterobjekte.
     * Der Hashcode wird basierend auf dem Benutzernamen des Mitarbeiters berechnet.
     *
     * @return Der berechnete Hashcode des Mitarbeiters
     */
    @Override
    public int hashCode() {
        return Objects.hash(benutzername);
    }

}
