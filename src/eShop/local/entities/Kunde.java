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
    }

    public int getKundenNummer(){return kundenNummer;}
    public String getName(){return name;}
    public String getStrasse(){return strasse;}
    public String getPlz(){return plz;}
    public String getBenutzername(){return benutzername;}
    public String getPasswort(){return passwort;}

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
