package eShop.local.ui.cui;
import eShop.local.domain.eShop;
import eShop.local.domain.exceptions.*;
import eShop.local.entities.Artikel;
import eShop.local.entities.Kunde;
import eShop.local.entities.Mitarbeiter;
import eShop.local.entities.Warenkorb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.SQLOutput;
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
    private eShop eshop = new eShop();
    private BufferedReader in;
    private Kunde aktuellerKunde;

    // Konstruktor (Datei als Parameter geben, BufferedReader)
    public ShopClientCUI(){
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
                    Kunde k = eshop.kundeEinloggen(kundeBenutzername, kundePasswort);
                    System.out.println("Willkommen zurück, " + k.getName());
                    aktuellerKunde = k;
                    return 1;
                } catch(KundeExistiertNichtException e){
                    System.out.println("Fehler beim Einloggen");
                    e.printStackTrace();
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
                    return 2;
                } catch(MitarbeiterExistiertNichtException e){
                    System.out.println("Fehler beim Einloggen");
                    e.printStackTrace();
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
        } catch (KundeExistiertBereitsException e) {
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
        System.out.print("         \n  Produkte im Warenkorb kaufen: 'k'");
        System.out.print("         \n  Warenkorb leeren: 'l'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Ausloggen:        'a'");
        System.out.print("> "); // Prompt
    }

    private void kundeneingabeVerarbeiten(String line) throws IOException {
        switch(line) {
            case "s":
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
                artikelListe = eshop.gibAlleArtikel();
                gibArtikelListeAus(artikelListe);
                break;
            case "h":
                System.out.println("Artikelnummer: ");
                int artikelnummer = Integer.parseInt(liesEingabe());
                System.out.println("Wie viel?");
                int anzahl = Integer.parseInt(liesEingabe());
                eshop.artikelInWarenkorb(artikelnummer, anzahl, aktuellerKunde);
                break;
            case "w":
                eshop.warenkorbLeeren(aktuellerKunde);
                break;
            case "k":
                // Befehle
            case "l":
                aktuellerKunde.getWarenkorb().getHashmap().clear();
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
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Ausloggen:        'a'");
        System.out.print("> "); // Prompt
    }

    private void mitarbeitereingabeVerarbeiten(String line) throws IOException {
        List<Artikel> artikelListe;
        String bezeichnung;
        switch(line) {
            case "n":
                System.out.println("Artikelnummer: ");
                int artikelNummer = Integer.parseInt(liesEingabe());
                System.out.println("Bezeichnung: ");
                bezeichnung = liesEingabe();
                System.out.println("Bestand des Artikels: ");
                int bestand = Integer.parseInt(liesEingabe());
                System.out.println("Preis des Artikels: ");
                float preis = Float.parseFloat(liesEingabe());

                try {
                    eshop.artikelAnlegen(artikelNummer, bezeichnung, bestand, preis);
                    break;
                } catch (ArtikelExistiertBereitsException e){
                    System.out.println("Artikel existiert bereits!");
                    e.printStackTrace();
                }

            case "p":
                artikelListe = eshop.gibAlleArtikel();
                gibArtikelListeAus(artikelListe);
                break;

            case "s":
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
                System.out.println("Nummer des Artikels: ");
                int nummer = Integer.parseInt(liesEingabe());
                System.out.println("Bezeichnung des Artikels: ");
                bezeichnung = liesEingabe();
                eshop.artikelEntfernen(nummer, bezeichnung);

                break;

            case "b":
                System.out.println("Nummer des Artikels: ");
                int a_nummer = Integer.parseInt(liesEingabe());
                System.out.println("Neuer Bestand:");
                int n_bestand = Integer.parseInt(liesEingabe());
                String ausgabe = "";
                try {
                    eshop.bestandAendern(a_nummer, n_bestand);
                    ausgabe = String.format("Bestand von Artikel %d auf %d gesetzt", a_nummer, n_bestand);

                } catch (ArtikelExistiertNichtException exception) {
                    ausgabe = String.format("Artikel mit Nummer %d konnte nicht gefunden werden", a_nummer);
                }
                System.out.println(ausgabe);
                break;

            case "m":
                System.out.println("Mitarbeiternummer: ");
                int mitarbeiterNummer = Integer.parseInt(liesEingabe());
                System.out.println("Name: ");
                String name = liesEingabe();
                System.out.println("Benutzername: ");
                String benutzername = liesEingabe();
                System.out.println("Passwort: ");
                String passwort = liesEingabe();
                try {
                    Mitarbeiter m = eshop.mitarbeiterRegistrieren(mitarbeiterNummer, name, benutzername, passwort);
                    System.out.println("Neuer Mitarbeiter " + m.getName() + " angelegt");
                    break;
                } catch (MitarbeiterExistiertBereitsException e){
                    System.out.println("Mitarbeiter existiert bereits!");
                    e.printStackTrace();
                }
                break;

            case "d":
                System.out.print("Benutzername des Mitarbeiters, der gelöscht werden soll: ");
                String mitarbeiter = liesEingabe();
                eshop.mitarbeiterEntfernen(mitarbeiter);
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
            System.out.println("Bezeichnung: " + a.getBezeichnung() + ", Anzahl: " + anzahl + ", Preis: " + a.getPreis());
        }
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
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } while(!input.equals("a"));

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
            }
        } while (!input.equals("q"));
    }

    public static void main(String[] args) {
        ShopClientCUI cui = new ShopClientCUI();
        cui.run();
    }
}
