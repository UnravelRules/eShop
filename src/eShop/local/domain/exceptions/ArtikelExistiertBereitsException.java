package eShop.local.domain.exceptions;

public class ArtikelExistiertBereitsException extends Exception{

    public ArtikelExistiertBereitsException(String bezeichnung){
        super("Artikel " + bezeichnung + "existiert bereits");
    }
}
