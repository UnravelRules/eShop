package eShop.common.exceptions;

/**
 * Eine Ausnahme, die geworfen wird, wenn ein Artikel mit einer bestimmten Artikelnummer
 * oder Bezeichnung bereits im System existiert.
 */
public class ArtikelExistiertBereitsException extends Exception {

    /**
     * Konstruktor für die ArtikelExistiertBereitsException.
     *
     * @param artikelnummer Die Artikelnummer des bereits existierenden Artikels
     * @param bezeichnung Die Bezeichnung des bereits existierenden Artikels
     */
    public ArtikelExistiertBereitsException(int artikelnummer, String bezeichnung){
        super("Ein Artikel mit der Artikelnummer " + artikelnummer + " oder Bezeichnung " + bezeichnung + " existiert bereits!");
    }
}
