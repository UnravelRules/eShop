package eShop.client.ui.gui.models;

import eShop.common.entities.Ereignis;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel für die Darstellung der Ereignisse in der ShopClientGUI.
 * Erweitert AbstractTableModel, um Daten für eine JTable-Komponente bereitzustellen.
 */
public class EreignisTableModel extends AbstractTableModel {
    private List<Ereignis> eventlog;
    private String[] spaltenNamen = {"Typ", "Datum", "Benutzer", "Benutzername", "Artikel", "Menge"};

    /**
     * Konstruktor für WarenkorbTableModel mit den initialen Inhalten aus einer HashMap.
     *
     * @param aktuelleEreignisse Die anfänglichen Inhalte der Ereignisse als Liste von Ereignissen.
     */
    public EreignisTableModel(List<Ereignis> aktuelleEreignisse){
        super();
        eventlog = new ArrayList<>(aktuelleEreignisse);
    }

    /**
     * Setzt den Inhalt der Ereignistabelle auf eine neue Liste.
     *
     * @param aktuelleEreignisse Der aktualisierte Inhalt des Ereignislogs.
     */
    public void setEreignisse(List<Ereignis> aktuelleEreignisse){
        eventlog.clear();
        eventlog.addAll(aktuelleEreignisse);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return eventlog.size();
    }

    @Override
    public int getColumnCount() {
        return spaltenNamen.length;
    }

    @Override
    public String getColumnName(int col) {
        return spaltenNamen[col];
    }

    /**
     * Überschreibt die Standardmethode von AbstractTableModel, um unsere Werte richtig in der Tabelle darzustellen.
     * @param columnIndex die einzelnen Indizes der Spalten
     * @param rowIndex die einzelnen Idizes der Zeilen
     * @see javax.swing.table.AbstractTableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ereignis gewaehltesEreignis = eventlog.get(rowIndex);

        switch (columnIndex){
            case 0:
                return gewaehltesEreignis.getEreignisTyp();
            case 1:
                return gewaehltesEreignis.getDatum();
            case 2:
                return gewaehltesEreignis.getAccountTyp();
            case 3:
                return gewaehltesEreignis.getBenutzerName();
            case 4:
                return gewaehltesEreignis.getArtikelbezeichnung();
            case 5:
                return gewaehltesEreignis.getBestandsaenderung();
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
            case 1:
                return LocalDate.class;
            case 5:
                return Integer.class;
            default:
                return Object.class;
        }
    }
}
