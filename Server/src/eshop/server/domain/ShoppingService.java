package eshop.server.domain;

import eShop.common.exceptions.ArtikelExistiertNichtException;
import eShop.common.exceptions.BestandUeberschrittenException;
import eShop.common.exceptions.MassengutException;
import eShop.common.entities.*;

import java.util.HashMap;
import java.util.Map;

public class ShoppingService {
    ArtikelVerwaltung artikelVW;

    public ShoppingService(ArtikelVerwaltung artikelVW) {
        this.artikelVW = artikelVW;
    }

    /**
     * Fügt einen Artikel mit einer bestimmten Anzahl dem Warenkorb eines Kunden hinzu.
     * Werfen von Ausnahmen bei Bestandsüberschreitungen oder Massengutartikeln.
     *
     * @param a       Der Artikel, der zum Warenkorb hinzugefügt werden soll.
     * @param anzahl  Die Anzahl des Artikels, die hinzugefügt werden soll.
     * @param k       Der Kunde, dessen Warenkorb bearbeitet wird.
     * @throws MassengutException          Wenn die Anzahl nicht durch die Packungsgröße teilbar ist (nur bei Massengutartikeln).
     * @throws BestandUeberschrittenException Wenn die angegebene Anzahl den verfügbaren Bestand des Artikels übersteigt.
     */
    public void artikelInWarenkorb(Artikel a, int anzahl, Kunde k) throws MassengutException, BestandUeberschrittenException {
        Warenkorb warenkorb = k.getWarenkorb();

        // Überprüfen, ob die Anzahl den verfügbaren Bestand überschreitet
        if (anzahl > a.getBestand()) {
            throw new BestandUeberschrittenException(a, anzahl);
        }

        // Überprüfen, ob es sich um einen Massengutartikel handelt und die Anzahl durch die Packungsgröße teilbar ist
        if (a instanceof Massengutartikel) {
            int packungs_gr = ((Massengutartikel) a).getPackungsgroesse();
            if (anzahl % packungs_gr != 0) {
                throw new MassengutException(anzahl, packungs_gr);
            }
        }

        // Artikel zum Warenkorb hinzufügen
        warenkorb.artikelInWarenkorb(a, anzahl);
    }

    /**
     * Leert den Warenkorb eines Kunden.
     *
     * @param k Der Kunde, dessen Warenkorb geleert werden soll.
     */
    public void warenkorbLeeren(Kunde k) {
        Warenkorb warenkorb = k.getWarenkorb();
        warenkorb.warenkorbLeeren();
    }

    /**
     * Kauft alle Artikel im Warenkorb eines Kunden und erstellt eine Rechnung.
     * Die Bestände der gekauften Artikel werden aktualisiert und der Warenkorb geleert.
     *
     * @param k Der Kunde, dessen Warenkorb gekauft wird.
     * @return Die erstellte Rechnung für den Kauf.
     * @throws ArtikelExistiertNichtException Wenn ein Artikel im Warenkorb nicht gefunden werden kann.
     * @throws MassengutException             Wenn es Probleme mit Massengutartikeln gibt (z.B. nicht teilbare Anzahl).
     */
    public Rechnung warenkorbKaufen(Kunde k) throws ArtikelExistiertNichtException, MassengutException {
        Warenkorb warenkorb = k.getWarenkorb();
        HashMap<Artikel, Integer> inhalt = warenkorb.getInhalt();
        float gesamtpreis = 0;

        // Durch alle Artikel im Warenkorb iterieren und Bestände aktualisieren
        for (Map.Entry<Artikel, Integer> eintrag : inhalt.entrySet()) {
            int artikelnummer = eintrag.getKey().getArtikelnummer();
            int anzahl = eintrag.getValue();
            for (Artikel a : artikelVW.getArtikelBestand()) {
                if (a.getArtikelnummer() == artikelnummer) {
                    artikelVW.bestandAendern(a.getArtikelnummer(), a.getBestand() - anzahl);
                    gesamtpreis += a.getPreis() * anzahl;
                }
            }
        }

        // Kopie des Warenkorbs für die Rechnung erstellen und Warenkorb leeren
        HashMap<Artikel, Integer> warenkorbKopie = new HashMap<>(inhalt);
        Rechnung rechnung = new Rechnung(k, gesamtpreis, warenkorbKopie);
        warenkorb.warenkorbLeeren();

        return rechnung;
    }

    /**
     * Verändert den Bestand eines Artikels im Warenkorb eines Kunden.
     *
     * @param aktuellerKunde Der Kunde, dessen Warenkorb bearbeitet wird.
     * @param artikel        Der Artikel, dessen Bestand geändert werden soll.
     * @param neuerBestand   Der neue Bestand des Artikels.
     * @throws MassengutException Wenn es Probleme mit Massengutartikeln gibt (z.B. nicht teilbare Anzahl).
     */
    public void warenkorbVeraendern(Kunde aktuellerKunde, Artikel artikel, int neuerBestand) throws MassengutException {
        Warenkorb warenkorb = aktuellerKunde.getWarenkorb();
        HashMap<Artikel, Integer> inhalt = warenkorb.getInhalt();

        // Überprüfen, ob der Artikel im Warenkorb vorhanden ist
        if (inhalt.containsKey(artikel)) {
            // Artikel aus dem Warenkorb entfernen, wenn neuer Bestand 0 ist
            if (neuerBestand == 0) {
                inhalt.remove(artikel);
            } else {
                // Überprüfen, ob es sich um einen Massengutartikel handelt und die Anzahl durch die Packungsgröße teilbar ist
                if (artikel instanceof Massengutartikel) {
                    int packungsgroesse = ((Massengutartikel) artikel).getPackungsgroesse();
                    if (neuerBestand % packungsgroesse != 0) {
                        throw new MassengutException(neuerBestand, packungsgroesse);
                    }
                }
                // Bestand des Artikels im Warenkorb ändern
                warenkorb.inhaltVeraendern(artikel, neuerBestand);
            }
        }
    }

    /**
     * Entfernt einen Artikel aus dem Warenkorb eines Kunden.
     *
     * @param aktuellerKunde Der Kunde, dessen Warenkorb bearbeitet wird.
     * @param artikel        Der Artikel, der aus dem Warenkorb entfernt werden soll.
     */
    public void artikelAusWarenkorbEntfernen(Kunde aktuellerKunde, Artikel artikel) {
        Warenkorb warenkorb = aktuellerKunde.getWarenkorb();
        HashMap<Artikel, Integer> inhalt = warenkorb.getInhalt();

        // Artikel aus dem Warenkorb entfernen
        inhalt.remove(artikel);
    }
}
