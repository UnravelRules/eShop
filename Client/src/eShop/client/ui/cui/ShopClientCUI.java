package eShop.client.ui.cui;
import eShop.client.net.eShopFassade;
import eShop.common.exceptions.*;
import eShop.common.entities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.InetAddress;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Klasse dient als Benutzungsschnittstelle für den Nutzer
 * kümmert sich um die verschiedenen Menüs, sowohl für Kunden als auch Mitarbeiter
 *
 * @author Fabian
 */
public class ShopClientCUI {

    public static final int DEFAULT_PORT = 6789;

    private eShopFassade eshop;
    private BufferedReader in;
    private Kunde aktuellerKunde;
    private Mitarbeiter aktuellerMitarbeiter;

    // Konstruktor (Datei als Parameter geben, BufferedReader)
    public ShopClientCUI(String host, int port) throws IOException {
        eshop = new eShopFassade(host, port);

        // Stream-Objekt fuer Texteingabe ueber Konsolenfenster erzeugen
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    private String liesEingabe() throws IOException {
        // einlesen von Konsole
        return in.readLine();
    }

    // Menü zum Registrieren/Anmelden
    // entweder registriert sich ein Kunde neu, oder ein Kunde loggt sich ein, oder ein Mitarbeiter loggt sich ein
    private void gibAnmeldemenueAus(){
        System.out.print("Befehle: \n  Als Kunde registrieren:  'r'");
        System.out.print("         \n  Als Kunde einloggen: 'k'");
        System.out.print("         \n  Als Mitarbeiter einloggen: 'm'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Beenden:        'q'");
        System.out.print("> "); // Prompt
    }


    // Eingabe der Anmeldung verarbeiten, dann je nach Eingabe prüfen und in neues Menü weiterleiten
    private int anmeldungVerarbeiten(String line) throws IOException{
        switch(line){
            case "r":
                gibRegistrierenmenueAus();
                break;
            case "k":
                System.out.print("Benutzername: ");
                String kundeBenutzername = liesEingabe();
                System.out.print("Passwort: ");
                String kundePasswort = liesEingabe();
                try{
                    aktuellerKunde = eshop.kundeEinloggen(kundeBenutzername, kundePasswort);
                    System.out.println("Willkommen zurück, " + aktuellerKunde.getName());
                    return 1;
                } catch(LoginFehlgeschlagenException e){
                    System.out.println("Fehler: " + e.getMessage());
                    return 0;
                }
            case "m":
                System.out.print("Benutzername: ");
                String mitarbeiterBenutzername = liesEingabe();
                System.out.print("Passwort: ");
                String mitarbeiterPasswort = liesEingabe();
                try{
                    Mitarbeiter m = eshop.mitarbeiterEinloggen(mitarbeiterBenutzername, mitarbeiterPasswort);
                    System.out.println("Willkommen zurück, " + m.getName());
                    aktuellerMitarbeiter = m;
                    return 2;
                } catch(LoginFehlgeschlagenException e){
                    System.out.println("Fehler beim Einloggen");
                    return 0;
                }
        }
        return 0;
    }

    // Menue zum Registrieren eines neuen Kunden
    private void gibRegistrierenmenueAus() throws IOException {
        System.out.print("Name: ");
        String name = liesEingabe();
        System.out.print("Straße: ");
        String strasse = liesEingabe();
        System.out.print("Postleitzahl: ");
        String plz = liesEingabe();
        System.out.print("Benutzername: ");
        String benutzername = liesEingabe();
        System.out.print("Passwort: ");
        String passwort = liesEingabe();

        try{
            Kunde k = eshop.kundeRegistrieren(name, strasse, plz, benutzername, passwort);
            System.out.println("Herzlich Willkommen, " + k.getName());
        } catch (Exception e) {
            System.out.println("Fehler beim Registrieren");
            e.printStackTrace();
        }
    }


    // alle Optionen für einen Kunden, bspw. Artikel in Warenkorb hinzufügen, Warenkorb kaufen usw.
    private void kundenMenue(){
        System.out.print("Befehle: \n  Ein Artikel suchen:  's'");
        System.out.print("         \n  Alle Artikel ausgeben: 'p'");
        System.out.print("         \n  Ein Artikel in den Warenkorb hinzufügen: 'h'");
        System.out.print("         \n  Warenkorb anzeigen: 'w'");
        System.out.print("         \n  Warenkorb verändern: 'v'");
        System.out.print("         \n  Produkte im Warenkorb kaufen: 'k'");
        System.out.print("         \n  Warenkorb leeren: 'l'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Ausloggen:        'a'");
        System.out.print("> "); // Prompt
    }

    private void kundeneingabeVerarbeiten(String line) throws IOException, ArtikelExistiertNichtException {
        int artikelnummer;
        String bezeichnung;
        switch(line) {
            case "s":
                // Artikel suchen
                System.out.println("Bezeichnung des Artikels: ");
                String input = liesEingabe();
                List<Artikel> artikelListe = eshop.artikelSuchen(input);
                if(artikelListe.isEmpty()){
                    System.out.println("Keine Artikel mit dieser Bezeichnung gefunden!");
                    break;
                }
                gibArtikelListeAus(artikelListe);
                break;
            case "p":
                // Alle Artikel ausgeben
                artikelListe = eshop.gibAlleArtikel();
                gibArtikelListeAus(artikelListe);
                break;
            case "h":
                // Artikel zum Warenkorb hinzufuegen
                System.out.print("Artikelnummer: ");
                artikelnummer = Integer.parseInt(liesEingabe());
                System.out.print("Wie viel?: ");
                int anzahl = Integer.parseInt(liesEingabe());
                try {
                    eshop.artikelInWarenkorb(artikelnummer, anzahl, aktuellerKunde);
                } catch (ArtikelExistiertNichtException e){
                    System.out.println("Kein Artikel mit dieser Nummer gefunden");
                } catch (MassengutException e){
                    System.out.println("Anzahl ist nicht mit Packungsgroesse kompatibel");
                } catch (BestandUeberschrittenException e){
                    System.out.println("Die Anzahl überschreitet den Artikelbestand");
                }
                break;
            case "w":
                // Warenkorb ausgeben
                HashMap<Artikel, Integer> warenkorb = eshop.gibWarenkorb(this.aktuellerKunde);
                gibWarenkorbAus(warenkorb);
                break;
            case "v":
                System.out.println("Bezeichnung: ");
                bezeichnung = liesEingabe();
                System.out.println("Neue Anzahl (0 um Eintrag zu löschen): ");
                int neuerBestand = Integer.parseInt(liesEingabe());
                try {
                    eshop.warenkorbVeraendern(aktuellerKunde, bezeichnung, neuerBestand);
                } catch (MassengutException e){
                    System.out.println("Anzahl nicht mit Packungsgroesse kompatibel");
                } catch (BestandUeberschrittenException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "k":
                // Warenkorb kaufen
                try {
                    Rechnung r = eshop.warenkorbKaufen(aktuellerKunde);
                    rechnungAnzeigen(r);
                } catch (UnbekanntesAccountObjektException e) {
                    System.out.println("Unbekanntes Account Objekt beim Update des Eventlogs");
                } catch (MassengutException e) {
                    System.out.println("Neuer Bestand ist nicht mit Packungsgröße kompatibel");
                }

                break;
            case "l":
                // Warenkorb leeren
                eshop.warenkorbLeeren(aktuellerKunde);
                break;
        }
    }

    // alle Optionen für einen Mitarbeiter, bspw. neue Artikel anlegen, Bestand ändern, neuen Mitarbeiter anlegen usw.
    private void mitarbeiterMenue(){
        System.out.print("Befehle: \n  Neuen Artikel anlegen:  'n'");
        System.out.print("         \n  Alle Artikel ausgeben: 'p'");
        System.out.print("         \n  Artikel suchen: 's'");
        System.out.print("         \n  Artikel entfernen: 'e'");
        System.out.print("         \n  Bestand aendern: 'b'");
        System.out.print("         \n  Mitarbeiter anlegen: 'm'");
        System.out.print("         \n  Mitarbeiter entfernen: 'd'");
        System.out.print("         \n  Eventlog ausgeben: 'l'");
        System.out.print("         \n  Bestandshistorie ausgeben: 'h'");
        System.out.print("         \n  Daten sichern: 'z'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Ausloggen:        'a'");
        System.out.print("> "); // Prompt
    }

    private void mitarbeitereingabeVerarbeiten(String line) throws IOException {
        List<Artikel> artikelListe;
        String bezeichnung;
        switch(line) {
            case "n":
                // Neuen Artikel hinzufügen
                System.out.println("Massengutartikel: J/N");
                char massengut = liesEingabe().charAt(0);
                System.out.print("Artikelnummer: ");
                int artikelNummer = Integer.parseInt(liesEingabe());
                System.out.print("Bezeichnung: ");
                bezeichnung = liesEingabe();
                System.out.print("Bestand des Artikels: ");
                int bestand = Integer.parseInt(liesEingabe());
                System.out.print("Preis des Artikels: ");
                float preis = Float.parseFloat(liesEingabe());


                if(massengut == 'N' || massengut == 'n') {
                    try {
                        eshop.artikelAnlegen(artikelNummer, bezeichnung, bestand, preis, aktuellerMitarbeiter);
                        break;
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                } else if(massengut == 'J' || massengut == 'j') {
                    try {
                        System.out.print("Packungsgröße: ");
                        int packungsgroesse = Integer.parseInt(liesEingabe());
                        eshop.massengutartikelAnlegen(artikelNummer, bezeichnung, bestand, preis, aktuellerMitarbeiter, packungsgroesse);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    } catch (MassengutException exception){
                        System.out.println("Fehler: "+ exception.getMessage());
                    }
                }
                break;

            case "p":
                // ALle Artikel ausgeben
                artikelListe = eshop.gibAlleArtikel();
                gibArtikelListeAus(artikelListe);
                break;

            case "s":
                // Suchen von Artikel
                System.out.println("Bezeichnung des Artikels: ");
                String input = liesEingabe();
                artikelListe = eshop.artikelSuchen(input);
                if(artikelListe.isEmpty()){
                    System.out.println("Keine Artikel mit dieser Bezeichnung gefunden!");
                    break;
                }
                gibArtikelListeAus(artikelListe);
                break;

            case "e":
                // Entfernen von Artikel
                System.out.println("Nummer des Artikels: ");
                int nummer = Integer.parseInt(liesEingabe());
                System.out.println("Bezeichnung des Artikels: ");
                bezeichnung = liesEingabe();
                try{
                    eshop.artikelEntfernen(nummer, bezeichnung, aktuellerMitarbeiter);
                } catch(UnbekanntesAccountObjektException exception){
                    System.out.println("Unbekanntes Account Objekt beim Updaten vom Eventlog");
                } catch(ArtikelExistiertNichtException exception){
                    System.out.println("Artikel existiert nicht");
                }

                break;

            case "b":
                // Bestand Aendern
                System.out.println("Nummer des Artikels: ");
                int a_nummer = Integer.parseInt(liesEingabe());
                System.out.println("Neuer Bestand:");
                int n_bestand = Integer.parseInt(liesEingabe());
                String ausgabe = "";
                try {
                    eshop.bestandAendern(a_nummer, n_bestand, aktuellerMitarbeiter);
                    ausgabe = String.format("Bestand von Artikel %d auf %d gesetzt", a_nummer, n_bestand);

                } catch (ArtikelExistiertNichtException exception) {
                    ausgabe = String.format("Artikel mit Nummer %d konnte nicht gefunden werden", a_nummer);
                } catch (UnbekanntesAccountObjektException exception) {
                    ausgabe = "Unbekanntes Account Objekt beim Updaten vom Eventlog";
                } catch (MassengutException exception){
                    ausgabe = String.format("Bestand kann nicht geändert werden, da neuer Bestand %d nicht kompatibel mit Packungsgröße ist", n_bestand);
                }
                System.out.println(ausgabe);
                break;

            case "m":
                // Neuen Mitarbeiter registrieren
                System.out.print("Mitarbeiternummer: ");
                int mitarbeiterNummer = Integer.parseInt(liesEingabe());
                System.out.print("Name: ");
                String name = liesEingabe();
                System.out.print("Benutzername: ");
                String benutzername = liesEingabe();
                System.out.print("Passwort: ");
                String passwort = liesEingabe();
                try {
                    Mitarbeiter m = eshop.mitarbeiterRegistrieren(mitarbeiterNummer, name, benutzername, passwort);
                    System.out.println("Neuer Mitarbeiter " + m.getName() + " angelegt");
                    break;
                } catch (MitarbeiterExistiertBereitsException | FehlendeEingabenException e){
                    System.out.println("Mitarbeiter existiert bereits!");
                    e.printStackTrace();
                }
                break;

            case "d":
                // Mitarbeiter löschen
                System.out.print("Benutzername des Mitarbeiters, der gelöscht werden soll: ");
                String mitarbeiter = liesEingabe();
                eshop.mitarbeiterEntfernen(mitarbeiter);
                break;
            case "l":
                // Eventlog ausgeben
                System.out.println("Eventlog:");
                ArrayList<Ereignis> eventlog = eshop.eventlogAusgeben();
                for(Ereignis event: eventlog){
                    String output = String.format("    %s", event);
                    System.out.println(output);
                }
                break;
            case "h":
                // Bestandshistorie ausgeben
                System.out.print("Bestandshistorie des Artikels mit Nummer: ");
                int artikel_nummer = Integer.parseInt(liesEingabe());
                try {
                    ArrayList<Integer> bestands_historie = eshop.getBestandhistorie(artikel_nummer);
                    LocalDate datum = LocalDate.now();
                    for(int bestands_item: bestands_historie){
                        String output = String.format("%s: Bestand %d",datum, bestands_item);
                        System.out.println(output);
                        datum = datum.minusDays(1);
                    }
                } catch (ArtikelExistiertNichtException e) {
                    System.out.println("Artikelnummer existiert nicht");
                }
            case "z":
                eshop.sichereDaten();
                break;
        }
    }

    private void gibArtikelListeAus(List<Artikel> artikelListe){
        if(artikelListe.isEmpty()) {
            System.out.println("Keine Artikel vorhanden");
        } else {
            for(Artikel a : artikelListe) {
                System.out.println(a);
            }
        }
    }

    private void gibWarenkorbAus(HashMap<Artikel, Integer> warenkorb){
        for (Map.Entry<Artikel, Integer> eingabe : warenkorb.entrySet()){
            Artikel a = eingabe.getKey();
            int anzahl = eingabe.getValue();
            System.out.printf("Bezeichnung: %s, Anzahl: %d, Preis: %.2f€, Gesamtpreis: %.2f€%n", a.getBezeichnung(), anzahl , a.getPreis(), a.getPreis() * anzahl);
        }
    }

    private void rechnungAnzeigen(Rechnung r){
        int kundennummer = r.getKunde().getNummer();
        String name = r.getKunde().getName();
        String strasse = r.getKunde().getStrasse();
        String plz = r.getKunde().getPlz();
        String datum = String.valueOf(r.getDatum());
        HashMap<Artikel, Integer> gekaufteArtikel = r.getGekaufteArtikel();
        float gesamtpreis = r.getGesamtpreis();

        System.out.println("------------------------------------------------------");
        System.out.printf("Rechnung vom Kunden: %s %d  |  Adresse: %s %s  |  am %s%n", name, kundennummer, strasse, plz, datum);
        gibWarenkorbAus(gekaufteArtikel);
        System.out.printf("Gesamtpreis: %.2f€%n", gesamtpreis);
        System.out.println("------------------------------------------------------");
    }

    /**
     * Methode zur Ausführung der Hauptschleife:
     * - Menü ausgeben
     * - Eingabe des Benutzers einlesen
     * - Eingabe verarbeiten und basierend darauf ein weiteres Menü ausgeben
     * (sorgt also dafür, dass ein Kunde (Identifier: 1) nach dem Login ins Kundenmenü weitergeleitet wird,
     * bzw. ein Mitarbeiter (Identifier: 2) ins Mitarbeitermenü weitergeleitet wird)
     * @author Fabian
     */

    public void run(){
        String input = "";
        int benutzertyp = 0;

        do {
            gibAnmeldemenueAus();
            try{
                input = liesEingabe();
                benutzertyp = anmeldungVerarbeiten(input);
            } catch(IOException e){
                e.printStackTrace();
            }
            if(benutzertyp == 1){
                do{
                    kundenMenue();
                    try{
                        input = liesEingabe();
                        kundeneingabeVerarbeiten(input);
                    } catch (IOException | ArtikelExistiertNichtException e){
                        e.printStackTrace();
                    }
                } while(!input.equals("a"));
                eshop.logout();

            } else if(benutzertyp == 2){
                do{
                    mitarbeiterMenue();
                    try{
                        input = liesEingabe();
                        mitarbeitereingabeVerarbeiten(input);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } while (!input.equals("a"));
                eshop.logout();
            }
        } while (!input.equals("q"));
        try {
            eshop.sichereDaten();
            eshop.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 0;
        String host = null;
        InetAddress ia = null;

        // Host- und Port-Argumente einlesen, wenn angegeben
        if (args.length > 2) {
            System.out.println("Aufruf: java BibClientGUI [<hostname> [<port>]]");
            System.exit(0);
        }
        switch (args.length) {
            case 0:
                try {
                    ia = InetAddress.getLocalHost();
                } catch (Exception e) {
                    System.out.println("XXX InetAdress-Fehler: " + e);
                    System.exit(0);
                }
                host = ia.getHostName(); // host ist lokale Maschine
                port = DEFAULT_PORT;
                break;
            case 1:
                port = DEFAULT_PORT;
                host = args[0];
                break;
            case 2:
                host = args[0];
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.out
                            .println("Aufruf: java BibClientGUI [<hostname> [<port>]]");
                    System.exit(0);
                }
        }

        // CUI auf Starten und mit Server auf Host und Port verbinden
        ShopClientCUI cui;
        try {
            cui = new ShopClientCUI(host, port);
            cui.run();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}