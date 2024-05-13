package eShop.local.domain;

import eShop.local.domain.exceptions.*;
import eShop.local.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class eShop {
    private KundenVerwaltung kundenVW;
    private MitarbeiterVerwaltung mitarbeiterVW;
    private ArtikelVerwaltung artikelVW;
    private ShoppingService shoppingService;

    private ArrayList<String> eventlog;

    private DateTimeFormatter datumFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public eShop(){
        kundenVW = new KundenVerwaltung();
        mitarbeiterVW = new MitarbeiterVerwaltung();
        artikelVW = new ArtikelVerwaltung();
        shoppingService = new ShoppingService();
        eventlog = new ArrayList<String>();
    }

    /**
     * Registriert einen neuen Kunden und gibt ihn als Kundenobjekt zurück
     * @param name
     * @param str
     * @param plz
     * @param benutzer
     * @param passwort
     * @return Kundenobjekt
     * @throws KundeExistiertBereitsException
     */
    public Kunde kundeRegistrieren(String name, String str, String plz, String benutzer, String passwort) throws KundeExistiertBereitsException {
        return kundenVW.registrieren(name, str, plz, benutzer, passwort);
    }

    /**
     * Loggt einen Kunden ein und gibt ihn als Kundenobjekt zurück
     * @param benutzername
     * @param passwort
     * @return Kundenobjekt
     * @throws KundeExistiertNichtException
     */
    public Kunde kundeEinloggen(String benutzername, String passwort) throws KundeExistiertNichtException {
        return kundenVW.login(benutzername, passwort);
    }

    /**
     * Loggt einen Mitarbeiter ein und gibt ihn als Mitarbeiterobjekt zurück
     * @param benutzername
     * @param passwort
     * @return Mitarbeiterobjekt
     * @throws MitarbeiterExistiertNichtException
     */
    public Mitarbeiter mitarbeiterEinloggen(String benutzername, String passwort) throws MitarbeiterExistiertNichtException{
        return mitarbeiterVW.anmelden(benutzername, passwort);
    }

    /**
     * Registriert einen Mitarbeiter
     * @param nummer
     * @param name
     * @param benutzer
     * @param passwort
     * @return Mitarbeiterobjekt
     * @throws MitarbeiterExistiertBereitsException
     */
    public Mitarbeiter mitarbeiterRegistrieren(int nummer, String name, String benutzer, String passwort) throws MitarbeiterExistiertBereitsException {
        return mitarbeiterVW.registrieren(nummer, name, benutzer, passwort);
    }

    /**
     * Entfernt einen Mitarbeiter
     * @param benutzername
     */
    public void mitarbeiterEntfernen(String benutzername){
        mitarbeiterVW.entfernen(benutzername);
    }

    /**
     * Legt einen neuen Artikel im Bestand an und speichert die Aenderung im Eventlog.
     * Gibt den neuen Artikel als Artikelobjekt zurück
     * @param nummer
     * @param bezeichnung
     * @param bestand
     * @param preis
     * @param aktuellerMitarbeiter
     * @return Artikel
     * @throws ArtikelExistiertBereitsException
     * @throws UnbekanntesAccountObjektException
     */
    public Artikel artikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertBereitsException, UnbekanntesAccountObjektException {
        Artikel a = new Artikel(nummer, bezeichnung, bestand, preis);
        artikelVW.artikelHinzufuegen(a);
        updateEventlog(aktuellerMitarbeiter, a);
        return a;
    }

    /**
     * Setzt den Bestand eines Artikels auf einen neuen Wert und speichert den neuen Bestand im Eventlog.
     * @param artikel_nummer
     * @param neuer_bestand
     * @param aktuellerMitarbeiter
     * @throws ArtikelExistiertNichtException
     * @throws UnbekanntesAccountObjektException
     */
    public void bestandAendern(int artikel_nummer, int neuer_bestand, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException {
        int ret_neuer_bestand = artikelVW.bestandAendern(artikel_nummer, neuer_bestand);
        Artikel artikel = artikelVW.getArtikelMitNummer(artikel_nummer);
        updateEventlog(aktuellerMitarbeiter, artikel);
    }
    public ArrayList<Artikel> artikelSuchen(String bezeichnung) {
        return artikelVW.artikelSuchen(bezeichnung);
    }

    /**
     * Entfernt einen Artikel aus dem Bestand und setzt den Bestand im Eventlog auf 0
     * @param nummer
     * @param bezeichnung
     * @param aktuellerMitarbeiter
     * @throws ArtikelExistiertNichtException
     * @throws UnbekanntesAccountObjektException
     */
    public void artikelEntfernen(int nummer, String bezeichnung, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException {
        Artikel artikel = artikelVW.getArtikelMitNummer(nummer);
        Artikel artikelKopie = new Artikel(artikel.getArtikelnummer(), artikel.getBezeichnung(), 0, artikel.getPreis());
        artikelVW.artikelEntfernen(nummer, bezeichnung);

        updateEventlog(aktuellerMitarbeiter, artikelKopie);
    }

    public ArrayList<Artikel> gibAlleArtikel(){
        return artikelVW.getArtikelBestand();
    }

    /**
     * Legt einen Artikel mit angegebener Anzahl in den Warenkorb des Kunden
     * @param artikelnummer
     * @param anzahl
     * @param aktuellerKunde
     */
    public void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde){
        shoppingService.artikelInWarenkorb(artikelnummer, anzahl, aktuellerKunde);
    }

    /**
     * Leert den Warenkorb eines Kunden
     * @param aktuellerKunde
     */
    public void warenkorbLeeren(Kunde aktuellerKunde){
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb().getHashmap();
        shoppingService.warenkorbLeeren(warenkorb);
    }

    /**
     * Kauft alle Artikel im Warenkorb und speichert die Änderungen des Bestands im Eventlog.
     * Es wird außerdem noch eine Rechnung zurückgegeben.
     * @param aktuellerKunde
     * @return Rechnung
     * @throws ArtikelExistiertNichtException
     * @throws UnbekanntesAccountObjektException
     */
    public Rechnung warenkorbKaufen(Kunde aktuellerKunde) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException {
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb().getHashmap();
        Rechnung rechnung = shoppingService.warenkorbKaufen(aktuellerKunde);
        for (Map.Entry<Artikel, Integer> eintrag : warenkorb.entrySet()) {
            Artikel artikel = eintrag.getKey();
            updateEventlog(aktuellerKunde, artikel);
        }
        return rechnung;
    }

    /**
     * Legt einen neuen Eintrag im Eventlog an.
     * Dabei werden Datum, Accounttyp, Benutzername, Accountnummer, Artikelbezeichnung, Artikelnummer und der neue Bestand gespeichert.
     * @param account
     * @param artikel
     * @return AktualisierterEventlog
     * @throws UnbekanntesAccountObjektException
     */
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
        LocalDate datum = LocalDate.now();
        String datumString = datum.format(datumFormatter);
        String event = String.format("%s, %s, %s, %d, %s, %d, %d", datumString, accountTyp, nutzerName, accountNummer, artikelBezeichnung, artikelnummer, neuerBestand);
        eventlog.add(event);
        return eventlog;
    }

    /**
     * Gibt den Eventlog als ArrayList<String> zurück
     * @return Eventlog
     */
    public ArrayList<String> getEventlog(){
        return eventlog;
    }
}