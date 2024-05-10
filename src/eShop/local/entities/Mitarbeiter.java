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

    public int getMitarbeiterNummer(){
        return mitarbeiterNummer;
    }
    public String getName(){
        return name;
    }
    public String getBenutzername(){
        return benutzername;
    }
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
