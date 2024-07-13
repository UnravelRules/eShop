package eShop.common.entities;

public class Benutzer {
    protected int nummer;
    protected String name;
    protected String benutzername;
    protected String passwort;

    public Benutzer(int nummer, String name, String benutzername, String passwort){
        this.nummer = nummer;
        this.name = name;
        this.benutzername = benutzername;
        this.passwort = passwort;
    }

    public int getNummer() {
        return nummer;
    }

    public String getName() {
        return name;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public String getPasswort() {
        return passwort;
    }

}
