package eShop.local.ui.gui.models;

import eShop.local.entities.Artikel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarenkorbTableModel extends AbstractTableModel {
    private HashMap<Artikel, Integer> inhalt;
    private List<Artikel> keys;
    private String[] spaltenNamen = {"Artikelnummer", "Bezeichnung", "Preis", "Anzahl", "Gesamtpreis"};

    public WarenkorbTableModel(HashMap<Artikel, Integer> aktuellerInhalt){
        super();
        inhalt = new HashMap<>(aktuellerInhalt);
    }

    public void setInhalt(HashMap<Artikel, Integer> aktuellerInhalt){
        inhalt.clear();
        inhalt.putAll(aktuellerInhalt);
        fireTableDataChanged();
    }
    @Override
    public int getRowCount() {
        return inhalt.size();
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
        keys = new ArrayList<>(inhalt.keySet());

        Artikel gewaehlterInhalt = keys.get(rowIndex);
        int anzahl = inhalt.get(gewaehlterInhalt);

        switch (columnIndex){
            case 0:
                return gewaehlterInhalt.getArtikelnummer();
            case 1:
                return gewaehlterInhalt.getBezeichnung();
            case 2:
                return gewaehlterInhalt.getPreis();
            case 3:
                return anzahl;
            case 4:
                return anzahl * gewaehlterInhalt.getPreis();
            default:
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
            case 0, 3:
                return Integer.class;
            case 2, 4:
                return Float.class;
            default:
                return Object.class;
        }
    }
}
