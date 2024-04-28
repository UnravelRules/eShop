package eShop.local.entities;
/** die Klasse Artikel steht für ein Artikel in EShop
 * jeden Artikel hat eine Artikel-Nr, Bezeichnung, Bestand und Preis
  */
public class Artikel {
    // Attribute eines Artikels

    private int artikelnummer;
    private String bezeichnung;
    private int bestand;
    private float preis;

    public Artikel(int artikelNr, String bezeichnung, int bestand, float preis) {
        this.artikelnummer = artikelNr;
        this.bezeichnung = bezeichnung;
        this.bestand = bestand;
        this.preis = preis;
    }

    public String toString(){
        return("Nr: " + artikelnummer + " / Bezeichnung: " + bezeichnung + " / Bestand: " + bestand + " Stk. / Preis: " + preis + "€");
    }

/** Artikelnummer des Artikels.
 * */
    public int getArtikelnummer() {

        return artikelnummer;
    }

     /** Bezeichnung des Artikels.
     * */
    public String getBezeichnung() {
        return bezeichnung;
    }

    /** Bestand des Artikels.
     * */
    public int getBestand() {
        return bestand;
    }

    public void setBestand(int bestand) {
        this.bestand = bestand;
    }

    /** Preis des Artikels. es konnte wegen die kommazahlen double oder float genommen werden statt integer :).
     * */
    public float getPreis() {
        return preis;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
}


