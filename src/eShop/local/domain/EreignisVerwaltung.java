package eShop.local.domain;

import eShop.local.domain.exceptions.ArtikelExistiertNichtException;
import eShop.local.domain.exceptions.UnbekanntesAccountObjektException;
import eShop.local.entities.*;
import eShop.local.persistence.FilePersistenceManager;
import eShop.local.persistence.PersistenceManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EreignisVerwaltung {
    private ArrayList<Ereignis> eventlog = new ArrayList<>();
    private PersistenceManager pm = new FilePersistenceManager();

    private AccountTyp accountTyp;


    public void liesDaten(String datei) throws IOException {
        pm.openForReading(datei);
        Ereignis einEreignis;

        do{
            einEreignis = pm.ladeEreignis();
            if(einEreignis != null){
                eventlog.add(einEreignis);
            }
        } while(einEreignis != null);
        pm.close();
    }

    public void schreibeDaten(String datei) throws IOException{
        pm.openForWriting(datei);

        for(Ereignis ereignis : eventlog){
            pm.speichereEreignis(ereignis);
        }
        pm.close();
    }

    /**
     * Legt einen neuen Eintrag im Eventlog an.
     * Dabei werden Datum, Accounttyp, Benutzername, Accountnummer, Artikelbezeichnung, Artikelnummer und der neue Bestand gespeichert.
     *
     * @param account
     * @param artikel
     * @return AktualisierterEventlog
     * @throws UnbekanntesAccountObjektException
     */

    /**
     * Gibt den Eventlog als ArrayList<String> zurück
     * @return Eventlog
     */
    ArrayList<Ereignis> updateEventlog(EreignisTyp ereignisTyp, Object account, Artikel artikel, int delta) throws UnbekanntesAccountObjektException {
        LocalDate datum = LocalDate.now();
        String benutzerName;

        if(account instanceof Mitarbeiter) {
            accountTyp = AccountTyp.MITARBEITER;
            benutzerName = ((Mitarbeiter) account).getBenutzername();
        } else if (account instanceof Kunde) {
            accountTyp = AccountTyp.KUNDE;
            benutzerName = ((Kunde) account).getBenutzername();
        } else
            throw  new UnbekanntesAccountObjektException();
        Ereignis ereignis = new Ereignis(ereignisTyp, accountTyp , benutzerName, delta, datum, artikel.getBezeichnung());
        eventlog.add(ereignis);
        return eventlog;
    }

    public ArrayList<Ereignis> getEventlog(){
        return eventlog;
    }

}
