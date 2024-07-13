package eShop.common.entities;

import java.util.Objects;

public class Kunde extends Benutzer {
    private String strasse = "";
    private String plz = "";
    private Warenkorb warenkorb;


    public Kunde(int nummer, String name, String str, String plz, String benutzer, String pw){
        super(nummer, name, benutzer, pw);
        this.strasse = str;
        this.plz = plz;
        this.warenkorb = new Warenkorb();
    }


    /**
     * Gibt die Straße eines Kunden als String zurück
     * @return Straße
     */
    public String getStrasse(){return strasse;}

    /**
     * Gibt die Postleitzahl eines Kunden als Integer zurück
     * @return Postleitzahl
     */
    public String getPlz(){return plz;}


    /**
     * Gibt das Passwort eines Kunden zurück.
     * Diese Methode wird zum Anmelden verwendet.
     * @return Passwort
     */

    public Warenkorb getWarenkorb() {
        return warenkorb;
    }

    @Override
    public boolean equals(Object anderesObjekt) {
        if (anderesObjekt instanceof Kunde andererKunde) {
            return benutzername.equals(andererKunde.benutzername);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(benutzername);
    }
}
