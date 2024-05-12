package eShop.local.entities;

import java.time.LocalDate;
import java.util.HashMap;

public class Rechnung {
    private Kunde kunde;
    private LocalDate datum;
    private HashMap<Artikel, Integer> gekaufteArtikel;
    private float gesamtpreis;

    public Rechnung(Kunde k, float gesamtpreis){
        this.kunde = k;
        this.datum = LocalDate.now();
        this.gekaufteArtikel = k.getWarenkorb().getHashmap();
        this.gesamtpreis = gesamtpreis;
    }

    public Kunde getKunde() {
        return kunde;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public HashMap<Artikel, Integer> getGekaufteArtikel() {
        return gekaufteArtikel;
    }

    public float getGesamtpreis() {
        return gesamtpreis;
    }
}
