package eShop.local.domain.exceptions;

public class ArtikelExistiertBereitsException extends Exception {
    public ArtikelExistiertBereitsException(int artikelnummer, String bezeichnung){
        super("Ein Artikel mit der Artikelnummer " + artikelnummer + " oder Bezeichnung " + bezeichnung + " existiert bereits!");
    }
}
