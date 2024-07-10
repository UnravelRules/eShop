package eShop.local.entities;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Rechnung {
    private Kunde kunde;
    private LocalDate datum;
    private HashMap<Artikel, Integer> gekaufteArtikel;
    private float gesamtpreis;

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
