package eshop.server.domain;

import eShop.common.exceptions.ArtikelExistiertNichtException;
import eShop.common.exceptions.BestandUeberschrittenException;
import eShop.common.exceptions.MassengutException;
import eShop.common.entities.*;
import eShop.common.exceptions.NegativerBestandException;

import java.util.HashMap;
import java.util.Map;

public class ShoppingService {
    ArtikelVerwaltung artikelVW;
    public ShoppingService(ArtikelVerwaltung artikelVW){
        this.artikelVW = artikelVW;
    }
    public void artikelInWarenkorb(Artikel a, int anzahl, Kunde k) throws MassengutException, BestandUeberschrittenException {
        Warenkorb warenkorb = k.getWarenkorb();
        if(anzahl > a.getBestand()){
            throw new BestandUeberschrittenException(a, anzahl);
        }
        if(a instanceof Massengutartikel){
            int packungs_gr = ((Massengutartikel) a).getPackungsgroesse();
            if(anzahl % packungs_gr != 0){
                throw new MassengutException(anzahl, ((Massengutartikel) a).getPackungsgroesse());
            }
        }

        warenkorb.artikelInWarenkorb(a, anzahl);
    }

    public void warenkorbLeeren(Kunde k){
        Warenkorb warenkorb = k.getWarenkorb();
        warenkorb.warenkorbLeeren();
    }

    public Rechnung warenkorbKaufen(Kunde k) throws ArtikelExistiertNichtException, MassengutException {
        Warenkorb warenkorb = k.getWarenkorb();
        HashMap<Artikel, Integer> inhalt = warenkorb.getInhalt();
        float gesamtpreis = 0;

        // den Bestand der Artikel, die gekauft werden, ändern
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

        HashMap<Artikel, Integer> warenkorbKopie = new HashMap<>(inhalt);
        Rechnung rechnung = new Rechnung(k, gesamtpreis, warenkorbKopie);
        warenkorb.warenkorbLeeren();
        return rechnung;
    }

    public void warenkorbVeraendern(Kunde aktuellerKunde, Artikel artikel, int neuerBestand) throws MassengutException{
        Warenkorb warenkorb = aktuellerKunde.getWarenkorb();
        HashMap<Artikel, Integer> inhalt = warenkorb.getInhalt();

        if(inhalt.containsKey(artikel)) {
            if (neuerBestand == 0){
                inhalt.remove(artikel);
            } else{
                if(artikel instanceof Massengutartikel){
                    int packungsgroesse = ((Massengutartikel) artikel).getPackungsgroesse();
                    if(neuerBestand % packungsgroesse != 0){
                        throw new MassengutException(neuerBestand, packungsgroesse);
                    }
                }
                warenkorb.inhaltVeraendern(artikel, neuerBestand);
            }
        }
    }

    public void artikelAusWarenkorbEntfernen(Kunde aktuellerKunde, Artikel artikel){
        Warenkorb warenkorb = aktuellerKunde.getWarenkorb();
        HashMap<Artikel, Integer> inhalt = warenkorb.getInhalt();

        inhalt.remove(artikel);
    }
}