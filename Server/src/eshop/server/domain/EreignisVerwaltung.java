package eshop.server.domain;

import eShop.common.exceptions.UnbekanntesAccountObjektException;
import eShop.common.entities.*;
import eshop.server.persistence.FilePersistenceManager;
import eshop.server.persistence.PersistenceManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Verwaltet alle Ereignisse
 */
public class EreignisVerwaltung {
    private ArrayList<Ereignis> eventlog = new ArrayList<>();
    private PersistenceManager pm = new FilePersistenceManager();

    private AccountTyp accountTyp;


    /**
     * Liest den Ereignislog aus einer Datei
     * @param datei Datei
     * @throws IOException Fehler beim Einlesen
     */
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

    /**
     * Speichert die Ereignisse aus dem Log in einer Datei
     * @param datei Ereignislogdatei
     * @throws IOException Fehler beim Speichern
     */
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
            throw new UnbekanntesAccountObjektException();
        Ereignis ereignis = new Ereignis(ereignisTyp, accountTyp , benutzerName, delta, datum, artikel.getBezeichnung());
        eventlog.add(ereignis);
        return eventlog;
    }

    public ArrayList<Ereignis> getEventlog(){
        return eventlog;
    }

}
