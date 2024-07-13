package eShop.local.persistence;

import java.io.IOException;

import eShop.common.entities.Artikel;
import eShop.common.entities.Ereignis;
import eShop.common.entities.Kunde;
import eShop.common.entities.Mitarbeiter;

/**
 * Allgemeine Schnittstelle für den Zugriff auf ein Speichermedium
 * (z.B. Datei oder Datenbank) zum Ablegen von beispielsweise
 * Bücher- oder Kundendaten.
 *
 * Das Interface muss von Klassen implementiert werden, die eine
 * Persistenz-Schnittstelle realisieren wollen.
 */
public interface PersistenceManager {

    public void openForReading(String datenquelle) throws IOException;

    public void openForWriting(String datenquelle) throws IOException;

    public boolean close();

    /**
     * Methode zum Einlesen der Buchdaten aus einer externen Datenquelle.
     *
     * @return Artikel-Objekt, wenn Einlesen erfolgreich, false null
     */
    public Artikel ladeArtikel() throws IOException;

    /**
     * Methode zum Schreiben der Buchdaten in eine externe Datenquelle.
     *
     * @param a Artikel-Objekt, das gespeichert werden soll
     * @return true, wenn Schreibvorgang erfolgreich, false sonst
     */
    public boolean speichereArtikel(Artikel a) throws IOException;

	public Kunde ladeKunde() throws IOException;

	public boolean speichereKunde(Kunde k) throws IOException;

    public Mitarbeiter ladeMitarbeiter() throws IOException;

    public boolean speichereMitarbeiter(Mitarbeiter m) throws IOException;

    public Ereignis ladeEreignis() throws IOException;

    public boolean speichereEreignis(Ereignis e) throws IOException;
}
