package eShop.local.domain;

import eShop.local.domain.exceptions.*;
import eShop.local.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class eShop {
    private KundenVerwaltung kundenVW;
    private MitarbeiterVerwaltung mitarbeiterVW;
    private ArtikelVerwaltung artikelVW;
    private ShoppingService shoppingService;

    private ArrayList<String> eventlog;


    public eShop(){
        kundenVW = new KundenVerwaltung();
        mitarbeiterVW = new MitarbeiterVerwaltung();
        artikelVW = new ArtikelVerwaltung();
        shoppingService = new ShoppingService();
        eventlog = new ArrayList<String>();
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

    public Artikel artikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertBereitsException, UnbekanntesAccountObjektException {
        Artikel a = new Artikel(nummer, bezeichnung, bestand, preis);
        artikelVW.artikelHinzufuegen(a);
        updateEventlog(aktuellerMitarbeiter, a);
        return a;
    }

    public void bestandAendern(int artikel_nummer, int neuer_bestand, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException {
        int ret_neuer_bestand = artikelVW.bestandAendern(artikel_nummer, neuer_bestand);
        Artikel artikel = artikelVW.getArtikelMitNummer(artikel_nummer);
        updateEventlog(aktuellerMitarbeiter, artikel);
    }
    public ArrayList<Artikel> artikelSuchen(String bezeichnung) {
        return artikelVW.artikelSuchen(bezeichnung);
    }

    public void artikelEntfernen(int nummer, String bezeichnung, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException {
        Artikel artikel = artikelVW.getArtikelMitNummer(nummer);
        Artikel artikelKopie = new Artikel(artikel.getArtikelnummer(), artikel.getBezeichnung(), 0, artikel.getPreis());
        artikelVW.artikelEntfernen(nummer, bezeichnung);

        updateEventlog(aktuellerMitarbeiter, artikelKopie);
    }

    public ArrayList<Artikel> gibAlleArtikel(){
        return artikelVW.getArtikelBestand();
    }

    public void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde){
        shoppingService.artikelInWarenkorb(artikelnummer, anzahl, aktuellerKunde);
    }

    public void warenkorbLeeren(Kunde aktuellerKunde){
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb().getHashmap();
        shoppingService.warenkorbLeeren(warenkorb);
    }

    public Rechnung warenkorbKaufen(Kunde aktuellerKunde) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException {
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb().getHashmap();
        Rechnung rechnung = shoppingService.warenkorbKaufen(aktuellerKunde);
        for (Map.Entry<Artikel, Integer> eintrag : warenkorb.entrySet()) {
            Artikel artikel = eintrag.getKey();
            updateEventlog(aktuellerKunde, artikel);
        }
        return rechnung;
    }

    ArrayList<String> updateEventlog(Object account, Artikel artikel) throws UnbekanntesAccountObjektException {
        String accountTyp = "";
        int accountNummer = 0;
        String nutzerName = "";
        if(account instanceof Mitarbeiter) {
            accountTyp = "Mitarbeiter";
            nutzerName = ((Mitarbeiter) account).getBenutzername();
            accountNummer = ((Mitarbeiter) account).getMitarbeiterNummer();
        } else if (account instanceof Kunde) {
            accountTyp = "Kunde";
            nutzerName = ((Kunde) account).getBenutzername();
            accountNummer = ((Kunde) account).getKundenNummer();
        } else
            throw  new UnbekanntesAccountObjektException();
        int artikelnummer = artikel.getArtikelnummer();
        String artikelBezeichnung = artikel.getBezeichnung();
        int neuerBestand = artikel.getBestand();
        String event = String.format("%s, %s, %d, %s, %d, %d", accountTyp, nutzerName, accountNummer, artikelBezeichnung, artikelnummer, neuerBestand);
        eventlog.add(event);
        return eventlog;
    }

    public ArrayList<String> getEventlog(){
        return eventlog;
    }
}