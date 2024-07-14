package eshop.server.domain;

import eShop.common.exceptions.*;
import eShop.common.entities.*;
import eShop.common.interfaces.eShopInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;


public class eShop implements eShopInterface {
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public Mitarbeiter mitarbeiterRegistrieren(int nummer, String name, String benutzer, String passwort) throws MitarbeiterExistiertBereitsException {
        Mitarbeiter m = new Mitarbeiter(nummer, name, benutzer, passwort);
        mitarbeiterVW.registrieren(m);
        return m;
    }

    /**
     * Entfernt einen Mitarbeiter
     * @param benutzername
     */
    @Override
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
    @Override
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

    @Override
    public Massengutartikel massengutartikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter, int packungsgroesse) throws MassengutException {
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

        throw new MassengutException(bestand, packungsgroesse);
    }

    /**
     * Setzt den Bestand eines Artikels auf einen neuen Wert und speichert den neuen Bestand im Eventlog.
     * @param artikel_nummer
     * @param neuer_bestand
     * @param aktuellerMitarbeiter
     * @throws ArtikelExistiertNichtException
     * @throws UnbekanntesAccountObjektException
     */
    @Override
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
        artikelVW.bestandAendern(artikel_nummer, neuer_bestand);
        ereignisVW.updateEventlog(ereignisTyp, aktuellerMitarbeiter, artikel, delta);
    }
    @Override
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
    @Override
    public void artikelEntfernen(int nummer, String bezeichnung, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException {
        Artikel artikel = artikelVW.getArtikelMitNummer(nummer);
        int delta = artikel.getBestand();
        Artikel artikelKopie = new Artikel(artikel.getArtikelnummer(), artikel.getBezeichnung(), 0, artikel.getPreis());
        artikelVW.artikelEntfernen(nummer, bezeichnung);
        EreignisTyp ereignisTyp = EreignisTyp.AUSLAGERUNG;
        ereignisVW.updateEventlog(ereignisTyp, aktuellerMitarbeiter, artikelKopie, delta);
    }

    @Override
    public ArrayList<Artikel> gibAlleArtikel(){
        return artikelVW.getArtikelBestand();
    }

    /**
     * Legt einen Artikel mit angegebener Anzahl in den Warenkorb des Kunden
     * @param artikelnummer
     * @param anzahl
     * @param aktuellerKunde
     */
    @Override
    public void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde) throws ArtikelExistiertNichtException, MassengutException {
        Artikel artikel = artikelVW.getArtikelMitNummer(artikelnummer);
        shoppingService.artikelInWarenkorb(artikel, anzahl, aktuellerKunde);
    }

    /**
     * Leert den Warenkorb eines Kunden
     * @param aktuellerKunde
     */
    @Override
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
    @Override
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

    @Override
    public void warenkorbVeraendern(Kunde aktuellerKunde, String bezeichnung, int neuerBestand) throws MassengutException, ArtikelExistiertNichtException, NegativerBestandException {
        Artikel artikel = artikelVW.getArtikelMitBezeichnung(bezeichnung);
        if(neuerBestand > artikel.getBestand()){
            throw new NegativerBestandException(artikel, neuerBestand);
        }
        shoppingService.warenkorbVeraendern(aktuellerKunde, artikel, neuerBestand);
    }

    @Override
    public void artikelAusWarenkorbEntfernen(Kunde aktuellerKunde, String bezeichnung) throws ArtikelExistiertNichtException {
        Artikel artikel = artikelVW.getArtikelMitBezeichnung(bezeichnung);
        shoppingService.artikelAusWarenkorbEntfernen(aktuellerKunde, artikel);
    }

    @Override
    public HashMap<Artikel, Integer> gibWarenkorb(Kunde aktuellerKunde){
        return aktuellerKunde.getWarenkorb().getInhalt();
    }

    @Override
    public ArrayList<Ereignis> eventlogAusgeben(){
        return ereignisVW.getEventlog();
    }

    @Override
    public void schreibeKunde() throws IOException {
        kundenVW.schreibeDaten(kundenDatei+"_K.txt");
    }
    @Override
    public void schreibeMitarbeiter() throws IOException {
        mitarbeiterVW.schreibeDaten(mitarbeiterDatei+"_M.txt");
    }

    @Override
    public void schreibeArtikel() throws IOException {
        artikelVW.schreibeDaten(artikelDatei+"_A.txt");
    }

    @Override
    public void schreibeEreignis() throws IOException {
        ereignisVW.schreibeDaten(ereignisDatei+"_E.txt");
    }

    @Override
    public void sichereDaten() throws IOException{
        schreibeKunde();
        schreibeMitarbeiter();
        schreibeArtikel();
        schreibeEreignis();
    }

    @Override
    public ArrayList<Integer> getBestandhistorie(int Artikelnummer) throws ArtikelExistiertNichtException {
        Artikel artikel = artikelVW.getArtikelMitNummer(Artikelnummer);
        ArrayList<Integer> bestandslog = new ArrayList<>();
        ArrayList<Ereignis> eventlog = ereignisVW.getEventlog();

        int aktueller_bestand = artikel.getBestand();
        LocalDate aktuelles_datum = LocalDate.now();
        LocalDate cutoffDatum = aktuelles_datum.minusDays(30);


        int ereignis_index = eventlog.size() - 1;
        while (!aktuelles_datum.isBefore(cutoffDatum)) {
            Ereignis aktuelles_ereignis = null;
            try {
                aktuelles_ereignis = eventlog.get(ereignis_index);
            } catch (IndexOutOfBoundsException ignore) {
                aktuelles_ereignis = eventlog.get(++ereignis_index);
            }
            LocalDate ereignis_datum = aktuelles_ereignis.getDatum();
            while(ereignis_datum.isBefore(aktuelles_datum)) {
                bestandslog.add(aktueller_bestand);
                aktuelles_datum = aktuelles_datum.minusDays(1);
            }

            while(!aktuelles_ereignis.getArtikelbezeichnung().equals(artikel.getBezeichnung())) {
                ereignis_index--;
                try{
                    aktuelles_ereignis = eventlog.get(ereignis_index);
                } catch( IndexOutOfBoundsException ignored){
                    break;
                }
            }

            bestandslog.add(aktueller_bestand);
            aktuelles_datum = aktuelles_datum.minusDays(1);
            try{
                aktuelles_ereignis = eventlog.get(ereignis_index);
                ereignis_datum = aktuelles_ereignis.getDatum();
            } catch( IndexOutOfBoundsException exception){
                continue;
            }

            while(aktuelles_datum.isBefore(ereignis_datum) & ereignis_index >= 0){
                // Iterieren durch ereignisse bis wir an einem neuen Tag ankommen
                if (aktuelles_ereignis.getArtikelbezeichnung().equals(artikel.getBezeichnung())) {
                    int delta = aktuelles_ereignis.getBestandsaenderung();
                    switch (aktuelles_ereignis.getEreignisTyp()) {
                        case NEU -> {
                            // Bestand vor einfügen eines Artikels ist 0
                            aktueller_bestand = 0;
                        }
                        case KAUF, AUSLAGERUNG -> {
                            // Artikel wurden entfernt
                            aktueller_bestand += delta;
                        }
                        case EINLAGERUNG -> {
                            // Artikel wurden hinzugefügt
                            aktueller_bestand -= delta;
                        }
                        case null, default -> {
                            throw new RuntimeException("Unknown Ereignistyp");
                        }
                    }
                }
                try{
                    ereignis_index--;
                    aktuelles_ereignis = eventlog.get(ereignis_index);
                    ereignis_datum = aktuelles_ereignis.getDatum();
                } catch( IndexOutOfBoundsException ignored){
                    break;
                }
            }

        }
        bestandslog.removeLast();
        return bestandslog;
    }
}