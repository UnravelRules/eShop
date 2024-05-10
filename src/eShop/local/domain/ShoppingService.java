package eShop.local.domain;

import eShop.local.entities.Artikel;
import eShop.local.entities.Kunde;
import eShop.local.entities.Warenkorb;

import java.util.HashMap;

public class ShoppingService {
    private ArtikelVerwaltung artikelVw;

    public ShoppingService(){
        this.artikelVw = new ArtikelVerwaltung();
    }
    public void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde){
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb().getHashmap();
        for(Artikel a : artikelVw.getArtikelBestand()){
            if(a.getArtikelnummer() == artikelnummer){
                warenkorb.put(a, anzahl);
            }
        }
    }

    public void warenkorbLeeren(HashMap<Artikel, Integer> warenkorb){
        warenkorb.clear();
    }

    public void warenkorbKaufen(){
        // alles was sich im Warenkorb befindet kaufen -> Rechnung wird erstellt
    }
}
