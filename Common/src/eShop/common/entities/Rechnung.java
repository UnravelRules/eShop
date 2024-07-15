package eShop.common.entities;

import java.time.LocalDate;
import java.util.HashMap;

/**
 * Diese Klasse repräsentiert eine Rechnung im eShop-System.
 * Eine Rechnung enthält Informationen über den Kunden, das Kaufdatum, die gekauften Artikel und den Gesamtpreis.
 */
public class Rechnung {
    private Kunde kunde;
    private LocalDate datum;
    private HashMap<Artikel, Integer> gekaufteArtikel;
    private float gesamtpreis;

    /**
     * Konstruktor zur Initialisierung einer Rechnung.
     *
     * @param k           Der Kunde, dem die Rechnung zugeordnet ist
     * @param gesamtpreis Der Gesamtpreis der Rechnung
     * @param w           Die gekauften Artikel mit ihren jeweiligen Mengen als HashMap
     */
    public Rechnung(Kunde k, float gesamtpreis, HashMap<Artikel, Integer> w){
        this.kunde = k;
        this.datum = LocalDate.now();
        this.gekaufteArtikel = w;
        this.gesamtpreis = gesamtpreis;
    }

    /**
     * Methode um den Kunden einer Rechnung zu bekommen.
     * Gibt den Kunden als Kundenobjekt zurück.
     * @return Kunde
     */
    public Kunde getKunde() {
        return kunde;
    }

    /**
     * Gibt das Datum einer Rechnung zurück.
     * @return Datum
     */
    public LocalDate getDatum() {
        return datum;
    }

    /**
     * Gibt alle gekauften Artikel einer Rechnung als Hashmap zurück
     * @return gekaufteArtikel
     */
    public HashMap<Artikel, Integer> getGekaufteArtikel() {
        return gekaufteArtikel;
    }

    /**
     * Gibt den gesamtpreis der Rechung zurück.
     * @return Gesamtpreis
     */
    public float getGesamtpreis() {
        return gesamtpreis;
    }

}
