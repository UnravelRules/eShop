package eShop.local.entities;

import eShop.local.domain.exceptions.MassengutException;

public class Massengutartikel extends Artikel{
    private int packungsgroesse;

    public Massengutartikel(int artikelNr, String bezeichnung, int bestand, float preis, int neuePackungsgroesse){
        super(artikelNr, bezeichnung, bestand, preis);
        packungsgroesse = neuePackungsgroesse;
    }

    public int getPackungsgroesse() {
        return packungsgroesse;
    }

    public void setPackungsgroesse(int neuePackungsgroesse){
        this.packungsgroesse = neuePackungsgroesse;
    }

    @Override
    public void setBestand(int neuerBestand) throws MassengutException {
        int differenz = neuerBestand - bestand;
        if(differenz % packungsgroesse == 0){
            this.bestand = neuerBestand;
        } else {
            throw new MassengutException();
        }
    }

    @Override public String toString(){
        return String.format("Nr: " + artikelnummer + " / Bezeichnung: " + bezeichnung + " / Bestand: " + bestand + " Stk. / Packungsgröße " + packungsgroesse +" Stk. / Preis pro Stk. : %.2f€", preis);
    }
}