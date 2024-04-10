package eShop.local.entities;

public class Kunde {
    private int kundenNummer;
    private String name;
    private String strasse = "";
    private String plz = "";
    private String benutzername;
    private String passwort;


    public Kunde(int nummer, String name, String benutzer, String pw){
        this.kundenNummer = nummer;
        this.name = name;
        this.benutzername = benutzer;
        this.passwort = pw;
    }
    public Kunde(int nummer, String name, String str, String plz, String benutzer, String pw){
        this.kundenNummer = nummer;
        this.name = name;
        this.strasse = str;
        this.plz = plz;
        this.benutzername = benutzer;
        this.passwort = pw;
    }

    public int getKundenNummer(){return kundenNummer;}
    public String getName(){return name;}
    public String getStrasse(){return strasse;}
    public String getPlz(){return plz;}
    public String getBenutzername(){return benutzername;}
    public String getPasswort(){return passwort;}
}
