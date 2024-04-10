package eShop.local.entities;

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

    public int getMitarbeiterNummer(){return mitarbeiterNummer;}
    public String getName(){return name;}
    public String getBenutzername(){return benutzername;}
    public String getPasswort(){return passwort;}
}
