package eShop.local.domain;

import eShop.local.domain.exceptions.ArtikelExistiertNichtException;
import eShop.local.domain.exceptions.MassengutException;
import eShop.local.entities.Artikel;
import eShop.local.entities.Kunde;
import eShop.local.entities.Massengutartikel;
import eShop.local.entities.Rechnung;

import java.util.HashMap;
import java.util.Map;

public class Warenkorb {
    private ArtikelVerwaltung artikelVW;

    public Warenkorb(ArtikelVerwaltung artikelVw){
        this.artikelVW = artikelVw;
    }

    /**
     * Fügt einen Artikel zum Warenkorb hinzu
     *
     * @param artikelnummer
     * @param anzahl
     * @param aktuellerKunde
     */
    public void artikelInWarenkorb(int artikelnummer, int anzahl, Kunde aktuellerKunde) throws ArtikelExistiertNichtException, MassengutException {
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb();
        Artikel a = artikelVW.getArtikelMitNummer(artikelnummer);
        if(a instanceof Massengutartikel){
            int packungs_gr = ((Massengutartikel) a).getPackungsgroesse();
            if(anzahl % packungs_gr != 0){
                throw new MassengutException();
            }
        }
        if(warenkorb.containsKey(a)){
            int alteAnzahl = warenkorb.get(a);
            warenkorb.put(a, alteAnzahl + anzahl);
            return;
        }
        warenkorb.put(a, anzahl);
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
    public Rechnung warenkorbKaufen(Kunde aktuellerKunde) throws ArtikelExistiertNichtException, MassengutException {
        float gesamtpreis = 0;
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb();

        // den Bestand der Artikel, die gekauft werden, ändern
        for (Map.Entry<Artikel, Integer> eintrag : warenkorb.entrySet()) {
            int artikelnummer = eintrag.getKey().getArtikelnummer();
            int anzahl = eintrag.getValue();
            for (Artikel a : artikelVW.getArtikelBestand()) {
                if (a.getArtikelnummer() == artikelnummer) {
                    artikelVW.bestandAendern(a.getArtikelnummer(), a.getBestand() - anzahl);
                    gesamtpreis += a.getPreis() * anzahl;
                }
            }
        }
        HashMap<Artikel, Integer> warenkorbKopie = new HashMap<>(warenkorb);
        Rechnung rechnung = new Rechnung(aktuellerKunde, gesamtpreis, warenkorbKopie);
        warenkorb.clear();
        return rechnung;
    }

    public void warenkorbVeraendern(Kunde aktuellerKunde, String bezeichnung, int neuerBestand) throws MassengutException{
        HashMap<Artikel, Integer> warenkorb = aktuellerKunde.getWarenkorb();
        for (Map.Entry<Artikel, Integer> eintrag : warenkorb.entrySet()) {
            Artikel a = eintrag.getKey();
            if(a.getBezeichnung().equals(bezeichnung)) {
                if (neuerBestand == 0){
                    warenkorb.remove(a);
                } else{
                    if(a instanceof Massengutartikel){
                        int packungsgroesse = ((Massengutartikel) a).getPackungsgroesse();
                        if(neuerBestand % packungsgroesse != 0){
                            throw new MassengutException();
                        }
                    }
                    warenkorb.put(a, neuerBestand);
                }
            }
        }
    }
}
