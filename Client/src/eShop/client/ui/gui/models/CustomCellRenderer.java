package eShop.client.ui.gui.models;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Benutzerdefinierter Zellenrenderer für eine JTable, der die Darstellung von Werten anpasst.
 */
public class CustomCellRenderer extends DefaultTableCellRenderer {
    /**
     * Konstruktor für den benutzerdefinierten Zellenrenderer.
     * Setzt die horizontale Ausrichtung der Zelleninhalte auf links.
     */
    public CustomCellRenderer(){
        super.setHorizontalAlignment(JLabel.LEFT);
    }

    /**
     * Überschreibt die Methode getTableCellRendererComponent, um die Darstellung der Zelleninhalte anzupassen.
     *
     * @param table Die JTable-Komponente, zu der die Zelle gehört.
     * @param value Der Wert der Zelle.
     * @param isSelected Gibt an, ob die Zelle ausgewählt ist.
     * @param row Die Zeilennummer der Zelle.
     * @param column Die Spaltennummer der Zelle.
     * @return Die gerenderte Komponente für die Zelle.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Float) {
            if(hasDecimal((Float) value)){
                setText(String.format("%.2f€", (Float) value));
            } else {
                setText(String.format("%.0f€", (Float) value));
            }
        }
        return c;
    }

    /**
     * Überprüft, ob eine Gleitkommazahl Dezimalstellen hat.
     *
     * @param value Die zu prüfende Gleitkommazahl.
     * @return True, wenn die Zahl Dezimalstellen hat, ansonsten false.
     */
    public static boolean hasDecimal(float value) {
        return value % 1 != 0;
    }
}
