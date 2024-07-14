package eShop.common.exceptions;

public class MassengutException extends Exception {
    private int bestand;
    private int packungsgroesse;

    public MassengutException(int bestand, int packungsgroesse){
        super("Der Bestand " + bestand + " ist nicht durch die Packungsgröße " + packungsgroesse + " teilbar!");
        this.bestand = bestand;
        this.packungsgroesse = packungsgroesse;
    }

    public int getBestand() {
        return bestand;
    }

    public int getPackungsgroesse() {
        return packungsgroesse;
    }

}