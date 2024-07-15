package eShop.client.ui.gui;


import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;

public class Bestandshistorie extends JPanel {
    private ArrayList<Integer> bestandlog;
    private ArrayList<Integer> tage;
    private final int min_x = 0;
    private final int max_x = 31;
    private int min_y;
    private int max_y;


    public Bestandshistorie(ArrayList<Integer> bestandlog) {
        this.bestandlog = bestandlog;

        tage = new ArrayList<>();
        for(int i = 1; i <= 30; i++){
            tage.add(i);
        }

        System.out.println(tage);

        min_y = 0;
        max_y = getMaximumValue(bestandlog);
    }

    public void paint(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set the rendering hints for better graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define the center point of the Cartesian plane
        int width = getWidth();
        int height = getHeight();
        int originX = 50; // Margin from the left edge
        int originY = height - 50; // Margin from the bottom edge

        // Draw the x-axis
        g2d.drawLine(originX, originY, width - 20, originY); // Horizontal line
        // Draw the arrow for the x-axis
        g2d.drawLine(width - 20, originY, width - 30, originY - 5); // Arrow part 1
        g2d.drawLine(width - 20, originY, width - 30, originY + 5); // Arrow part 2
        // Label the x-axis
        g2d.drawString("Tage", width - 30, originY + 20);

        // Draw the y-axis
        g2d.drawLine(originX, originY, originX, 20); // Vertical line
        // Draw the arrow for the y-axis
        g2d.drawLine(originX, 20, originX - 5, 30); // Arrow part 1
        g2d.drawLine(originX, 20, originX + 5, 30); // Arrow part 2
        // Label the y-axis
        g2d.drawString("Bestand", originX, 10);


        int xRange = max_x - min_x;
        double xScale = (double) (width - originX - 20) / xRange;

        for(int i = min_x; i < max_x; i++){
            int xPixel = (int) (originX + (i - min_x) * xScale);
            g2d.draw(new Line2D.Double(xPixel, originY - 5, xPixel, originY + 5));
            g2d.drawString(String.valueOf(i), xPixel - 5, originY + 20);
        }

        int yRange = max_y - min_y;
        double yScale = (double) (originY - 20) / yRange;

        if(yRange > 50){
            for (int i = originY; i > 20; i -= 20) {
                g2d.draw(new Line2D.Double(originX - 5, i, originX + 5, i));
                g2d.drawString(String.valueOf((originY - i) * yRange / (originY - 20) + min_y), originX - 25, i + 5);
            }
        } else {
            for(int i = min_y; i < max_y; i++){
                int yPixel = (int) (originY - (i - min_y) * yScale);
                g2d.draw(new Line2D.Double(originX - 5, yPixel, originX + 5, yPixel));
                g2d.drawString(String.valueOf(i), originX - 25, yPixel + 5);
            }
        }

        double oldX = 0;
        double oldY = 0;
        for (int i = 0; i < tage.size(); i++) {
            int xPixel = (int) (originX + (tage.get(i) - min_x) * xScale);
            int yPixel = (int) (originY - (this.bestandlog.get(i) - min_y) * yScale);
            Ellipse2D point = new Ellipse2D.Double(xPixel, yPixel, 1, 1);
            g2d.fill(point);
            if(i == 0){
                g2d.draw(new Line2D.Double(point.getX() - xScale, point.getY(), point.getX(), point.getY()));
            } else {
                g2d.draw(new Line2D.Double(oldX, oldY, point.getX(), point.getY()));
            }
            oldX = point.getX();
            oldY = point.getY();
        }
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
}
