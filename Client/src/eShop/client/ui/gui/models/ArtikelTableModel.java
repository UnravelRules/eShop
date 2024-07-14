package eShop.client.ui.gui.models;

import eShop.common.entities.Artikel;
import eShop.common.entities.Massengutartikel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ArtikelTableModel extends AbstractTableModel {
    private List<Artikel> artikel;
    private boolean istMitarbeiter;
    private String[] spaltenNamen = {"Nummer", "Bezeichnung", "Bestand", "Preis", "Packungsgröße"};

    public ArtikelTableModel(List<Artikel> aktuelleArtikel, boolean istMitarbeiter){
        super();
        this.istMitarbeiter = istMitarbeiter;
        artikel = new ArrayList<>(aktuelleArtikel);
    }

    public void setArtikel(List<Artikel> aktuelleArtikel){
        artikel.clear();
        artikel.addAll(aktuelleArtikel);
        fireTableDataChanged();
    }
    @Override
    public int getRowCount() {
        return artikel.size();
    }
    @Override
    public int getColumnCount() {
        return spaltenNamen.length;
    }
    @Override
    public String getColumnName(int col) {
        return spaltenNamen[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Wichtige Überprüfung, falls eine row für Kunden gelöscht wird, sodass kein IndexOutOfBounds geworfen wird
        if (rowIndex >= artikel.size()) {
            return null;
        }

        Artikel gewaehlterArtikel = artikel.get(rowIndex);

        if(istMitarbeiter) {
            switch (columnIndex) {
                case 0:
                    return gewaehlterArtikel.getArtikelnummer();
                case 1:
                    return gewaehlterArtikel.getBezeichnung();
                case 2:
                    return gewaehlterArtikel.getBestand();
                case 3:
                    return gewaehlterArtikel.getPreis();
                case 4:
                    if (gewaehlterArtikel instanceof Massengutartikel) {
                        return ((Massengutartikel) gewaehlterArtikel).getPackungsgroesse();
                    }
                default:
                    return null;
            }
        } else if(!(gewaehlterArtikel.getBestand() <= 0)) {
            switch (columnIndex) {
                case 0:
                    return gewaehlterArtikel.getArtikelnummer();
                case 1:
                    return gewaehlterArtikel.getBezeichnung();
                case 2:
                    return gewaehlterArtikel.getBestand();
                case 3:
                    return gewaehlterArtikel.getPreis();
                case 4:
                    if (gewaehlterArtikel instanceof Massengutartikel) {
                        return ((Massengutartikel) gewaehlterArtikel).getPackungsgroesse();
                    }
                default:
                    return null;
            }
        } else {
            artikel.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
            return null;
        }
    }

    /**
     * Überschreibt die Standardmethode von TableModel, in welchem standardmäßig ein Object.class zurückgegeben wird.
     * Gibt nun den richtigen Datentypen für die einzelnen Spalten zurück.
     * Wichtig, damit die Sortierfunktion mit allen Datentypen funktioniert.
     * @param columnIndex die einzelnen Indizes der Spalten
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0, 2, 4:
                return Integer.class;
            case 3:
                return Float.class;
            default:
                return Object.class;
        }
    }

}