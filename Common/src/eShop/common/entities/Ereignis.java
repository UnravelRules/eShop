package eShop.common.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ereignis {

    private EreignisTyp ereignisTyp;

    private AccountTyp accountTyp;

    private int bestandsaenderung;

    // Hier sollte Benutzername noch zu Account Objekt geändert werden, sobald das implementiert ist.
    private String benutzerName;

    private LocalDate datum;

    private String artikelbezeichnung;

    public Ereignis(EreignisTyp ereignis_typ, AccountTyp account, String benutzerName, int delta, LocalDate ereignis_datum, String artikelbezeichnung) {
        ereignisTyp = ereignis_typ;
        bestandsaenderung = delta;
        this.artikelbezeichnung = artikelbezeichnung;
        datum = ereignis_datum;
        accountTyp = account;
        this.benutzerName = benutzerName;
    }

    @Override
    public String toString() {
        DateTimeFormatter datumFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datumString = datum.format(datumFormatter);
        return String.format("%s, %s, %s, %s, %s, Menge: %d", ereignisTyp.name(), datumString, accountTyp.name(), benutzerName, artikelbezeichnung, bestandsaenderung);
    }

    public EreignisTyp getEreignisTyp() {
        return ereignisTyp;
    }

    public AccountTyp getAccountTyp() {
        return accountTyp;
    }

    public int getBestandsaenderung() {
        return bestandsaenderung;
    }

    public String getBenutzerName() {
        return benutzerName;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public String getArtikelbezeichnung() {
        return artikelbezeichnung;
    }
}
