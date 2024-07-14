package eShop.client.ui.gui.models;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomCellRenderer extends DefaultTableCellRenderer {
    public CustomCellRenderer(){
        super.setHorizontalAlignment(JLabel.LEFT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Float) {
            if(hasDecimal((Float) value)){
                setText(String.format("%.2f €", (Float) value));
            } else {
                setText(String.format("%.0f €", (Float) value));
            }
        }
        return c;
    }

    public static boolean hasDecimal(float value) {
        return value % 1 != 0;
    }
}
