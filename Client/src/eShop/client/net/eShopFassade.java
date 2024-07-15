package eShop.client.net;

import eShop.common.entities.*;
import eShop.common.exceptions.*;
import eShop.common.interfaces.eShopInterface;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class eShopFassade implements eShopInterface {

    private Socket socket = null;
    private BufferedReader sin;
    private PrintStream sout;

    public eShopFassade(String host, int port) throws IOException {
        try {
            socket = new Socket(host, port);

            InputStream is = socket.getInputStream();
            sin = new BufferedReader(new InputStreamReader(is));
            sout = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            System.err.println("Fehler beim Socket-Stream öffnen: " + e);
            // Wenn im "try"-Block Fehler auftreten, dann Socket schließen:
            if (socket != null)
                socket.close();
            System.err.println("Socket geschlossen");
            System.exit(0);
        }

        // Verbindung erfolgreich hergestellt: IP-Adresse und Port ausgeben
        System.err.println("Verbunden: " + socket.getInetAddress() + ":"
                + socket.getPort());

        // Begrüßungsmeldung vom Server lesen
        String message = sin.readLine();
        System.out.println(message);

    }

    /**
     * Funktion, die die Registrierung eines Kunden beim Server aufruft
     * @param name Name der Kunden
     * @param str Straße des Kunden
     * @param plz Postleitzahl des Kunden
     * @param benutzer Benutzername des Kunden
     * @param passwort Passwort des Kunden
     * @return Gibt den erstellten Kunden zurück
     * @throws KundeExistiertBereitsException Falls bereits ein Kunde mit demselben Benutzernamen existiert
     * @throws FehlendeEingabenException Falls mindestens ein Feld leer ist
     */
    @Override
    public Kunde kundeRegistrieren(String name, String str, String plz, String benutzer, String passwort) throws KundeExistiertBereitsException, FehlendeEingabenException {
        // Aktion senden
        sout.println("r");
        // Parameter senden
        sout.println(name);
        sout.println(str);
        sout.println(plz);
        sout.println(benutzer);
        sout.println(passwort);

        // Antwort vom Server
        String antwort = "Fehler";
        try {
            antwort = sin.readLine();
            if (antwort.equals("Erfolg")){
                Kunde kunde = liesKundeVonServer();
                return kunde;
            } else if (antwort.equals("KundeExistiertBereitsException")) {
                // Registrierung Fehlgeschlagen
                throw new KundeExistiertBereitsException(benutzer);
            } else if (antwort.equals("FehlendeEingabenException")) {
                String ex_message = sin.readLine();
                throw new FehlendeEingabenException(ex_message);
            } else {
                throw new IOException();
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Empfängt ein Kundenobjekt vom Server
     * @return Gibt den Kunden zurück
     * @throws IOException Falls während der Übertragung etwas fehlschlägt
     */
    private Kunde liesKundeVonServer() throws IOException {
        String antwort = sin.readLine();
        int nummer = Integer.parseInt(antwort);
        String name;
        name = sin.readLine();
        String str;
        str = sin.readLine();
        String plz;
        plz = sin.readLine();
        String benutzer;
        benutzer = sin.readLine();
        String passwort;
        passwort = sin.readLine();
        Kunde kunde = new Kunde(nummer, name, str, plz, benutzer, passwort);
        return kunde;
    }

    /**
     * Ruft die KundenEinloggen Methode auf dem Server auf
     * @param benutzername Benutzername des Kunden
     * @param passwort Passwort des Kunden
     * @return Gibt das Kundenobjekt bei Erfolg zurück
     * @throws LoginFehlgeschlagenException Falls der Login fehlschlägt
     */
    @Override
    public Kunde kundeEinloggen(String benutzername, String passwort) throws LoginFehlgeschlagenException {
        sout.println("k");
        sout.println(benutzername);
        sout.println(passwort);
        // Antwort vom Server
        String antwort = "Fehler";
        try {
            antwort = sin.readLine();
            if (antwort.equals("Erfolg")){
                Kunde kunde = liesKundeVonServer();
                return kunde;
            } else {
                throw new LoginFehlgeschlagenException(benutzername, passwort);
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Ruft die MitarbeiterEinloggen Methode auf dem Server auf
     * @param benutzername Benutzername des Mitarbeiters
     * @param passwort Passwort des Mitarbeiters
     * @return Gibt das MitarbeiterObjekt bei Erfolg zurück
     * @throws LoginFehlgeschlagenException Falls der Login fehlschlägt
     */
    @Override
    public Mitarbeiter mitarbeiterEinloggen(String benutzername, String passwort) throws LoginFehlgeschlagenException {
        sout.println("m");
        sout.println(benutzername);
        sout.println(passwort);

        String antwort = "Fehler";
        try {
            antwort = sin.readLine();
            if (antwort.equals("Erfolg")){
                Mitarbeiter mitarbeiter = liesMitarbeiterVonServer();
                return mitarbeiter;
            } else {
                throw new LoginFehlgeschlagenException(benutzername, passwort);
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Empfängt ein Mitarbeiterobjekt vom Server
     * @return Gibt das Mitarbeiterobjekt zurück
     * @throws IOException Falls bei der Übertragung etwas schiefgeht
     */
    private Mitarbeiter liesMitarbeiterVonServer() throws IOException {
        String antwort;
        antwort = sin.readLine();
        int nummer = Integer.parseInt(antwort);
        String name;
        name = sin.readLine();
        String benutzername;
        benutzername = sin.readLine();
        String passwort;
        passwort = sin.readLine();

        Mitarbeiter mitarbeiter = new Mitarbeiter(nummer, name, benutzername, passwort);
        return mitarbeiter;
    }

    /**
     * Ruft die Mitarbeiterregistrieren-Methode auf dem Server auf
     * @param nummer Mitarbeiternummer
     * @param name Name des Mitarbeiters
     * @param benutzer Benutzername des Mitarbeiters
     * @param passwort Passwort des Mitarbeiters
     * @return Gibt das Mitarbeiterobjekt zurück
     * @throws MitarbeiterExistiertBereitsException Falls bereits ein Mitarbeiter mit diesem Benutzernamen existiert
     * @throws FehlendeEingabenException Falls mindestens eines der Felder leer ist
     */
    @Override
    public Mitarbeiter mitarbeiterRegistrieren(int nummer, String name, String benutzer, String passwort) throws MitarbeiterExistiertBereitsException, FehlendeEingabenException {
        sout.println("m");
        sout.println(nummer);
        sout.println(name);
        sout.println(benutzer);
        sout.println(passwort);
        try {
            String antwort = sin.readLine();
            if (antwort.equals("Erfolg")){
                return liesMitarbeiterVonServer();
            } else if(antwort.equals("MitarbeiterExistiertBereitsException")) {
                Mitarbeiter ex_mitarbeiter = liesMitarbeiterVonServer();
                throw new MitarbeiterExistiertBereitsException(ex_mitarbeiter);
            } else if(antwort.equals("FehlendeEingabenException")){
                String ex_message = sin.readLine();
                throw new FehlendeEingabenException(ex_message);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ruft die Mitarbeiterentfernen-Methode auf dem Server auf
     * @param benutzername Benutzername des Mitarbeiters, der entfernt werden soll
     */
    @Override
    public void mitarbeiterEntfernen(String benutzername) {
        sout.println("d");
        sout.println(benutzername);
        try {
            String antwort = sin.readLine();
            if (!(antwort.equals("Erfolg"))){
                String ex_message = sin.readLine();
                System.out.println(ex_message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Nimmt alle Daten zum Erstellen eines Artikels entgegen und sendet diese an den Server um dort den Artikel anzulegen
     * @param nummer Artikelnummer
     * @param bezeichnung Bezeichnung des Artikels
     * @param bestand Bestand des Artikels
     * @param preis Preis des Artikels
     * @param aktuellerMitarbeiter Aktuell eingeloggter Mitarbeiter (Wird vom Server nicht mehr verwendet, da dieser einfach den eingeloggten Mitarbeiter auf Serverseite benutzt)
     * @return Gibt den Artikel zurück
     * @throws RuntimeException Falls beim Erstellen etwas schiefgeht
     */
    @Override
    public Artikel artikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter) throws RuntimeException {
        // Neuen normalen Artikel anlegen ist auf Serverseite mit dieser Kombination versehen, da die CUI
        // bereits zwischen Artikel und Massengutartikel im Methodenaufruf unterscheidet.
        sout.println("na");
        sout.println(nummer);
        sout.println(bezeichnung);
        sout.println(bestand);
        sout.println(preis);
        String input = "";
        try {
            input = sin.readLine();
            if (input.equals("Erfolg")){
                return liesArtikelVonServer();
            } else {
                input = sin.readLine();
                throw new RuntimeException(input);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Nimmt alle Eingaben für das Erstellen eies Massengutartikels entgegen und sendet diese an den Server um ihn da zu erstellen
     * @param nummer Artikelnummer
     * @param bezeichnung Bezeichnung
     * @param bestand Bestand des Artikels (sollte ein Vielfaches der Packungsgröße sein)
     * @param preis Preis
     * @param aktuellerMitarbeiter Aktuell eingeloggter Mitarbeiter (Wird vom Server nicht mehr verwendet, da dieser einfach den eingeloggten Mitarbeiter auf Serverseite benutzt)
     * @param packungsgroesse Packungsgröße
     * @return Gibt den Massengutartikel zurück.
     * @throws MassengutException Falls der Bestand kein Vielfaches von der Packungsgröße ist
     */
    @Override
    public Massengutartikel massengutartikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter, int packungsgroesse) throws MassengutException {
        // Neuen Massengutartikel anlegen ist auf Serverseite mit dieser Kombination versehen, da die CUI
        // bereits zwischen Artikel und Massengutartikel im Methodenaufruf unterscheidet.
        sout.println("nm");
        sout.println(nummer);
        sout.println(bezeichnung);
        sout.println(bestand);
        sout.println(preis);
        sout.println(packungsgroesse);
        String input = "";
        try {
            input = sin.readLine();
            if (input.equals("Erfolg")){
                 Object artikelObject = liesArtikelVonServer();
                 if (artikelObject instanceof Massengutartikel){
                     return (Massengutartikel) artikelObject;
                 } else {
                     throw new RuntimeException("Unerwartetes Artikelobjekt beim Erstellen eines Massengutartikel");
                 }
            } else {
                int ex_bestand = Integer.parseInt(sin.readLine());
                int ex_packungsgroesse = Integer.parseInt(sin.readLine());
                throw new MassengutException(ex_bestand, ex_packungsgroesse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ruft die Bestandaendern-Methode auf dem Server auf. Diese kann nur vom Mitarbeiter ausgeführt werden.
     * @param artikel_nummer Artikelnummer
     * @param neuer_bestand Neuer Bestand des Artikels
     * @param aktuellerMitarbeiter Aktueller Mitarbeiter (wird nur der, der Serverseite benutzt)
     * @throws ArtikelExistiertNichtException Es konnte kein Artikel mit der Nummer gefunden werden
     * @throws UnbekanntesAccountObjektException Fehlerhaftes Account Objekt übergeben
     * @throws MassengutException Bestand ist kein Vielfaches der Packungsgröße
     */
    @Override
    public void bestandAendern(int artikel_nummer, int neuer_bestand, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException, MassengutException {
        sout.println("b");
        sout.println(artikel_nummer);
        sout.println(neuer_bestand);
        String input;
        try {
            input = sin.readLine();
            if (input.equals("ArtikelExistiertNichtException")){
                String bezeichnung = sin.readLine();
                throw new ArtikelExistiertNichtException(bezeichnung);
            }else if (input.equals("UnbekanntesAccountObjektException")){
                throw new UnbekanntesAccountObjektException();
            } else if (input.equals("MassengutException")){
                int ex_bestand = Integer.parseInt(sin.readLine());
                int ex_packungsgroesse = Integer.parseInt(sin.readLine());
                throw new MassengutException(ex_bestand, ex_packungsgroesse);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sucht nach der Beschreibung und gibt eine Liste der passenden Artikel zurück
     * @param bezeichnung Bezeichnung
     * @return Artikelliste mit den Artikeln, die der Beschreibung entsprechen
     */
    @Override
    public ArrayList<Artikel> artikelSuchen(String bezeichnung) {
        sout.println("s");
        sout.println(bezeichnung);
        ArrayList<Artikel> artikelListe = new ArrayList<Artikel>();
        try {
            int artikelAnzahl = Integer.parseInt(sin.readLine());
            for (int i = 0; i < artikelAnzahl; i++) {
                artikelListe.add(liesArtikelVonServer());
            }
            return artikelListe;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sendet den zu entfernenden Artikel an den Server
     * @param nummer Artikelnummer
     * @param bezeichnung Bezeichnung
     * @param aktuellerMitarbeiter Aktueller Mitarbeiter (es wird jedoch der eingeloggte auf Serverseite benutzt
     * @throws ArtikelExistiertNichtException Artikel konnte nicht gefunden werden
     * @throws UnbekanntesAccountObjektException Accountobjekt ist fehlerhaft
     */
    @Override
    public void artikelEntfernen(int nummer, String bezeichnung, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException {
        sout.println("rm");
        sout.println(nummer);
        sout.println(bezeichnung);
        try {
            String antwort = sin.readLine();
            if(antwort.equals("ArtikelExistiertNichtException")){
                String ex_bezeichnung = sin.readLine();
                throw new ArtikelExistiertNichtException(bezeichnung);
            } else if (antwort.equals("UnbekanntesAccountObjektException")){
                throw new UnbekanntesAccountObjektException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fragt eine Liste mit allen Artikeln beim Server an
     * @return Artikelliste
     */
    @Override
    public ArrayList<Artikel> gibAlleArtikel() {
        try {
            sout.println("p");
            ArrayList<Artikel> artikelListe= new ArrayList<Artikel>();
            int artikelAnzahl = Integer.parseInt(sin.readLine());
            for (int i = 0; i < artikelAnzahl; i++) {
                artikelListe.add(liesArtikelVonServer());
            }
            return artikelListe;
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Empfängt einen Artikel vom Server
     * @return Gibt den empfangenen Artikel zurück
     * @throws IOException Fehler bei der Übertragung
     */
    public Artikel liesArtikelVonServer() throws IOException {
        String antwort = "";
        antwort = sin.readLine();
        if(antwort.equals("M")){
            //Massengutartikel
            int nummer = Integer.parseInt(sin.readLine());
            String bezeichnung = sin.readLine();
            int bestand = Integer.parseInt(sin.readLine());
            float preis = Float.parseFloat(sin.readLine());
            int packungsgroesse = Integer.parseInt(sin.readLine());
            return new Massengutartikel(nummer, bezeichnung, bestand, preis, packungsgroesse);
        } else if (antwort.equals("A")) {
            //Artikel
            int nummer = Integer.parseInt(sin.readLine());
            String bezeichnung = sin.readLine();
            int bestand = Integer.parseInt(sin.readLine());
            float preis = Float.parseFloat(sin.readLine());
            return new Artikel(nummer, bezeichnung, bestand, preis);
        } else {
            return null;
        }
    }

    /**
     * Legt einen Artikel auf Serverseite in den Warenkorb
     * @param artikelnummer Artikelnummer
     * @param anzahl Anzahl
     * @param aktuellerKunde Aktueller Kunde (es wird der aktuelle Kunde auf Serverseite benutzt)
     * @throws ArtikelExistiertNichtException Artikel konnte nicht gefunden werden
     * @throws MassengutException Anzahl ist kein Vielfaches der Packungsgröße
     */
    @Override
    public void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde) throws ArtikelExistiertNichtException, MassengutException , BestandUeberschrittenException{
        sout.println("h");
        sout.println(artikelnummer);
        sout.println(anzahl);
        // Der Kunde wird hier nicht übergeben, da der Server den aktuell eingeloggten Kunden schon gespeichert hat
        String input = null;
        try{
            input = sin.readLine();
            if (input.equals("ArtikelExistiertNichtException"))
                throw new ArtikelExistiertNichtException(sin.readLine());
            else if (input.equals("MassengutException")) {
                int bestand = Integer.parseInt(sin.readLine());
                int packungsgroesse = Integer.parseInt(sin.readLine());
                throw new MassengutException(bestand, packungsgroesse);
            } else if (input.equals("BestandUeberschrittenException")) {
                Artikel artikel = liesArtikelVonServer();
                int ex_anzahl = Integer.parseInt(sin.readLine());
                throw new BestandUeberschrittenException(artikel, ex_anzahl);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sendet eine Anfrage zum Leeren des Warenkorbs an den Server
     * @param aktuellerKunde Aktueller Kunde (es wird der auf Serverseite eingeloggte benutzt)
     */
    @Override
    public void warenkorbLeeren(Kunde aktuellerKunde) {
        sout.println("l");
        String input = null;
        try {
            input = sin.readLine();
            if(input.equals("Fehler")){
                input = sin.readLine();
                System.out.println("Fehler");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Kauft den Warenkorb auf Serverseite und empfängt die Rechnung
     * @param aktuellerKunde Aktueller Kunde (es wird der auf Serverseite eingeloggte benutzt)
     * @return Rechnung
     * @throws UnbekanntesAccountObjektException Fehlerhaftes Account Objekt
     * @throws MassengutException Falls eine Anzahl kein Vielfaches der entsprechenden Packungsgröße ist
     * @throws ArtikelExistiertNichtException Ein Artikel konnte nicht gefunden werden
     */
    @Override
    public Rechnung warenkorbKaufen(Kunde aktuellerKunde) throws UnbekanntesAccountObjektException, MassengutException, ArtikelExistiertNichtException {
        sout.println("k");
        String input = "";
        try {
            input = sin.readLine();
            if(input.equals("Erfolg")){
                return liesRechnungVonServer();
            } else {
                input = sin.readLine();
                System.out.println("Fehler: " + input);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Empfängt eine Rechnung vom Server
     * @return Rechnung
     * @throws IOException Fehler bei der Übertragung
     */
    private Rechnung liesRechnungVonServer() throws IOException {
        Kunde kunde = liesKundeVonServer();
        HashMap<Artikel, Integer> warenkorb = liesWarenkorbVonServer();
        float gesamt = Float.parseFloat(sin.readLine());
        return new Rechnung(kunde, gesamt, warenkorb);
    }

    /**
     * Empfängt ein Warenkorbobjekt vom Server
     * @return Gibt den Warenkorb als reine Hashmap aus, da das entsprechende Kundenobjekt nur auf Serverseite existiert
     * @throws IOException Fehler bei der Übertragung
     */
    private HashMap<Artikel, Integer> liesWarenkorbVonServer() throws IOException {
        HashMap<Artikel, Integer> warenkorb = new HashMap<>();
        String input = null;
        input = sin.readLine();
        // Wie viele Einträge hat der Warenkorb
        int anzahlArtikel = Integer.parseInt(input);
        for (int i = 0; i < anzahlArtikel; i++) {
            Artikel artikel = liesArtikelVonServer();
            int anzahl = Integer.parseInt(sin.readLine());
            warenkorb.put(artikel, anzahl);
        }
        return warenkorb;
    }

    /**
     * Sendet die neue Anzahl eines Artikels im Warenkorb an den Server
     * @param aktuellerKunde Aktueller Kunde (es wird der auf Serverseite eingeloggte benutzt)
     * @param bezeichnung Bezeichnung des Artikels
     * @param neuerBestand Neue Anzahl
     * @throws MassengutException Anzahl ist kein Vielfaches der Packungsgröße
     * @throws ArtikelExistiertNichtException Artikel konnte nicht gefunden werden
     * @throws BestandUeberschrittenException Neue Anzahl kann nicht größer als der Bestand sein
     */
    @Override
    public void warenkorbVeraendern(Kunde aktuellerKunde, String bezeichnung, int neuerBestand) throws MassengutException, ArtikelExistiertNichtException, BestandUeberschrittenException {
        sout.println("v");
        sout.println(bezeichnung);
        sout.println(neuerBestand);
        String input = null;
        try{
            input = sin.readLine();
            if(input.equals("MassengutException")){
                int bestand = Integer.parseInt(sin.readLine());
                int packungsgroesse = Integer.parseInt(sin.readLine());
                throw new MassengutException(bestand, packungsgroesse);
            } else if (input.equals("ArtikelExistiertNichtException")){
                String e_bezeichnung = sin.readLine();
                throw new ArtikelExistiertNichtException(e_bezeichnung);
            } else if (input.equals("BestandUeberschrittenException")) {
                Artikel artikel = liesArtikelVonServer();
                int anzahl = Integer.parseInt(sin.readLine());
                throw new BestandUeberschrittenException(artikel, anzahl);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sendet eine Anfrage zum Entfernen eines Artikels aus dem Warenkorb zum Server
     * @param aktuellerKunde Aktueller Kunde (es wird der auf Serverseite eingeloggte benutzt)
     * @param bezeichnung Bezeichnung des Artikels
     * @throws ArtikelExistiertNichtException Artikel konnte nicht gefunden werden
     */
    @Override
    public void artikelAusWarenkorbEntfernen(Kunde aktuellerKunde, String bezeichnung) throws ArtikelExistiertNichtException {
        // Artikel können einfach entfernt werden, indem wir die Anzahl auf 0 setzen.
        // Dadurch ist die implementierung auf dem Server leichter, da die Eshop-Seite eine Anzahl von 0 handeln kann
        try {
            warenkorbVeraendern(aktuellerKunde, bezeichnung, 0);
        } catch (MassengutException | BestandUeberschrittenException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Holt sich den Warenkorb vom Server
     * @param aktuellerKunde Aktueller Kunde (es wird der auf Serverseite eingeloggte verwendet)
     * @return Gibt eine Hashmap als Warenkorb zurück, da das Kundenobjekt nicht übertragen wird
     */
    @Override
    public HashMap<Artikel, Integer> gibWarenkorb(Kunde aktuellerKunde) {
        sout.println("w");
        String input = null;
        HashMap<Artikel, Integer> warenkorb = null;
        try {
            warenkorb = liesWarenkorbVonServer();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return warenkorb;
    }

    /**
     * Empfängt den kompletten Eventlog
     * @return gibt eine Liste von Ereignissen zurück
     */
    @Override
    public ArrayList<Ereignis> eventlogAusgeben() {
        ArrayList<Ereignis> eventlog = new ArrayList<Ereignis>();
        sout.println("l");
        String input = null;
        try {
            input = sin.readLine();
            int anzahlEvents = Integer.parseInt(input);
            for (int i = 0; i < anzahlEvents; i++) {
                Ereignis event = liesEventVonServer();
                eventlog.add(event);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return eventlog;
    }

    /**
     * Empfängt ein Ereignisobjekt vom Server
     * @return Ereignis
     */
    private Ereignis liesEventVonServer() {
        try {
            String input = sin.readLine();
            EreignisTyp ereignisTyp = EreignisTyp.valueOf(input);
            input = sin.readLine();
            AccountTyp accountTyp = AccountTyp.valueOf(input);
            String benutzername = sin.readLine();
            int delta = Integer.parseInt(sin.readLine());
            String dateString = sin.readLine();
            LocalDate datum = LocalDate.parse(dateString);
            String bezeichnung = sin.readLine();
            return new Ereignis(ereignisTyp, accountTyp, benutzername, delta, datum, bezeichnung);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tut hier nichts, da es nur auf Serverseite aufgerufen werden kann
     */
    @Override
    public void schreibeKunde() throws IOException {
        // Leer weil diese Funktion nur auf Serverseite aufgerufen werden kann
    }

    /**
     * Tut hier nichts, da es nur auf Serverseite aufgerufen werden kann
     */
    @Override
    public void schreibeMitarbeiter() throws IOException {
        // Leer weil diese Funktion nur auf Serverseite aufgerufen werden kann
    }

    /**
     * Tut hier nichts, da es nur auf Serverseite aufgerufen werden kann
     */
    @Override
    public void schreibeArtikel() throws IOException {
        // Leer weil diese Funktion nur auf Serverseite aufgerufen werden kann
    }

    /**
     * Tut hier nichts, da es nur auf Serverseite aufgerufen werden kann
     */
    @Override
    public void schreibeEreignis() throws IOException {
        // Leer weil diese Funktion nur auf Serverseite aufgerufen werden kann
    }

    /**
     * Sendet eine Anfrage zum Sichern der Daten an den Server
     * @throws IOException Fehler bei der Übertragung oder Speicherung
     */
    @Override
    public void sichereDaten() throws IOException {
        sout.println("z");
        String antwort = sin.readLine();
        if (antwort.equals("Fehler")){
            String ex_message = sin.readLine();
            throw new IOException(ex_message);
        }
    }

    /**
     * Holt sich die Bestandshistorie eines Artikels vom Server
     * @param Artikelnummer Artikelnummer
     * @return Gibt eine Liste von Integern zurück
     * @throws ArtikelExistiertNichtException Artikel konnte nicht gefunden werden
     */
    @Override
    public ArrayList<Integer> getBestandhistorie(int Artikelnummer) throws ArtikelExistiertNichtException {
        ArrayList<Integer> historie = new ArrayList<Integer>();
        sout.println("h");
        sout.println(Artikelnummer);
        int laenge = 0;
        int bestand;
        try {
            String antwort  = sin.readLine();
            if(antwort.equals("Erfolg")){
                laenge = Integer.parseInt(sin.readLine());
                for (int i = 0; i < laenge; i++) {
                    bestand = Integer.parseInt(sin.readLine());
                    historie.add(bestand);
                }
            } else {
                String bezeichnung = sin.readLine();
                throw new ArtikelExistiertNichtException(bezeichnung);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return historie;
    }

    /**
     * Sendet eine Logout-Anfrage an den Server
     */
    public void logout(){
        sout.println("a");
    }

    /**
     * Bittet den Server die Verbindung zu trennen
     * @throws IOException Fehler bei der Übertragung
     */
    public void disconnect() throws IOException {
        // Kennzeichen für gewählte Aktion senden
        sout.println("q");
        // (Parameter sind hier nicht zu senden)

        // Antwort vom Server lesen:
        String antwort = "Fehler";
        try {
            antwort = sin.readLine();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println(antwort);
    }
}
