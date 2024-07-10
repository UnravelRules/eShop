package eShop.local.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Bestandshistorie extends JPanel {
    private ArrayList<Integer> bestandlog;
    private int[] tage;

    public Bestandshistorie(ArrayList<Integer> bestandlog) {
        this.bestandlog = bestandlog;

        tage = new int[30];
        for(int i = 1; i <= 30; i++){
            tage[i - 1] = i;
        }
    }

    public void paint(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int minimumY = getMinimumValue(bestandlog);
        int maximumY = getMaximumValue(bestandlog);

        AffineTransform transform = new AffineTransform();
        transform.translate(0, height);
        transform.scale(1, -1);

        g2d.setTransform(transform);

        int xScale = (int) (width / (double)(30 - 1));
        int yScale = (int) (height / (double)(getMaximumValue(bestandlog)));

        g2d.setStroke(new BasicStroke(2));
        drawCartesianPlane(g2d, width, height, minimumY, maximumY);

        for (int i = 0; i < tage.length; i++) {
            g2d.fillOval(tage[i] + xScale, this.bestandlog.get(i) + yScale, 5, 5);
        }
    }

    public void drawCartesianPlane(Graphics2D g2d, int width, int height, int minimumY, int maximumY){
        int margin = 20;
        g2d.drawLine(0, margin, width - 5, margin); // X-Achse


        g2d.drawLine(margin, 0, margin, height - margin); // Y-Achse
    }

    public int getMaximumValue(ArrayList<Integer> bestandlog){
        int maxValue = 0;
        for(Integer value : bestandlog){
            if(value > maxValue){
                maxValue = value;
            }
        }
        return maxValue + 5;
    }

    public int getMinimumValue(ArrayList<Integer> bestandlog){
        int minValue = getMaximumValue(bestandlog);
        for(Integer value : bestandlog){
            if(value < minValue){
                minValue = value;
            }
        }
        return minValue;
    }
}
