package eshop.server.net;

import eShop.common.entities.*;
import eShop.common.exceptions.*;
import eShop.common.interfaces.eShopInterface;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Nimmt Anfragen von Clients entgegen, verarbeitet diese und delegiert an den Eshop
 */
public class ClientRequestProcessor implements Runnable{

    // Eshop Objekt, das die eigentliche Arbeit macht
    private eShopInterface eshop;
    private Kunde aktuellerKunde;
    private Mitarbeiter aktuellerMitarbeiter;

    // Kommunikation
    private Socket clientSocket;
    private BufferedReader in;
    private PrintStream out;

     public ClientRequestProcessor(Socket clientSocket, eShopInterface eShop) {
         this.eshop = eShop;
         this.clientSocket = clientSocket;

         try{
             in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             out = new PrintStream(clientSocket.getOutputStream());
         } catch (IOException e) {
             try {
                 clientSocket.close();
             } catch (IOException e1) {
                 System.out.println("Exception beim Öffnen der Streams");
             }
         }

         System.out.println("Verbunden mit " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

     }

    /**
     * Der Server akzeptiert Anfragen zu Menüs auf dem aktuellen Socket
     */
    public void run() {
        String input = "";

        out.println("Server an Client: Bereit");
        do {
            try {
                input = in.readLine();
            } catch (Exception e) {
                System.out.println("Exception bei Lesen vom Client: ");
                System.out.println(e.getMessage());
                input = null;
            }
            switch (input){
                case null:
                    input = "q";
                    break;
                case "r":
                    kundeRegistrieren();
                    break;
                case "k":
                    int status_k = kundeEinloggen();
                    if(status_k == 0){
                        kundenMenu();
                    }
                    break;
                case "m":
                    int status_m = mitarbeiterEinloggen();
                    if(status_m == 0){
                        mitarbeiterMenu();
                    }
                    break;
                case "z":
                    datenSichern();
                    break;
                case "a", "q":
                    break;
                default:
                    System.out.println("Unbekannte Aktion im Hauptmenü: " + input);
            }
        } while (!(input.equals("q")));
        disconnect();
    }

    /**
     * Schließt den Socket und speichert die Daten
     */
    private void disconnect() {
        try {
            out.println("Tschuess!");
            clientSocket.close();

            System.out.println("Verbindung zu " + clientSocket.getInetAddress()
                    + ":" + clientSocket.getPort() + " durch Client abgebrochen");
            eshop.sichereDaten();
        } catch (Exception e) {
            System.out.println("--->Fehler beim Beenden der Verbindung: ");
            System.out.println(e.getMessage());
            out.println("Fehler");
        }
    }

    /**
     * Hilfsfunktion, die einen String vom Client einließt und bei einer IOException den Context ausgibt
     * @param context Der Context, indem die Funktion aufgerufen wurde. Ist sehr hilfreich fürs debuggen.
     * @return Gibt den gelesenen String zurück
     */
    public String liesEingabeVonClient(String context){
        String input = null;
        try{
            input = in.readLine();
        } catch (IOException e) {
            System.out.println("Exception bei Lesen vom Client: " + context);
            System.out.println(e.getMessage());
        }
        return input;
    }

    /**
     * Das Kundenmenü mit allen möglichen Anfrageoptionen
     */
    private void kundenMenu(){
        String input = "";
        do {
            try{
                input = in.readLine();
            } catch (Exception e) {
                System.out.println("Exception bei Lesen vom Client: Kundenmenü");
                System.out.println(e.getMessage());
                input = null;
            }
            switch(input){
                case null:
                case "a":
                    input = "a";
                    try {
                        eshop.sichereDaten();
                    } catch (IOException e) {
                        System.out.println("Fehler beim Speichern der Daten");
                    }
                    break;
                case "s":
                    sucheArtikel();
                    break;
                case "p":
                    gibAlleArtikel();
                    break;
                case "h":
                    artikelInWarenkorb();
                    break;
                case "w":
                    gibWarenkorb();
                    break;
                case "k":
                    warenkorbKaufen();
                    break;
                case "v":
                    warenkorbVeraendern();
                    break;
                case "l":
                    warenkorbLeeren();
                    break;
                case "z":
                    datenSichern();
                default:
                    System.out.println("Unbekannte Aktion im Kundenmenü: " + input);
            }

        } while (!(input.equals("a")));
        this.aktuellerKunde = null;
    }

    /**
     * Leert den Warenkorb des eingeloggten Kunden
     */
    private void warenkorbLeeren() {
        try{
            eshop.warenkorbLeeren(this.aktuellerKunde);
            out.println("Erfolg");
        } catch (Exception e) {
            out.println("Fehler");
        }

    }

    /**
     * Das Mitarbeitermenü mit allen möglichen Anfrageoptionen
     */
    private void mitarbeiterMenu(){
        String input = "";
        do {
            try{
                input = in.readLine();
            } catch (Exception e) {
                System.out.println("Exception bei Lesen vom Client: Mitarbeitermenü");
                System.out.println(e.getMessage());
                input = null;
            }
            switch(input){
                case null:
                case "a":
                    input = "a";
                    try {
                        eshop.sichereDaten();
                    } catch (IOException e) {
                        System.out.println("Fehler beim Speichern der Daten");
                    }
                    break;
                case "s":
                    sucheArtikel();
                    break;
                case "p":
                    gibAlleArtikel();
                    break;
                case "na":
                    // Anlegen eines normalen Artikels
                    artikelAnlegen();
                    break;
                case "nm":
                    // Anlegen eines Massengutartikels
                    massengutArtikelAnlegen();
                    break;
                case "b":
                    bestandaendern();
                    break;
                case "l":
                    eventlogAusgeben();
                    break;
                case "h":
                    bestandHistorieAusgeben();
                    break;
                case "rm":
                    artikelEntfernen();
                    break;
                case "m":
                    mitarbeiterRegistrieren();
                    break;
                case "d":
                    mitarbeiterLoeschen();
                    break;
                case "z":
                    datenSichern();
                    break;
                default:
                    System.out.println("Unbekannte Aktion: " + input);
            }

        } while (!(input.equals("a")));
        this.aktuellerMitarbeiter = null;
    }

    /**
     * Methode, die vom Client aufgerufen werden, kann zum Sichern der Daten.
     * Diese gibt noch zurück, ob es erfolgreich war.
     */
    private void datenSichern() {
        try {
            eshop.sichereDaten();
            out.println("Erfolg");
        } catch (IOException e) {
            out.println("Fehler");
            out.println(e.getMessage());
        }
    }

    /**
     * Löscht einen Mitarbeiter
     */
    private void mitarbeiterLoeschen() {
        String benutzername = liesEingabeVonClient("Benutzername");
        try {
            eshop.mitarbeiterEntfernen(benutzername);
            out.println("Erfolg");
        } catch (Exception e) {
            out.println("Fehler");
            out.println(e.getMessage());
        }
    }

    /**
     * Registriert einen neuen Mitarbeiter
     */
    private void mitarbeiterRegistrieren() {
        int nummer = Integer.parseInt(liesEingabeVonClient("Nummer"));
        String name = liesEingabeVonClient("Name");
        String benutzername = liesEingabeVonClient("Benutzername");
        String passwort = liesEingabeVonClient("Passwort");
        try {
            Mitarbeiter mitarbeiter = eshop.mitarbeiterRegistrieren(nummer, name, benutzername, passwort);
            out.println("Erfolg");
            sendeMitarbeiterAnClient(mitarbeiter);
        } catch (MitarbeiterExistiertBereitsException e) {
            out.println("MitarbeiterExistiertBereitsException");
            sendeMitarbeiterAnClient(e.getMitarbeiter());
        } catch (FehlendeEingabenException e) {
            out.println("FehlendeEingabenException");
            out.println(e.getAction());
        }
    }

    /**
     * Entfernt einen Artikel aus der Artikelverwaltung
     */
    private void artikelEntfernen() {
        int nummer = Integer.parseInt(liesEingabeVonClient("Nummer"));
        String bezeichnung = liesEingabeVonClient("Bezeichnung");
        try {
            eshop.artikelEntfernen(nummer, bezeichnung, this.aktuellerMitarbeiter);
            out.println("Erfolg");
        } catch (ArtikelExistiertNichtException e) {
            out.println("ArtikelExistiertNichtException");
            out.println(e.getBezeichnung());
        } catch (UnbekanntesAccountObjektException e) {
            out.println("UnbekanntesAccountObjektException");
        }
    }

    /**
     * Sendet die Bestandshistorie an den Client
     */
    private void bestandHistorieAusgeben() {
        int nummer = Integer.parseInt(liesEingabeVonClient("Nummer"));
        try {
            ArrayList<Integer> historie = eshop.getBestandhistorie(nummer);
            out.println("Erfolg");
            int size = historie.size();
            out.println(size);
            for (int i = 0; i < size; i++) {
                out.println(historie.get(i));
            }
        } catch (ArtikelExistiertNichtException e) {
            out.println("Fehler");
            out.println(e.getBezeichnung());
        }
    }

    /**
     * Sendet den Ereignislog an den Client
     */
    private void eventlogAusgeben() {
        ArrayList<Ereignis> eventlog = eshop.eventlogAusgeben();
        int anzahl = eventlog.size();
        out.println(anzahl);
        for(Ereignis ereignis : eventlog){
            sendeEventAnClient(ereignis);
        }
    }

    /**
     * Sendet ein einzelnes Ereignis an den Client
     * @param ereignis Ereignis
     */
    private void sendeEventAnClient(Ereignis ereignis) {
        out.println(ereignis.getEreignisTyp().name());
        out.println(ereignis.getAccountTyp().name());
        out.println(ereignis.getBenutzerName());
        out.println(ereignis.getBestandsaenderung());
        String dateString = ereignis.getDatum().toString();
        out.println(dateString);
        out.println(ereignis.getArtikelbezeichnung());

    }

    /**
     * Ändert den Bestand eines Artikels in der Artikelverwaltung
     */
    private void bestandaendern() {
        int nummer = Integer.parseInt(liesEingabeVonClient("Nummer"));
        int neuerBestand = Integer.parseInt(liesEingabeVonClient("Neuer Bestand"));
        try {
            eshop.bestandAendern(nummer, neuerBestand, aktuellerMitarbeiter);
            out.println("Erfolg");
        } catch (ArtikelExistiertNichtException e) {
            out.println("ArtikelExistiertNichtException");
            out.println(e.getBezeichnung());
        } catch (UnbekanntesAccountObjektException e) {
            out.println("UnbekanntesAccountObjektException");
        } catch (MassengutException e) {
            out.println("MassengutException");
            out.println(e.getBestand());
            out.println(e.getPackungsgroesse());
        }
    }

    /**
     * Legt einen neuen Massengutartikel in der Artikelverwaltung an
     */
    private void massengutArtikelAnlegen() {
        int nummer = Integer.parseInt(liesEingabeVonClient("Nummer"));
        String bezeichnung = liesEingabeVonClient("Bezeichnung");
        int bestand = Integer.parseInt(liesEingabeVonClient("Bestand"));
        float preis = Float.parseFloat(liesEingabeVonClient("Preis"));
        int packungsgroesse = Integer.parseInt(liesEingabeVonClient("Packungsgroesse"));
        try {
            Massengutartikel massengutartikel = eshop.massengutartikelAnlegen(nummer, bezeichnung, bestand, preis, this.aktuellerMitarbeiter, packungsgroesse);
            out.println("Erfolg");
            sendeArtikelAnClient(massengutartikel);
        } catch (MassengutException e) {
            out.println("Fehler");
            out.println(e.getBestand());
            out.println(e.getPackungsgroesse());
        }
    }

    /**
     * Legt einen normalen Artikel in der Artikelverwaltung an
     */
    private void artikelAnlegen() {
        int nummer = Integer.parseInt(liesEingabeVonClient("Nummer"));
        String bezeichnung = liesEingabeVonClient("Bezeichnung");
        int bestand = Integer.parseInt(liesEingabeVonClient("Bestand"));
        float preis = Float.parseFloat(liesEingabeVonClient("Preis"));
        try {
            Artikel artikel = eshop.artikelAnlegen(nummer, bezeichnung, bestand, preis, this.aktuellerMitarbeiter);
            out.println("Erfolg");
            sendeArtikelAnClient(artikel);
        } catch (RuntimeException e) {
            out.println("Fehler");
            out.println(e.getMessage());
        }

    }

    /**
     * Registriert einen neuen Kunden in der Kundenverwaltung
     */
    private void kundeRegistrieren() {
        String name = liesEingabeVonClient("Name");
        String strasse = liesEingabeVonClient("Strasse");
        String plz = liesEingabeVonClient("Postleitzahl");
        String benutzername = liesEingabeVonClient("Benutzername");
        String passwort = liesEingabeVonClient("Passwort");

        // Einfügen in die KundeVW
        try{
            Kunde k = eshop.kundeRegistrieren(name, strasse, plz, benutzername, passwort);
            out.println("Erfolg");
            sendeKundeAnClient(k);
        } catch (KundeExistiertBereitsException e) {
            out.println("KundeExistiertBereitsException");
        } catch (FehlendeEingabenException e) {
            out.println("FehlendeEingabenException");
            out.println(e.getAction());
        }
    }

    /**
     * Loggt einen Kunden ein
     * @return Statuscode 0 = Erfolg, 1 = Fehler
     */
    private int kundeEinloggen(){
        String benutzername = liesEingabeVonClient("Benutzername");
        String passwort = liesEingabeVonClient("Passwort");

        try {
            Kunde kunde = eshop.kundeEinloggen(benutzername, passwort);
            out.println("Erfolg");
            sendeKundeAnClient(kunde);
            this.aktuellerKunde = kunde;
            return 0;
        } catch (LoginFehlgeschlagenException e) {
            out.println("Fehler");
            return 1;
        }


    }

    /**
     * Sendet ein Kundenobjekt and den Client
     * @param kunde Kunde
     */
    private void sendeKundeAnClient(Kunde kunde){
        out.println(kunde.getNummer());
        out.println(kunde.getName());
        out.println(kunde.getStrasse());
        out.println(kunde.getPlz());
        out.println(kunde.getBenutzername());
        out.println(kunde.getPasswort());
    }

    /**
     * Loggt einen Mitarbeiter ein und sendet das Mitarbeiterobjekt an den Client
     * @return Statuscode 0 = Erfolg, 1 = Fehler
     */
    private int mitarbeiterEinloggen(){
        String benutzername = liesEingabeVonClient("Benutzername");
        String passwort = liesEingabeVonClient("Passwort");

        try {
            Mitarbeiter mitarbeiter = eshop.mitarbeiterEinloggen(benutzername, passwort);
            out.println("Erfolg");
            sendeMitarbeiterAnClient(mitarbeiter);
            this.aktuellerMitarbeiter = mitarbeiter;
            return 0;
        } catch (LoginFehlgeschlagenException e) {
            out.println("Fehler");
            return 1;
        }

    }

    /**
     * Sendet ein Mitarbeiterobjekt an den Client
     * @param mitarbeiter Mitarbeiter
     */
    private void sendeMitarbeiterAnClient(Mitarbeiter mitarbeiter) {
        out.println(mitarbeiter.getNummer());
        out.println(mitarbeiter.getName());
        out.println(mitarbeiter.getBenutzername());
        out.println(mitarbeiter.getPasswort());
    }

    /**
     * Sendet alle Artikel in der Artikelverwaltung an den Client
     */
    private void gibAlleArtikel() {
        List<Artikel> artikelList = eshop.gibAlleArtikel();
        // sende alle Artikel zum Client
        sendeArtikelListeAnClient(artikelList);

    }

    /**
     * Sendet einen Artikel an den Client
     * @param artikel Artikel
     */
    private void sendeArtikelAnClient(Artikel artikel) {
        // Wir senden zuerst ein Typ (A für Artikel, M für Massengutartikel)
        if (artikel instanceof Massengutartikel){
            out.println("M");
            out.println(artikel.getArtikelnummer());
            out.println(artikel.getBezeichnung());
            out.println(artikel.getBestand());
            out.println(artikel.getPreis());
            out.println(((Massengutartikel) artikel).getPackungsgroesse());
        } else if (artikel != null) {
            out.println("A");
            out.println(artikel.getArtikelnummer());
            out.println(artikel.getBezeichnung());
            out.println(artikel.getBestand());
            out.println(artikel.getPreis());
        }
    }

    /**
     * Sendet eine Liste von Artikeln an den Client.
     * Das wird z.B. von der Suche benutzt
     * @param artikelList Artikelliste
     */
    private void sendeArtikelListeAnClient(List<Artikel> artikelList) {
        // Anzahl von Artikeln übertragen
        out.println(artikelList.size());
        for (Artikel artikel : artikelList) {
            sendeArtikelAnClient(artikel);
        }
    }

    /**
     * Sucht nach einem Artikel in der Artikelverwaltung und gibt dann die Liste an den Client zurück
     */
    private void sucheArtikel(){
        String bezeichnung = liesEingabeVonClient("Bezeichnung");
        ArrayList<Artikel> artikelListe = eshop.artikelSuchen(bezeichnung);
        sendeArtikelListeAnClient(artikelListe);
    }

    /**
     * Fügt einen Artikel zum Warenkorb des eingeloggten Kunden hinzu
     */
    private void artikelInWarenkorb(){
        int artikelNummer = Integer.parseInt(liesEingabeVonClient("ArtikelNummer"));
        int anzahl = Integer.parseInt(liesEingabeVonClient("Anzahl"));
        try {
            eshop.artikelInWarenkorb(artikelNummer, anzahl, this.aktuellerKunde);
            out.println("Erfolg");
        } catch (ArtikelExistiertNichtException e) {
            out.println("ArtikelExistiertNichtException");
            out.println(e.getBezeichnung());
        } catch (MassengutException e) {
            out.println("MassengutException");
            out.println(e.getBestand());
            out.println(e.getPackungsgroesse());
        } catch (BestandUeberschrittenException e) {
            out.println("BestandUeberschrittenException");
            Artikel artikel = e.getArtikel();
            sendeArtikelAnClient(artikel);
            out.println(e.getNeuerBestand());
        }
    }

    /**
     * Sendet den Warenkorb des eingeloggten Kunden als Hashmap an den Client.
     */
    private void gibWarenkorb(){
        HashMap<Artikel, Integer> warenkorb = eshop.gibWarenkorb(this.aktuellerKunde);
        sendeWarenkorbAnClient(warenkorb);
    }

    /**
     * Sendet den Warenkorb als einzelne Artikel und Integer an den Client
     * @param warenkorb Hashmap des Warenkorbs
     */
    private void sendeWarenkorbAnClient(HashMap<Artikel, Integer> warenkorb) {
        int size = warenkorb.size();
        out.println(size);
        for (Artikel artikel : warenkorb.keySet()) {
            sendeArtikelAnClient(artikel);
            out.println(warenkorb.get(artikel));
        }
    }

    /**
     * Führt den Kauf durch und sendet die Rechnung and den Client
     */
    private void warenkorbKaufen(){
        try {
            Rechnung rechnung = eshop.warenkorbKaufen(this.aktuellerKunde);
            out.println("Erfolg");
            sendeRechnungAnClient(rechnung);
        } catch (UnbekanntesAccountObjektException | MassengutException | ArtikelExistiertNichtException e) {
            out.println("Fehler");
            out.println(e.getMessage());
        }
    }

    /**
     * Sendet die Rechnung an den Client
     * @param rechnung Rechnung
     */
    private void sendeRechnungAnClient(Rechnung rechnung){
        sendeKundeAnClient(rechnung.getKunde());
        sendeWarenkorbAnClient(rechnung.getGekaufteArtikel());
        out.println(rechnung.getGesamtpreis());
    }

    /**
     * Verändert den Bestand eines Artikels im Warenkorb des eingeloggten Kunden
     */
    private void warenkorbVeraendern(){
        Kunde kunde = this.aktuellerKunde;
        String bezeichnung = liesEingabeVonClient("Bezeichnung");
        int neuerBestand = Integer.parseInt(liesEingabeVonClient("Neuer Bestand"));
        try {
            eshop.warenkorbVeraendern(kunde, bezeichnung, neuerBestand);
            out.println("Erfolg");
        } catch (MassengutException e) {
            out.println("MassengutException");
            out.println(e.getBestand());
            out.println(e.getPackungsgroesse());
        } catch (ArtikelExistiertNichtException e) {
            out.println("ArtikelExistiertNichtException");
            out.println(e.getBezeichnung());
        } catch (BestandUeberschrittenException e) {
            out.println("BestandUeberschrittenException");
            Artikel artikel = e.getArtikel();
            sendeArtikelAnClient(artikel);
            out.println(e.getNeuerBestand());
        }
    }
}
