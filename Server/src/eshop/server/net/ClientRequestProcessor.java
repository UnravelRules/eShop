package eshop.server.net;

import eShop.common.entities.*;
import eShop.common.exceptions.ArtikelExistiertNichtException;
import eShop.common.exceptions.KundeExistiertNichtException;
import eShop.common.exceptions.MassengutException;
import eShop.common.exceptions.MitarbeiterExistiertNichtException;
import eShop.common.interfaces.eShopInterface;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     *
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
                continue;
            }
            switch (input){
                case null:
                    input = "q";
                    break;
                case "r":
                    kundeRegistrieren();
                    break;
                case "k":
                    kundeEinloggen();
                    kundenMenu();
                    break;
                case "m":
                    mitarbeiterEinloggen();
                    mitarbeiterMenu();
                    break;
                case "q":
                    break;
                default:
                    System.out.println("Unbekannte Aktion im Hauptmenü: " + input);
            }
        } while (!(input.equals("q")));
        disconnect();
    }

    private void disconnect() {
        try {
            out.println("Tschuess!");
            clientSocket.close();

            System.out.println("Verbindung zu " + clientSocket.getInetAddress()
                    + ":" + clientSocket.getPort() + " durch Client abgebrochen");
        } catch (Exception e) {
            System.out.println("--->Fehler beim Beenden der Verbindung: ");
            System.out.println(e.getMessage());
            out.println("Fehler");
        }
    }

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

    // Kunden Menü
    private void kundenMenu(){
        String input = "";
        do {
            try{
                input = in.readLine();
            } catch (Exception e) {
                System.out.println("Exception bei Lesen vom Client: Kundenmenü");
                System.out.println(e.getMessage());
            }
            switch(input){
                case null:
                case "a":
                    input = "a";
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
                default:
                    System.out.println("Unbekannte Aktion im Kundenmenü: " + input);
            }

        } while (!(input.equals("a")));
        this.aktuellerKunde = null;
    }

    private void mitarbeiterMenu(){
        String input = "";
        do {
            try{
                input = in.readLine();
            } catch (Exception e) {
                System.out.println("Exception bei Lesen vom Client: Mitarbeitermenü");
                System.out.println(e.getMessage());
            }
            switch(input){
                case null:
                case "a":
                    input = "a";
                    break;
                case "s":
                    sucheArtikel();
                    break;
                case "p":
                    gibAlleArtikel();
                    break;
                default:
                    System.out.println("Unbekannte Aktion: " + input);
            }

        } while (!(input.equals("a")));
        this.aktuellerMitarbeiter = null;
    }

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
        } catch (Exception e) {
            System.out.println("Fehler");
            // Hier senden wir nicht die Exception, da wir sonst den Kunden mitschicken müssten.
        }
    }

    private void kundeEinloggen(){
        String benutzername = liesEingabeVonClient("Benutzername");
        String passwort = liesEingabeVonClient("Passwort");

        try {
            Kunde kunde = eshop.kundeEinloggen(benutzername, passwort);
            out.println("Erfolg");
            sendeKundeAnClient(kunde);
            this.aktuellerKunde = kunde;
        } catch (KundeExistiertNichtException e) {
            out.println("Fehler");
            out.println(e.getMessage());
        }


    }

    private void sendeKundeAnClient(Kunde kunde){
        out.println(kunde.getNummer());
        out.println(kunde.getName());
        out.println(kunde.getStrasse());
        out.println(kunde.getPlz());
        out.println(kunde.getBenutzername());
        out.println(kunde.getPasswort());
    }

    private void mitarbeiterEinloggen(){
        String benutzername = liesEingabeVonClient("Benutzername");
        String passwort = liesEingabeVonClient("Passwort");

        try {
            Mitarbeiter mitarbeiter = eshop.mitarbeiterEinloggen(benutzername, passwort);
            out.println("Erfolg");
            sendeMitarbeiterAnClient(mitarbeiter);
            this.aktuellerMitarbeiter = mitarbeiter;
        } catch (MitarbeiterExistiertNichtException e) {
            out.println("Fehler");
            out.println(e.getMessage());
        }

    }

    private void sendeMitarbeiterAnClient(Mitarbeiter mitarbeiter) {
        out.println(mitarbeiter.getNummer());
        out.println(mitarbeiter.getName());
        out.println(mitarbeiter.getBenutzername());
        out.println(mitarbeiter.getPasswort());
    }

    private void gibAlleArtikel() {
        List<Artikel> artikelList = eshop.gibAlleArtikel();
        // sende alle Artikel zum Client
        sendeArtikelListeAnClient(artikelList);

    }

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

    private void sendeArtikelListeAnClient(List<Artikel> artikelList) {
        // Anzahl von Artikeln übertragen
        out.println(artikelList.size());
        for (Artikel artikel : artikelList) {
            sendeArtikelAnClient(artikel);
        }
    }

    private void sucheArtikel(){
        String bezeichnung = liesEingabeVonClient("Bezeichnung");
        ArrayList<Artikel> artikelListe = eshop.artikelSuchen(bezeichnung);
        sendeArtikelListeAnClient(artikelListe);
    }

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
        }
    }

    private void gibWarenkorb(){
        HashMap<Artikel, Integer> warenkorb = eshop.gibWarenkorb(this.aktuellerKunde);
        int size = warenkorb.size();
        out.println(size);
        for (Artikel artikel : warenkorb.keySet()) {
            sendeArtikelAnClient(artikel);
            out.println(warenkorb.get(artikel));
        }
    }
}
