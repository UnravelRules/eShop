package eShop.local.entities;

import java.util.Objects;

public class Mitarbeiter {
    private int mitarbeiterNummer;
    private String name;
    private String benutzername;
    private String passwort;

    public Mitarbeiter(int nummer, String name, String benutzer, String pw){
        this.mitarbeiterNummer = nummer;
        this.name = name;
        this.benutzername = benutzer;
        this.passwort = pw;
    }

    /**
     * Gibt die Mitarbeiternummer des Mitarbeiterobjektes zurück
     * @return Mitabrbeiternummer
     */
    public int getMitarbeiterNummer(){
        return mitarbeiterNummer;
    }

    /**
     * Gibt den Namen des des Mitarbeiterobjektes zurück.
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

    @Override
    public boolean equals(Object anderesObjekt) {
        if(anderesObjekt instanceof Mitarbeiter andererMitarbeiter){
            return this.benutzername.equals(andererMitarbeiter.getBenutzername());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(benutzername);
    }

}
