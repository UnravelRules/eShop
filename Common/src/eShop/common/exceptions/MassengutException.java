package eShop.common.exceptions;

public class MassengutException extends Exception {
    public MassengutException(int bestand, int packungsgroesse){
        super("Der Bestand " + bestand + " ist nicht durch die Packungsgröße " + packungsgroesse + " teilbar!");
    }

}