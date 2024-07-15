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

/**
 * Diese Klasse stellt ein JPanel dar, das die Bestandshistorie (Inventarverlauf) eines Produkts
 * über einen Zeitraum von 30 Tagen visualisiert. Die x-Achse stellt die Tage dar und die y-Achse
 * stellt den Bestand dar.
 */
public class Bestandshistorie extends JPanel {
    private ArrayList<Integer> bestandlog; // Liste, die die Bestandswerte hält
    private ArrayList<Integer> tage; // Liste, die die Tage (1 bis 30) hält
    private final int min_x = 0; // Minimalwert für die x-Achse (Tage)
    private final int max_x = 31; // Maximalwert für die x-Achse (Tage)
    private int min_y; // Minimalwert für die y-Achse (Bestand)
    private int max_y; // Maximalwert für die y-Achse (Bestand)

    /**
     * Konstruktor der Klasse Bestandshistorie.
     * Initialisiert die Bestandshistorie mit den übergebenen Bestandswerten.
     *
     * @param bestandlog Liste der Bestandswerte
     */
    public Bestandshistorie(ArrayList<Integer> bestandlog) {
        this.bestandlog = bestandlog;

        // Initialisiert die Tage von 1 bis 30
        tage = new ArrayList<>();
        for(int i = 1; i <= 30; i++){
            tage.add(i);
        }

        // Setzt den Minimalwert für die y-Achse auf 0
        min_y = 0;
        // Setzt den Maximalwert für die y-Achse auf den höchsten Bestandswert plus 5
        max_y = getMaximumValue(bestandlog) + 5;
    }

    /**
     * Überschreibt die paint-Methode, um die Bestandshistorie zu zeichnen.
     *
     * @param g Graphics-Objekt zum Zeichnen
     */
    public void paint(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Setzt die Rendering-Hints für bessere Grafik
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Definiert den Ursprungspunkt des kartesischen Koordinatensystems
        int width = getWidth();
        int height = getHeight();
        int originX = 50; // Rand vom linken Rand
        int originY = height - 50; // Rand vom unteren Rand

        // Zeichnet die x-Achse
        g2d.drawLine(originX, originY, width - 20, originY); // Horizontale Linie
        // Zeichnet den Pfeil für die x-Achse
        g2d.drawLine(width - 20, originY, width - 30, originY - 5); // Pfeil Teil 1
        g2d.drawLine(width - 20, originY, width - 30, originY + 5); // Pfeil Teil 2
        // Beschriftet die x-Achse
        g2d.drawString("Tage", width - 30, originY + 20);

        // Zeichnet die y-Achse
        g2d.drawLine(originX, originY, originX, 20); // Vertikale Linie
        // Zeichnet den Pfeil für die y-Achse
        g2d.drawLine(originX, 20, originX - 5, 30); // Pfeil Teil 1
        g2d.drawLine(originX, 20, originX + 5, 30); // Pfeil Teil 2
        // Beschriftet die y-Achse
        g2d.drawString("Bestand", originX, 10);

        // Berechnet die Skala für die x-Achse
        int xRange = max_x - min_x;
        double xScale = (double) (width - originX - 20) / xRange;

        // Zeichnet die x-Achsen-Markierungen
        for(int i = min_x; i < max_x; i++){
            int xPixel = (int) (originX + (i - min_x) * xScale);
            g2d.draw(new Line2D.Double(xPixel, originY - 5, xPixel, originY + 5));
            g2d.drawString(String.valueOf(i), xPixel - 5, originY + 20);
        }

        // Berechnet die Skala für die y-Achse
        int yRange = max_y - min_y;
        double yScale = (double) (originY - 20) / yRange;

        // Zeichnet die y-Achsen-Markierungen
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

        // Zeichnet die Bestandskurve
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

    /**
     * Methode zur Bestimmung des höchsten Wertes in der Bestandshistorie.
     *
     * @param bestandlog Liste der Bestandswerte
     * @return Der höchste Bestandswert plus 5
     */
    public int getMaximumValue(ArrayList<Integer> bestandlog){
        int maxValue = 0;
        for(Integer value : bestandlog){
            if(value > maxValue){
                maxValue = value;
            }
        }
        return maxValue;
    }
}
