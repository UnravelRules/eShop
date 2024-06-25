package eShop.local.domain;

import eShop.local.domain.exceptions.*;
import eShop.local.entities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class eShop {
    private String kundenDatei = "";
    private String mitarbeiterDatei = "";
    private String artikelDatei = "";
    private String ereignisDatei = "";
    private KundenVerwaltung kundenVW;
    private MitarbeiterVerwaltung mitarbeiterVW;
    private ArtikelVerwaltung artikelVW;
    private EreignisVerwaltung ereignisVW;
    private ShoppingService shoppingService;


    public eShop(String kundenDatei, String mitarbeiterDatei, String artikelDatei, String ereignisDatei) throws IOException {
        this.kundenDatei = kundenDatei;
        this.mitarbeiterDatei = mitarbeiterDatei;
        this.artikelDatei = artikelDatei;
        this.ereignisDatei = ereignisDatei;

        kundenVW = new KundenVerwaltung();
        kundenVW.liesDaten(kundenDatei+"_K.txt");
        mitarbeiterVW = new MitarbeiterVerwaltung();
        mitarbeiterVW.liesDaten(mitarbeiterDatei+"_M.txt");
        artikelVW = new ArtikelVerwaltung();
        artikelVW.liesDaten(artikelDatei+"_A.txt");
        ereignisVW = new EreignisVerwaltung();
        ereignisVW.liesDaten(ereignisDatei+"_E.txt");
        shoppingService = new ShoppingService(artikelVW);
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
    public Kunde kundeRegistrieren(String name, String str, String plz, String benutzer, String passwort) throws KundeExistiertBereitsException, FehlendeEingabenException {
        if(name == null || str == null || plz == null || benutzer == null || passwort == null){
            throw new FehlendeEingabenException("Registrierung");
        } else if (name.isEmpty() || str.isEmpty() || plz.isEmpty() || benutzer.isEmpty() || passwort.isEmpty()) {
            throw new FehlendeEingabenException("Registrierung");
        }
        Kunde k = new Kunde(kundenVW.getKundenliste().size() + 1, name, str, plz, benutzer, passwort);
        kundenVW.registrieren(k);
        return k;
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
        Mitarbeiter m = new Mitarbeiter(nummer, name, benutzer, passwort);
        mitarbeiterVW.registrieren(m);
        return m;
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
    public Artikel artikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter) throws RuntimeException {
        Artikel a = new Artikel(nummer, bezeichnung, bestand, preis);
        try{
            artikelVW.artikelHinzufuegen(a);
            EreignisTyp ereignisTyp = EreignisTyp.NEU;
            ereignisVW.updateEventlog(ereignisTyp, aktuellerMitarbeiter, a, bestand);
        } catch(ArtikelExistiertBereitsException | UnbekanntesAccountObjektException e){
            throw new RuntimeException(e.getMessage());
        }
        return a;
    }

    public Massengutartikel massengutartikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter, int packungsgroesse) throws  MassengutException {
        Massengutartikel massengutartikel = new Massengutartikel(nummer, bezeichnung, bestand, preis, packungsgroesse);
        if(bestand % packungsgroesse == 0){
            try {
                artikelVW.massengutartikelHinzufuegen(massengutartikel);
                EreignisTyp ereignisTyp = EreignisTyp.NEU;
                ereignisVW.updateEventlog(ereignisTyp, aktuellerMitarbeiter, massengutartikel, bestand);
                return massengutartikel;
            } catch (ArtikelExistiertBereitsException | UnbekanntesAccountObjektException e) {
                throw new RuntimeException(e);
            }
        }
        throw new MassengutException(massengutartikel);
    }

    /**
     * Setzt den Bestand eines Artikels auf einen neuen Wert und speichert den neuen Bestand im Eventlog.
     * @param artikel_nummer
     * @param neuer_bestand
     * @param aktuellerMitarbeiter
     * @throws ArtikelExistiertNichtException
     * @throws UnbekanntesAccountObjektException
     */
    public void bestandAendern(int artikel_nummer, int neuer_bestand, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException, MassengutException {
        Artikel artikel = artikelVW.getArtikelMitNummer(artikel_nummer);
        EreignisTyp ereignisTyp;
        int delta = neuer_bestand - artikel.getBestand();
        if(delta < 0){
            // Falls Artikel entfernt wurden
            ereignisTyp = EreignisTyp.AUSLAGERUNG;
            delta = delta * (-1);
        } else {
            ereignisTyp = EreignisTyp.EINLAGERUNG;
        }
        int ret_neuer_bestand = artikelVW.bestandAendern(artikel_nummer, neuer_bestand);
        ereignisVW.updateEventlog(ereignisTyp, aktuellerMitarbeiter, artikel, delta);
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
        int delta = artikel.getBestand();
        Artikel artikelKopie = new Artikel(artikel.getArtikelnummer(), artikel.getBezeichnung(), 0, artikel.getPreis());
        artikelVW.artikelEntfernen(nummer, bezeichnung);
        EreignisTyp ereignisTyp = EreignisTyp.AUSLAGERUNG;
        ereignisVW.updateEventlog(ereignisTyp, aktuellerMitarbeiter, artikelKopie, delta);
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
    public void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde) throws ArtikelExistiertNichtException, MassengutException {
        Artikel artikel = artikelVW.getArtikelMitNummer(artikelnummer);
        shoppingService.artikelInWarenkorb(artikel, anzahl, aktuellerKunde);
    }

    /**
     * Leert den Warenkorb eines Kunden
     * @param aktuellerKunde
     */
    public void warenkorbLeeren(Kunde aktuellerKunde){
        shoppingService.warenkorbLeeren(aktuellerKunde);
    }

    /**
     * Kauft alle Artikel im Warenkorb und speichert die Änderungen des Bestands im Eventlog.
     * Es wird außerdem noch eine Rechnung zurückgegeben.
     * @param aktuellerKunde
     * @return Rechnung
     * @throws ArtikelExistiertNichtException
     * @throws UnbekanntesAccountObjektException
     */
    public Rechnung warenkorbKaufen(Kunde aktuellerKunde) throws UnbekanntesAccountObjektException, MassengutException, ArtikelExistiertNichtException {
        HashMap<Artikel, Integer> inhalt = new HashMap<>(aktuellerKunde.getWarenkorb().getInhalt());
        Rechnung rechnung = shoppingService.warenkorbKaufen(aktuellerKunde);

        for (Map.Entry<Artikel, Integer> eintrag : inhalt.entrySet()) {
            Artikel artikel = eintrag.getKey();
            EreignisTyp ereignisTyp = EreignisTyp.KAUF;
            int delta = eintrag.getValue();
            ereignisVW.updateEventlog(ereignisTyp, aktuellerKunde, artikel, delta);
        }

        return rechnung;
    }

    public void warenkorbVeraendern(Kunde aktuellerKunde, String bezeichnung, int neuerBestand) throws MassengutException, ArtikelExistiertNichtException {
        Artikel artikel = artikelVW.getArtikelMitBezeichnung(bezeichnung);
        shoppingService.warenkorbVeraendern(aktuellerKunde, artikel, neuerBestand);
    }

    public void artikelAusWarenkorbEntfernen(Kunde aktuellerKunde, String bezeichnung) throws ArtikelExistiertNichtException {
        Artikel artikel = artikelVW.getArtikelMitBezeichnung(bezeichnung);
        shoppingService.artikelAusWarenkorbEntfernen(aktuellerKunde, artikel);
    }

    public HashMap<Artikel, Integer> gibWarenkorb(Kunde aktuellerKunde){
        return aktuellerKunde.getWarenkorb().getInhalt();
    }

    public ArrayList<Ereignis> eventlogAusgeben(){
        return ereignisVW.getEventlog();
    }

    public void schreibeKunde() throws IOException {
        kundenVW.schreibeDaten(kundenDatei+"_K.txt");
    }
    public void schreibeMitarbeiter() throws IOException {
        mitarbeiterVW.schreibeDaten(mitarbeiterDatei+"_M.txt");
    }

    public void schreibeArtikel() throws IOException {
        artikelVW.schreibeDaten(artikelDatei+"_A.txt");
    }

    public void schreibeEreignis() throws IOException {
        ereignisVW.schreibeDaten(ereignisDatei+"_E.txt");
    }

    public void sichereDaten() throws IOException{
        schreibeKunde();
        schreibeMitarbeiter();
        schreibeArtikel();
        schreibeEreignis();
    }
}