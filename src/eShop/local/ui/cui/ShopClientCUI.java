package eShop.local.ui.cui;
import eShop.local.domain.eShop;
import eShop.local.domain.exceptions.KundeExistiertBereitsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShopClientCUI {
    private eShop eshop = new eShop();
    private BufferedReader in;

    // Konstruktor (Datei als Parameter geben, BufferedReader)
    public ShopClientCUI(){
        // Stream-Objekt fuer Texteingabe ueber Konsolenfenster erzeugen
        in = new BufferedReader(new InputStreamReader(System.in));
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
        System.out.flush(); // ohne NL ausgeben
    }

    private String liesEingabe() throws IOException {
        // einlesen von Konsole
        return in.readLine();
    }

    // Eingabe der Anmeldung verarbeiten, dann je nach Eingabe prüfen und in neues Menü weiterleiten
    private void anmeldungVerarbeiten(String line) throws IOException{
        switch(line){
            case "r":
                gibRegistrierenmenueAus();
                break;
            case "k":
                gibKundenloginAus();
                break;
            case "m":
                mitarbeiterEinloggen();
        }
    }

    // Menue zum Registrieren eines neuen Kunden
    public void gibRegistrierenmenueAus() throws IOException {
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
            eshop.kundenAnlegen(name, strasse, plz, benutzername, passwort);
        } catch (KundeExistiertBereitsException e) {
            System.out.println("Fehler beim Registrieren");
            e.printStackTrace();
        }
        //System.out.println("Herzlich willkommen, Herr / Frau " + kunde.getName());
    }
    public void gibKundenloginAus() throws IOException {
        System.out.print("Benutzername: ");
        String benutzername = liesEingabe();
        System.out.print("Passwort: ");
        String passwort = liesEingabe();

    }
    public void mitarbeiterEinloggen(){}

    // alle Optionen für einen Kunden, bspw. Artikel in Warenkorb hinzufügen, Warenkorb kaufen usw.
    private void kundenMenue(){
        System.out.print("Befehle: \n  Ein Artikel suchen:  's'");
        System.out.print("         \n  Ein Artikel in den Warenkorb hinzufügen: 'h'");
        System.out.print("         \n  Produkte im Warenkorb kaufen: 'k'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Beenden:        'q'");
        System.out.print("> "); // Prompt
        System.out.flush(); // ohne NL ausgeben
    }

    // alle Optionen für einen Mitarbeiter, bspw. neue Artikel anlegen, Bestand ändern, neuen Mitarbeiter anlegen usw.
    private void mitarbeiterMenue(){
        System.out.print("Befehle: \n  Neuen Artikel anlegen:  's'");
        System.out.print("         \n  Alle Artikel ausgeben: 'h'");
        System.out.print("         \n  Artikel suchen: 'h'");
        System.out.print("         \n  Artikel entfernen: 'k'");
        System.out.print("         \n  Bestand aendern: 'h'");
        System.out.print("         \n  Mitarbeiter anlegen: 'k'");
        System.out.print("         \n  Mitarbeiter entfernen: 'k'");
        System.out.print("         \n  ---------------------");
        System.out.println("         \n  Beenden:        'q'");
        System.out.print("> "); // Prompt
        System.out.flush(); // ohne NL ausgeben
    }

    public void run(){
        String input = "";

        do {
            gibAnmeldemenueAus();
            try{
                input = liesEingabe();
                anmeldungVerarbeiten(input);
            } catch(IOException e){
                e.printStackTrace();
            }
        } while (!input.equals("q"));
    }

    public static void main(String[] args) {
        ShopClientCUI cui = new ShopClientCUI();
        cui.run();
    }
}
