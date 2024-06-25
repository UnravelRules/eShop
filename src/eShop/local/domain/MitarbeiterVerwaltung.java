package eShop.local.domain;

import eShop.local.domain.exceptions.KundeExistiertBereitsException;
import eShop.local.domain.exceptions.MitarbeiterExistiertBereitsException;
import eShop.local.domain.exceptions.MitarbeiterExistiertNichtException;
import eShop.local.entities.Kunde;
import eShop.local.entities.Mitarbeiter;
import eShop.local.persistence.FilePersistenceManager;
import eShop.local.persistence.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;

public class MitarbeiterVerwaltung {
    private ArrayList<Mitarbeiter> registrierteMitarbeiter = new ArrayList<Mitarbeiter>();
    private PersistenceManager pm = new FilePersistenceManager();

    public void liesDaten(String datei) throws IOException {
        pm.openForReading(datei);
        Mitarbeiter einMitarbeiter;

        do {
            einMitarbeiter = pm.ladeMitarbeiter();
            if(einMitarbeiter != null) {
                try{
                    registrieren(einMitarbeiter);
                } catch (MitarbeiterExistiertBereitsException e) {
                    // ...
                }
            }
        } while (einMitarbeiter != null);
        pm.close();
    }

    public void schreibeDaten(String datei) throws IOException {
        pm.openForWriting(datei);

        for (Mitarbeiter mitarbeiter : registrierteMitarbeiter){
            pm.speichereMitarbeiter(mitarbeiter);
        }

        pm.close();
    }

    /**
     * Methode zum Registrieren von Mitarbeitern.
     * Gibt den registrierten Mitarbeiter als Mitarbeiterobjekt zurück
     *
     * @throws MitarbeiterExistiertBereitsException
     * */
    public Mitarbeiter registrieren(Mitarbeiter m) throws MitarbeiterExistiertBereitsException {
        if(registrierteMitarbeiter.contains(m)){
            throw new MitarbeiterExistiertBereitsException(m);
        }
        registrierteMitarbeiter.add(m);
        return m;
    }

    /**
     * Methode zum Anmelden eines Mitarbeiters
     *
     * @param benutzername Benutzername des einloggenden Mitarbeiters
     * @param passwort Passwort des einloggenden Mitarbeiters
     * @throws MitarbeiterExistiertNichtException Falls der Mitarbeiter nicht existiert
     * @return Mitarbeiterobjekt
     */
    public  Mitarbeiter anmelden(String benutzername, String passwort) throws MitarbeiterExistiertNichtException {
        for(Mitarbeiter aktuellerMitarbeiter: registrierteMitarbeiter){
            if(benutzername.equals(aktuellerMitarbeiter.getBenutzername()) && (passwort.equals(aktuellerMitarbeiter.getPasswort()))){
                return aktuellerMitarbeiter;
            }
        }
        throw new MitarbeiterExistiertNichtException(benutzername);
    }

    /**
     * Methode zum Entfernen eines Mitarbeiters
     * @param benutzername
     */
    public void entfernen(String benutzername){
        registrierteMitarbeiter.removeIf(m -> m.getBenutzername().equals(benutzername));
    }
}