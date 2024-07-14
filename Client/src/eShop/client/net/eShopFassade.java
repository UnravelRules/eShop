package eShop.client.net;

import eShop.common.entities.*;
import eShop.common.exceptions.*;
import eShop.common.interfaces.eShopInterface;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
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
     * @param name
     * @param str
     * @param plz
     * @param benutzer
     * @param passwort
     * @return
     * @throws KundeExistiertBereitsException
     * @throws FehlendeEingabenException
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
            } else {
                // Registrierung Fehlgeschlagen
                throw new KundeExistiertBereitsException(benutzer);
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

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
     * @param benutzername
     * @param passwort
     * @return
     * @throws KundeExistiertNichtException
     */
    @Override
    public Kunde kundeEinloggen(String benutzername, String passwort) throws KundeExistiertNichtException {
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
                // Registrierung Fehlgeschlagen
                throw new KundeExistiertNichtException("Benutzername nicht gefunden");
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param benutzername
     * @param passwort
     * @return
     * @throws MitarbeiterExistiertNichtException
     */
    @Override
    public Mitarbeiter mitarbeiterEinloggen(String benutzername, String passwort) throws MitarbeiterExistiertNichtException {
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
                // Registrierung Fehlgeschlagen
                throw new MitarbeiterExistiertNichtException(benutzername);
            }
        } catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

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
     * @param nummer
     * @param name
     * @param benutzer
     * @param passwort
     * @return
     * @throws MitarbeiterExistiertBereitsException
     */
    @Override
    public Mitarbeiter mitarbeiterRegistrieren(int nummer, String name, String benutzer, String passwort) throws MitarbeiterExistiertBereitsException {
        return null;
    }

    /**
     * @param benutzername
     */
    @Override
    public void mitarbeiterEntfernen(String benutzername) {

    }

    /**
     * @param nummer
     * @param bezeichnung
     * @param bestand
     * @param preis
     * @param aktuellerMitarbeiter
     * @return
     * @throws RuntimeException
     */
    @Override
    public Artikel artikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter) throws RuntimeException {
        return null;
    }

    /**
     * @param nummer
     * @param bezeichnung
     * @param bestand
     * @param preis
     * @param aktuellerMitarbeiter
     * @param packungsgroesse
     * @return
     * @throws MassengutException
     */
    @Override
    public Massengutartikel massengutartikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter, int packungsgroesse) throws MassengutException {
        return null;
    }

    /**
     * @param artikel_nummer
     * @param neuer_bestand
     * @param aktuellerMitarbeiter
     * @throws ArtikelExistiertNichtException
     * @throws UnbekanntesAccountObjektException
     * @throws MassengutException
     */
    @Override
    public void bestandAendern(int artikel_nummer, int neuer_bestand, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException, MassengutException {

    }

    /**
     * Sucht nach der Beschreibung und gibt eine Liste der passenden Artikel zurück
     * @param bezeichnung
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
     * @param nummer
     * @param bezeichnung
     * @param aktuellerMitarbeiter
     * @throws ArtikelExistiertNichtException
     * @throws UnbekanntesAccountObjektException
     */
    @Override
    public void artikelEntfernen(int nummer, String bezeichnung, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException {

    }

    /**
     * @return Arraylist<Artikel>
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
     * @param artikelnummer
     * @param anzahl
     * @param aktuellerKunde
     * @throws ArtikelExistiertNichtException
     * @throws MassengutException
     */
    @Override
    public void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde) throws ArtikelExistiertNichtException, MassengutException, NegativerBestandException {
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
            } else if (input.equals("NegativerBestandException")) {
                Artikel artikel = liesArtikelVonServer();
                int ex_anzahl = Integer.parseInt(sin.readLine());
                throw new NegativerBestandException(artikel, ex_anzahl);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param aktuellerKunde
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
     * @param aktuellerKunde
     * @return
     * @throws UnbekanntesAccountObjektException
     * @throws MassengutException
     * @throws ArtikelExistiertNichtException
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

    private Rechnung liesRechnungVonServer() throws IOException {
        Kunde kunde = liesKundeVonServer();
        HashMap<Artikel, Integer> warenkorb = liesWarenkorbVonServer();
        float gesamt = Float.parseFloat(sin.readLine());
        return new Rechnung(kunde, gesamt, warenkorb);
    }

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
     * @param aktuellerKunde
     * @param bezeichnung
     * @param neuerBestand
     * @throws MassengutException
     * @throws ArtikelExistiertNichtException
     * @throws NegativerBestandException
     */
    @Override
    public void warenkorbVeraendern(Kunde aktuellerKunde, String bezeichnung, int neuerBestand) throws MassengutException, ArtikelExistiertNichtException, NegativerBestandException {
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
            } else if (input.equals("NegativerBestandException")) {
                Artikel artikel = liesArtikelVonServer();
                int anzahl = Integer.parseInt(sin.readLine());
                throw new NegativerBestandException(artikel, anzahl);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param aktuellerKunde
     * @param bezeichnung
     * @throws ArtikelExistiertNichtException
     */
    @Override
    public void artikelAusWarenkorbEntfernen(Kunde aktuellerKunde, String bezeichnung) throws ArtikelExistiertNichtException {
        // Artikel können einfach entfernt werden, indem wir die Anzahl auf 0 setzen.
        // Dadurch ist die implementierung auf dem Server leichter, da die Eshop-Seite eine Anzahl von 0 handeln kann
        try {
            warenkorbVeraendern(aktuellerKunde, bezeichnung, 0);
        } catch (MassengutException | NegativerBestandException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param aktuellerKunde
     * @return
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
     * @return
     */
    @Override
    public ArrayList<Ereignis> eventlogAusgeben() {
        return null;
    }

    /**
     * @throws IOException
     */
    @Override
    public void schreibeKunde() throws IOException {

    }

    /**
     * @throws IOException
     */
    @Override
    public void schreibeMitarbeiter() throws IOException {

    }

    /**
     * @throws IOException
     */
    @Override
    public void schreibeArtikel() throws IOException {

    }

    /**
     * @throws IOException
     */
    @Override
    public void schreibeEreignis() throws IOException {

    }

    /**
     * @throws IOException
     */
    @Override
    public void sichereDaten() throws IOException {

    }

    /**
     * @param Artikelnummer
     * @return
     * @throws ArtikelExistiertNichtException
     */
    @Override
    public ArrayList<Integer> getBestandhistorie(int Artikelnummer) throws ArtikelExistiertNichtException {
        return null;
    }

    public void logout(){
        sout.println("a");
    }

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
