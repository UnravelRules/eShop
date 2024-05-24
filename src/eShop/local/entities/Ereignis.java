package eShop.local.entities;

import eShop.local.domain.exceptions.UnbekanntesAccountObjektException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ereignis {

    private EreignisTyp ereignisTyp;

    private AccountTyp accountTyp;

    private int bestandsaenderung;

    // Hier sollte Benutzername noch zu Account Objekt geändert werden, sobald das implementiert ist.
    private String benutzerName;

    private LocalDate datum;

    private Artikel artikel;

    public Ereignis(EreignisTyp ereignis_typ, Object account, int delta, LocalDate ereignis_datum, Artikel ereignis_artikel) throws UnbekanntesAccountObjektException {

        int accountNummer = 0;
        if(account instanceof Mitarbeiter) {
            accountTyp = AccountTyp.MITARBEITER;
            benutzerName = ((Mitarbeiter) account).getBenutzername();
        } else if (account instanceof Kunde) {
            accountTyp = AccountTyp.KUNDE;
            benutzerName = ((Kunde) account).getBenutzername();
        } else
            throw  new UnbekanntesAccountObjektException();
        ereignisTyp = ereignis_typ;
        bestandsaenderung = delta;
        artikel = ereignis_artikel;
        datum = ereignis_datum;
    }

    @Override
    public String toString() {
        DateTimeFormatter datumFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datumString = datum.format(datumFormatter);
        return String.format("%s, %s, %s, %s, %s, Menge: %d", ereignisTyp.name(), datumString, accountTyp.name(), benutzerName, artikel.bezeichnung, bestandsaenderung);
    }
}
