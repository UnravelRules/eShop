package eShop.common.entities;

import java.util.Objects;

/**
 * Diese Klasse repräsentiert einen Kunden im eShop-System.
 * Ein Kunde erweitert die Klasse Benutzer und enthält zusätzliche Informationen wie Straße, Postleitzahl und Warenkorb.
 */
public class Kunde extends Benutzer {
    private String strasse = "";
    private String plz = "";
    private Warenkorb warenkorb;

    /**
     * Konstruktor zur Initialisierung eines Kunden.
     *
     * @param nummer     Die Kundennummer
     * @param name       Der Name des Kunden
     * @param str        Die Straße des Kunden
     * @param plz        Die Postleitzahl des Kunden
     * @param benutzer   Der Benutzername des Kunden
     * @param pw         Das Passwort des Kunden
     */
    public Kunde(int nummer, String name, String str, String plz, String benutzer, String pw){
        super(nummer, name, benutzer, pw);
        this.strasse = str;
        this.plz = plz;
        this.warenkorb = new Warenkorb();
    }


    /**
     * Gibt die Straße des Kunden zurück.
     *
     * @return Die Straße des Kunden als String
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Gibt die Postleitzahl des Kunden zurück.
     *
     * @return Die Postleitzahl des Kunden als String
     */
    public String getPlz() {
        return plz;
    }

    /**
     * Gibt den Warenkorb des Kunden zurück.
     *
     * @return Der Warenkorb des Kunden
     */
    public Warenkorb getWarenkorb() {
        return warenkorb;
    }

    /**
     * Überschriebene equals-Methode zur Überprüfung der Gleichheit von Kunden.
     * Zwei Kunden gelten als gleich, wenn ihre Benutzernamen übereinstimmen.
     *
     * @param anderesObjekt Das andere Objekt, mit dem verglichen wird
     * @return true, wenn die Kunden gleich sind, ansonsten false
     */
    @Override
    public boolean equals(Object anderesObjekt) {
        if (anderesObjekt instanceof Kunde andererKunde) {
            return benutzername.equals(andererKunde.benutzername);
        }
        return false;
    }

    /**
     * Überschriebene hashCode-Methode zur Berechnung des Hashcodes basierend auf dem Benutzernamen.
     *
     * @return Der Hashcode des Kundenobjekts
     */
    @Override
    public int hashCode() {
        return Objects.hash(benutzername);
    }
}
