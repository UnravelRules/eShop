package eShop.common.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * die Klasse Ereignis steht für ein Ereignis im EShop, welches bei jeder Änderung von Artikeln erstellt wird.
 */
public class Ereignis {
    private EreignisTyp ereignisTyp;
    private AccountTyp accountTyp;
    private int bestandsaenderung;
    private String benutzerName;
    private LocalDate datum;
    private String artikelbezeichnung;

    /**
     * Konstruktor zur Initialisierung eines Ereignisses mit gegebenen Parametern.
     *
     * @param ereignis_typ Der Typ des Ereignisses (EINLAGERUNG, AUSLAGERUNG, KAUF, NEU).
     * @param account Der Typ des Accounts, der das Ereignis verursacht hat (z.B. KUNDE, MITARBEITER).
     * @param benutzerName Der Benutzername des Accounts, der das Ereignis verursacht hat.
     * @param delta Die Änderung des Bestands im Ereignis.
     * @param ereignis_datum Das Datum, an dem das Ereignis stattgefunden hat.
     * @param artikelbezeichnung Die Bezeichnung des betroffenen Artikels.
     */
    public Ereignis(EreignisTyp ereignis_typ, AccountTyp account, String benutzerName, int delta, LocalDate ereignis_datum, String artikelbezeichnung) {
        ereignisTyp = ereignis_typ;
        bestandsaenderung = delta;
        this.artikelbezeichnung = artikelbezeichnung;
        datum = ereignis_datum;
        accountTyp = account;
        this.benutzerName = benutzerName;
    }

    /**
     * Gibt eine String-Repräsentation des Ereignisses zurück.
     *
     * @return Eine formatierte Zeichenkette mit den Details des Ereignisses.
     */
    @Override
    public String toString() {
        DateTimeFormatter datumFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datumString = datum.format(datumFormatter);
        return String.format("%s, %s, %s, %s, %s, Menge: %d", ereignisTyp.name(), datumString, accountTyp.name(), benutzerName, artikelbezeichnung, bestandsaenderung);
    }

    /**
     * Gibt den Typ des Ereignisses zurück.
     *
     * @return Der Typ des Ereignisses.
     */
    public EreignisTyp getEreignisTyp() {
        return ereignisTyp;
    }

    /**
     * Gibt den Typ des Accounts zurück, der das Ereignis verursacht hat.
     *
     * @return Der Typ des Accounts.
     */
    public AccountTyp getAccountTyp() {
        return accountTyp;
    }

    /**
     * Gibt die Änderung des Bestands zurück, die durch das Ereignis verursacht wurde.
     *
     * @return Die Änderung des Bestands.
     */
    public int getBestandsaenderung() {
        return bestandsaenderung;
    }

    /**
     * Gibt den Benutzernamen des Accounts zurück, der das Ereignis verursacht hat.
     *
     * @return Der Benutzername des Accounts.
     */
    public String getBenutzerName() {
        return benutzerName;
    }

    /**
     * Gibt das Datum zurück, an dem das Ereignis stattgefunden hat.
     *
     * @return Das Datum des Ereignisses.
     */
    public LocalDate getDatum() {
        return datum;
    }

    /**
     * Gibt die Bezeichnung des betroffenen Artikels zurück.
     *
     * @return Die Bezeichnung des Artikels.
     */
    public String getArtikelbezeichnung() {
        return artikelbezeichnung;
    }
}
