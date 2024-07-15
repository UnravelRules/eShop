package eShop.common.entities;

import eShop.common.exceptions.MassengutException;

/**
 * Diese Klasse repräsentiert einen Massengutartikel, der spezielle Eigenschaften wie eine Packungsgröße besitzt.
 * Ein Massengutartikel erbt von der Klasse Artikel und erweitert diese um die Packungsgröße.
 */
public class Massengutartikel extends Artikel{
    private int packungsgroesse;

    /**
     * Konstruktor zur Initialisierung eines Massengutartikels.
     *
     * @param artikelNr           Die Artikelnummer des Massengutartikels
     * @param bezeichnung         Die Bezeichnung des Massengutartikels
     * @param bestand             Der Bestand des Massengutartikels
     * @param preis               Der Preis pro Stück des Massengutartikels
     * @param neuePackungsgroesse Die Packungsgröße des Massengutartikels
     */
    public Massengutartikel(int artikelNr, String bezeichnung, int bestand, float preis, int neuePackungsgroesse){
        super(artikelNr, bezeichnung, bestand, preis);
        packungsgroesse = neuePackungsgroesse;
    }

    /**
     * Gibt die Packungsgröße des Massengutartikels zurück.
     *
     * @return Die Packungsgröße des Massengutartikels
     */
    public int getPackungsgroesse() {
        return packungsgroesse;
    }

    /**
     * Überschriebene Methode zur Setzung des Bestands eines Massengutartikels.
     * Der Bestand muss ein Vielfaches der Packungsgröße sein, sonst wird eine Ausnahme geworfen.
     *
     * @param neuerBestand Der neue Bestand des Massengutartikels
     * @throws MassengutException Wenn der neue Bestand kein Vielfaches der Packungsgröße ist
     */
    @Override
    public void setBestand(int neuerBestand) throws MassengutException {
        int differenz = neuerBestand - bestand;
        if (differenz % packungsgroesse == 0) {
            this.bestand = neuerBestand;
        } else {
            throw new MassengutException(neuerBestand, packungsgroesse);
        }
    }

    /**
     * Überschriebene toString-Methode zur Darstellung eines Massengutartikels als String.
     *
     * @return Ein String, die die wichtigsten Informationen des Massengutartikels enthält
     */
    @Override
    public String toString() {
        return String.format("Nr: " + artikelnummer + " / Bezeichnung: " + bezeichnung + " / Bestand: " + bestand + " Stk. / Packungsgröße " + packungsgroesse + " Stk. / Preis pro Stk. : %.2f€", preis);
    }

    /**
     * Überschriebene equals-Methode zur Überprüfung der Gleichheit von Massengutartikeln.
     * Zwei Massengutartikel sind gleich, wenn entweder ihre Artikelnummer oder ihre Bezeichnung übereinstimmen.
     *
     * @param andererArtikel Das andere Objekt, mit dem verglichen wird
     * @return true, wenn die Massengutartikel gleich sind, ansonsten false
     */
    @Override
    public boolean equals(Object andererArtikel) {
        if (andererArtikel instanceof Artikel) {
            return ((this.artikelnummer == ((Artikel) andererArtikel).artikelnummer)
                    || (this.bezeichnung.equals(((Artikel) andererArtikel).bezeichnung)));
        } else {
            return false;
        }
    }
}