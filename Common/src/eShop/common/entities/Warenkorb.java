package eShop.common.entities;

import java.util.HashMap;

/**
 * Diese Klasse repräsentiert den Warenkorb eines Kunden im eShop-System.
 * Der Warenkorb enthält eine Sammlung von Artikeln mit ihren jeweiligen Mengen.
 */
public class Warenkorb {
    HashMap<Artikel, Integer> inhalt;

    /**
     * Konstruktor zur Initialisierung eines leeren Warenkorbs.
     * Der Warenkorb wird als HashMap initialisiert.
     */
    public Warenkorb(){
        inhalt = new HashMap<>();
    }

    /**
     * Fügt einen Artikel mit einer bestimmten Anzahl zum Warenkorb hinzu.
     * Wenn der Artikel bereits im Warenkorb vorhanden ist, wird die Anzahl aktualisiert.
     *
     * @param a Der Artikel, der dem Warenkorb hinzugefügt werden soll
     * @param anzahl Die Anzahl des Artikels, die hinzugefügt werden soll
     */
    public void artikelInWarenkorb(Artikel a, int anzahl){
        if (inhalt.containsKey(a)){
            int alteAnzahl = inhalt.get(a);
            inhalt.put(a, alteAnzahl + anzahl);
            return;
        }

        inhalt.put(a, anzahl);
    }

    /**
     * Verändert die Anzahl eines bestimmten Artikels im Warenkorb.
     * Wenn der Artikel nicht im Warenkorb enthalten ist, wird er hinzugefügt.
     *
     * @param a Der Artikel, dessen Anzahl im Warenkorb verändert werden soll
     * @param anzahl Die neue Anzahl des Artikels
     */
    public void inhaltVeraendern(Artikel a, int anzahl){
        inhalt.put(a, anzahl);
    }

    /**
     * Leert den gesamten Warenkorb, indem alle Artikel entfernt werden.
     */
    public void warenkorbLeeren(){
        inhalt.clear();
    }

    /**
     * Gibt den gesamten Inhalt des Warenkorbs zurück.
     *
     * @return Eine HashMap, die die Artikel im Warenkorb und ihre Mengen enthält
     */
    public HashMap<Artikel, Integer> getInhalt(){
        return inhalt;
    }
}