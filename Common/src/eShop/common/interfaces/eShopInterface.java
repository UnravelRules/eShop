package eShop.common.interfaces;

import eShop.common.entities.*;
import eShop.common.exceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface eShopInterface {

    Kunde kundeRegistrieren(String name, String str, String plz, String benutzer, String passwort) throws KundeExistiertBereitsException, FehlendeEingabenException;

    Kunde kundeEinloggen(String benutzername, String passwort) throws KundeExistiertNichtException;

    Mitarbeiter mitarbeiterEinloggen(String benutzername, String passwort) throws MitarbeiterExistiertNichtException;

    Mitarbeiter mitarbeiterRegistrieren(int nummer, String name, String benutzer, String passwort) throws MitarbeiterExistiertBereitsException;

    void mitarbeiterEntfernen(String benutzername);

    Artikel artikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter) throws RuntimeException;

    Massengutartikel massengutartikelAnlegen(int nummer, String bezeichnung, int bestand, float preis, Mitarbeiter aktuellerMitarbeiter, int packungsgroesse) throws MassengutException;

    void bestandAendern(int artikel_nummer, int neuer_bestand, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException, MassengutException;

    ArrayList<Artikel> artikelSuchen(String bezeichnung);

    void artikelEntfernen(int nummer, String bezeichnung, Mitarbeiter aktuellerMitarbeiter) throws ArtikelExistiertNichtException, UnbekanntesAccountObjektException;

    ArrayList<Artikel> gibAlleArtikel();

    void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde) throws ArtikelExistiertNichtException, MassengutException, NegativerBestandException;

    void warenkorbLeeren(Kunde aktuellerKunde);

    Rechnung warenkorbKaufen(Kunde aktuellerKunde) throws UnbekanntesAccountObjektException, MassengutException, ArtikelExistiertNichtException;

    void warenkorbVeraendern(Kunde aktuellerKunde, String bezeichnung, int neuerBestand) throws MassengutException, ArtikelExistiertNichtException, NegativerBestandException;

    void artikelAusWarenkorbEntfernen(Kunde aktuellerKunde, String bezeichnung) throws ArtikelExistiertNichtException;

    HashMap<Artikel, Integer> gibWarenkorb(Kunde aktuellerKunde);

    ArrayList<Ereignis> eventlogAusgeben();

    void schreibeKunde() throws IOException;

    void schreibeMitarbeiter() throws IOException;

    void schreibeArtikel() throws IOException;

    void schreibeEreignis() throws IOException;

    void sichereDaten() throws IOException;

    ArrayList<Integer> getBestandhistorie(int Artikelnummer) throws ArtikelExistiertNichtException;
}
