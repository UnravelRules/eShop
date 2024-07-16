package eShop.common.exceptions;

/**
 * Eine Ausnahme, die geworfen wird, wenn ein Artikel nicht im System gefunden werden kann.
 */
public class ArtikelExistiertNichtException extends Exception {
    private String bezeichnung;

    /**
     * Konstruktor für die ArtikelExistiertNichtException.
     *
     * @param bezeichnung Die Bezeichnung des Artikels, der nicht gefunden wurde
     */
    public ArtikelExistiertNichtException(String bezeichnung){
        super("Artikel " + bezeichnung + " existiert nicht");
        this.bezeichnung = bezeichnung;
    }

    /**
     * Gibt die Bezeichnung des nicht gefundenen Artikels zurück.
     *
     * @return Die Bezeichnung des Artikels
     */
    public String getBezeichnung() {
        return bezeichnung;
    }
}
