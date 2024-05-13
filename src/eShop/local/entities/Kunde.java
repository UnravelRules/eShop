package eShop.local.entities;

import java.util.Objects;

public class Kunde {
    private int kundenNummer;
    private String name;
    private String strasse = "";
    private String plz = "";
    private final String benutzername;
    private final String passwort;
    private Warenkorb warenkorb;


    public Kunde(int nummer, String name, String str, String plz, String benutzer, String pw){
        this.kundenNummer = nummer;
        this.name = name;
        this.strasse = str;
        this.plz = plz;
        this.benutzername = benutzer;
        this.passwort = pw;
        this.warenkorb = new Warenkorb();
    }

    /**
     * Gibt die Kundennummer eines Kundenobjektes zurück
     * @return Kundennummer
     */
    public int getKundenNummer(){return kundenNummer;}

    /**
     * Gibt den Namen eines Kunden als String zurück
     * @return Name
     */
    public String getName(){return name;}

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
     * Gibt den Benutzernamen eines Kunden zurück
     * @return Benutzername
     */
    public String getBenutzername(){return benutzername;}

    /**
     * Gibt das Passwort eines Kunden zurück.
     * Diese Methode wird zum Anmelden verwendet.
     * @return Passwort
     */
    public String getPasswort(){return passwort;}

    /**
     * Gibt den Warenkorb eines Kunden als Warenkorbobjekt zurück.
     * @return Warenkorb
     */
    public Warenkorb getWarenkorb() {
        return warenkorb;
    }

    @Override
    public boolean equals(Object anderesObjekt) {
        if (anderesObjekt instanceof Kunde andererKunde) {
            return this.benutzername.equals(andererKunde.benutzername);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(benutzername);
    }
}
