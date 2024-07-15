package eShop.common.entities;

import java.util.Objects;

public class Mitarbeiter extends Benutzer {
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

    @Override
    public boolean equals(Object anderesObjekt) {
        if(anderesObjekt instanceof Mitarbeiter andererMitarbeiter){
            return (this.benutzername.equals(andererMitarbeiter.getBenutzername())
                    || this.nummer == andererMitarbeiter.getMitarbeiterNummer());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(benutzername);
    }

}
