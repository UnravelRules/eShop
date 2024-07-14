package eShop.common.exceptions;

public class ArtikelExistiertNichtException extends Exception {
    private String bezeichnung;

    public ArtikelExistiertNichtException(String Bezeichnung){
        super("Artikel "+ Bezeichnung+ " existiert nicht");
        this.bezeichnung = Bezeichnung;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
}
