package eShop.local.entities;

public class Artikel {
    // Attribute eines Artikels
    private int artikelnummer;
    private String bezeichnung;
    private int bestand;

    public Artikel(int nr, String bez, int bes){
        this.artikelnummer = nr;
        this.bezeichnung = bez;
        this.bestand = bes;
    }

    public int getArtikelnummer(){return artikelnummer;}
    public String getBezeichnung(){return bezeichnung;}
    public int getBestand(){return bestand;}
}
