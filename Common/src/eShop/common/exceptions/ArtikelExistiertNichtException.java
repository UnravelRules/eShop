package eShop.common.exceptions;

public class ArtikelExistiertNichtException extends Exception {
    public ArtikelExistiertNichtException(String Bezeichnung){
        super("Artikel "+ Bezeichnung+ " existiert nicht");
    }
}
