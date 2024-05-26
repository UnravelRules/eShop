package eShop.local.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import eShop.local.domain.ArtikelVerwaltung;
import eShop.local.domain.exceptions.ArtikelExistiertNichtException;
import eShop.local.domain.exceptions.UnbekanntesAccountObjektException;
import eShop.local.entities.*;

/**
 * @author teschke
 *
 * Realisierung einer Schnittstelle zur persistenten Speicherung von
 * Daten in Dateien.
 * @see eShop.local.persistence.PersistenceManager
 */
public class FilePersistenceManager implements PersistenceManager {

    private BufferedReader reader = null;
    private PrintWriter writer = null;

    public void openForReading(String datei) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(datei));
    }

    public void openForWriting(String datei) throws IOException {
        writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
    }

    public boolean close() {
        if (writer != null)
            writer.close();

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return false;
            }
        }

        return true;
    }

    /**
     * Methode zum Einlesen der Buchdaten aus einer externen Datenquelle.
     * Das Verfügbarkeitsattribut ist in der Datenquelle (Datei) als "t" oder "f"
     * codiert abgelegt.
     *
     * @return Buch-Objekt, wenn Einlesen erfolgreich, false null
     */
    public Artikel ladeArtikel() throws IOException {
        String mga = liesZeile();
        if (mga == null) {
            // keine Daten mehr vorhanden
            return null;
        }
        boolean massengutartikel = mga.equals("Massengutartikel");

        // Nummer einlesen
        String nummerString = liesZeile();

        int nummer = Integer.parseInt(nummerString);

        // bezeichnung einlesen ...
        String bezeichnung = liesZeile();

        // Bestand einlesen
        String bestandString = liesZeile();
        // ... und von String in int konvertieren
        int bestand = Integer.parseInt(bestandString);

        // Preis einlesen
        String preisString = liesZeile();
        // ... und von String in int konvertieren
        float preis = Float.parseFloat(preisString);

        if(massengutartikel){
            int packungsgroesse = Integer.parseInt(liesZeile());
            return new Massengutartikel(nummer, bezeichnung, bestand, preis, packungsgroesse);
        }

        // neues Artikel-Objekt anlegen und zurückgeben
        return new Artikel(nummer, bezeichnung, bestand, preis);
    }

    /**
     * Methode zum Schreiben der Buchdaten in eine externe Datenquelle.
     * Das Verfügbarkeitsattribut wird in der Datenquelle (Datei) als "t" oder "f"
     * codiert abgelegt.
     *
     * @param a Artikel-Objekt, das gespeichert werden soll
     * @return true, wenn Schreibvorgang erfolgreich, false sonst
     */
    public boolean speichereArtikel(Artikel a) throws IOException {
        // Titel, Nummer und Verfügbarkeit schreiben
        if(a instanceof Massengutartikel){
            schreibeZeile("Massengutartikel");
        } else {
            schreibeZeile("Stückartikel");
        }
        schreibeZeile(a.getArtikelnummer() + "");
        schreibeZeile(a.getBezeichnung());
        schreibeZeile(a.getBestand() + "");
        schreibeZeile(a.getPreis() + "");
        if(a instanceof Massengutartikel){
            schreibeZeile(((Massengutartikel) a).getPackungsgroesse() + "");
        }
        return true;
    }


	public Kunde ladeKunde() throws IOException {
		String nummerString = liesZeile();
        if (nummerString == null) {
            // keine Daten mehr vorhanden
            return null;
        }
        int nummer = Integer.parseInt(nummerString);

        String name = liesZeile();

        String strasse = liesZeile();

        String plz = liesZeile();

        String benutzername = liesZeile();

        String passwort = liesZeile();

		return new Kunde(nummer, name, strasse, plz, benutzername, passwort);
	}

	public boolean speichereKunde(Kunde k) throws IOException {
		schreibeZeile(k.getKundenNummer() + "");
        schreibeZeile(k.getName());
        schreibeZeile(k.getStrasse());
        schreibeZeile(k.getPlz());
        schreibeZeile(k.getBenutzername());
        schreibeZeile(k.getPasswort());

        return true;
	}

    public Mitarbeiter ladeMitarbeiter() throws IOException {
        String nummerString = liesZeile();
        if (nummerString == null) {
            // keine Daten mehr vorhanden
            return null;
        }
        int nummer = Integer.parseInt(nummerString);

        String name = liesZeile();

        String benutzername = liesZeile();

        String passwort = liesZeile();

        return new Mitarbeiter(nummer, name, benutzername, passwort);
    }

    public boolean speichereMitarbeiter(Mitarbeiter m) throws IOException {
        schreibeZeile(m.getMitarbeiterNummer() + "");
        schreibeZeile(m.getName());
        schreibeZeile(m.getBenutzername());
        schreibeZeile(m.getPasswort());

        return true;
    }

    public Ereignis ladeEreignis() throws IOException {
        String ereignisString = liesZeile();
        if(ereignisString == null){
            return null;
        }
        EreignisTyp ereignistyp = EreignisTyp.valueOf(ereignisString);

        String datumString = liesZeile();
        LocalDate datum = LocalDate.parse(datumString);

        AccountTyp account = AccountTyp.valueOf(liesZeile());

        String benutzername = liesZeile();

        String artikelbezeichnung = liesZeile();

        int betragsaenderung = Integer.parseInt(liesZeile());

        return new Ereignis(ereignistyp, account, benutzername, betragsaenderung, datum, artikelbezeichnung);
    }

    public boolean speichereEreignis(Ereignis e){
        schreibeZeile(e.getEreignisTyp() + "");
        schreibeZeile(e.getDatum() + "");
        schreibeZeile(e.getAccountTyp().name());
        schreibeZeile(e.getBenutzerName());
        schreibeZeile(e.getArtikelbezeichnung());
        schreibeZeile(e.getBestandsaenderung() + "");

        return true;
    }

    /*
     * Private Hilfsmethoden
     */

    private String liesZeile() throws IOException {
        if (reader != null)
            return reader.readLine();
        else
            return "";
    }

    private void schreibeZeile(String daten) {
        if (writer != null)
            writer.println(daten);
    }
}
