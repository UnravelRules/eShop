package eShop.common.exceptions;

/**
 * Eine Ausnahme, die geworfen wird, wenn der Bestand eines Massengutartikels nicht durch die Packungsgröße teilbar ist.
 */
public class MassengutException extends Exception {

    private int bestand;
    private int packungsgroesse;

    /**
     * Konstruktor für die MassengutException.
     *
     * @param bestand Der aktuelle Bestand des Massengutartikels
     * @param packungsgroesse Die Packungsgröße des Massengutartikels
     */
    public MassengutException(int bestand, int packungsgroesse) {
        super("Der Bestand " + bestand + " ist nicht durch die Packungsgröße " + packungsgroesse + " teilbar!");
        this.bestand = bestand;
        this.packungsgroesse = packungsgroesse;
    }

    /**
     * Gibt den aktuellen Bestand zurück, für den die Ausnahme ausgelöst wurde.
     *
     * @return Der Bestand des Massengutartikels
     */
    public int getBestand() {
        return bestand;
    }

    /**
     * Gibt die Packungsgröße zurück, für die die Ausnahme ausgelöst wurde.
     *
     * @return Die Packungsgröße des Massengutartikels
     */
    public int getPackungsgroesse() {
        return packungsgroesse;
    }

}
