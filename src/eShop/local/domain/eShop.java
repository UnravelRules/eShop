package eShop.local.domain;

import eShop.local.domain.exceptions.*;
import eShop.local.entities.Artikel;
import eShop.local.entities.Kunde;
import eShop.local.entities.Mitarbeiter;

import java.util.ArrayList;

public class eShop {
    private KundenVerwaltung kundenVW;
    private MitarbeiterVerwaltung mitarbeiterVW;
    private ArtikelVerwaltung artikelVW;

    public eShop(){
        kundenVW = new KundenVerwaltung();
        mitarbeiterVW = new MitarbeiterVerwaltung();
        artikelVW = new ArtikelVerwaltung();
    }

    public Kunde kundeRegistrieren(String name, String str, String plz, String benutzer, String passwort) throws KundeExistiertBereitsException {
        return kundenVW.registrieren(name, str, plz, benutzer, passwort);
    }
    public Kunde kundeEinloggen(String benutzername, String passwort) throws KundeExistiertNichtException {
        return kundenVW.login(benutzername, passwort);
    }

    public Mitarbeiter mitarbeiterEinloggen(String benutzername, String passwort) throws MitarbeiterExistiertNichtException{
        return mitarbeiterVW.anmelden(benutzername, passwort);
    }

    public Mitarbeiter mitarbeiterRegistrieren(int nummer, String name, String benutzer, String passwort) throws MitarbeiterExistiertBereitsException {
        return mitarbeiterVW.registrieren(nummer, name, benutzer, passwort);
    }

    public void mitarbeiterEntfernen(String benutzername){
        mitarbeiterVW.entfernen(benutzername);
    }

    public Artikel artikelAnlegen(int nummer, String bezeichnung, int bestand, float preis) throws ArtikelExistiertBereitsException {
        Artikel a = new Artikel(nummer, bezeichnung, bestand, preis);
        artikelVW.artikelHinzufuegen(a);
        return a;
    }

    public void bestandAendern(int artikel_nummer, int neuer_bestand) throws ArtikelExistiertNichtException{
        int ret_neuer_bestand = artikelVW.bestandAendern(artikel_nummer, neuer_bestand);

    }
    public ArrayList<Artikel> artikelSuchen(String bezeichnung) {
        return artikelVW.artikelSuchen(bezeichnung);
    }

    public void artikelEntfernen(int nummer, String bezeichnung) {
        artikelVW.artikelEntfernen(nummer, bezeichnung);
    }

    public ArrayList<Artikel> gibAlleArtikel(){
        return artikelVW.getArtikelBestand();
    }
}
