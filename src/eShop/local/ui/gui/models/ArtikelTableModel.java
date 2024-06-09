package eShop.local.ui.gui.models;

import eShop.local.entities.Artikel;
import eShop.local.entities.Massengutartikel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ArtikelTableModel extends AbstractTableModel {
    private List<Artikel> artikel;
    private String[] spaltenNamen = {"Nummer", "Bezeichnung", "Bestand", "Preis", "Packungsgröße"};

    public ArtikelTableModel(List<Artikel> aktuelleArtikel){
        super();
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
        Artikel gewaehlterArtikel = artikel.get(rowIndex);

        switch(columnIndex){
            case 0:
                return gewaehlterArtikel.getArtikelnummer();
            case 1:
                return gewaehlterArtikel.getBezeichnung();
            case 2:
                return gewaehlterArtikel.getBestand();
            case 3:
                return gewaehlterArtikel.getPreis();
            case 4:
                if(gewaehlterArtikel instanceof Massengutartikel){
                    return ((Massengutartikel) gewaehlterArtikel).getPackungsgroesse();
                }
            default:
                return null;
        }
    }
}
