package eShop.local.domain;

import eShop.local.domain.exceptions.ArtikelExistiertNichtException;
import eShop.local.entities.Artikel;
import eShop.local.entities.Kunde;
import eShop.local.entities.Rechnung;

import java.util.HashMap;
import java.util.Map;

public class ShoppingService {
    private ArtikelVerwaltung artikelVw;

    public ShoppingService(){
        this.artikelVw = new ArtikelVerwaltung();
    }

    /**
     * Fügt einen Artikel zum Warenkorb hinzu
     * @param artikelnummer
     * @param anzahl
     * @param aktuellerKunde
     */
    public void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde){
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb().getHashmap();
        for(Artikel a : artikelVw.getArtikelBestand()){
            if(a.getArtikelnummer() == artikelnummer){
                warenkorb.put(a, anzahl);
            }
        }
    }

    /**
     * Leert den Warenkorb komplett
     * @param warenkorb
     */
    public void warenkorbLeeren(HashMap<Artikel, Integer> warenkorb){
        warenkorb.clear();
    }

    /**
     * Methode um alle Artikel im Warenkorb zu kaufen.
     * Gibt nach dem Kauf eine Rechnung zurück.
     * @param aktuellerKunde
     * @return Rechnung
     * @throws ArtikelExistiertNichtException
     */
    public Rechnung warenkorbKaufen(Kunde aktuellerKunde) throws ArtikelExistiertNichtException {
        float gesamtpreis = 0;
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb().getHashmap();

        // den Bestand der Artikel, die gekauft werden, ändern
        for (Map.Entry<Artikel, Integer> eintrag : warenkorb.entrySet()) {
            int artikelnummer = eintrag.getKey().getArtikelnummer();
            int anzahl = eintrag.getValue();
            for (Artikel a : artikelVw.getArtikelBestand()) {
                if (a.getArtikelnummer() == artikelnummer) {
                    artikelVw.bestandAendern(a.getArtikelnummer(), a.getBestand() - anzahl);
                    gesamtpreis += a.getPreis() * anzahl;
                }
            }
        }
        Rechnung rechnung = new Rechnung(aktuellerKunde, gesamtpreis);
        return rechnung;
    }
}