package eShop.local.domain;

import eShop.common.exceptions.KundeExistiertBereitsException;
import eShop.common.exceptions.KundeExistiertNichtException;
import eShop.common.entities.Kunde;
import eShop.local.persistence.FilePersistenceManager;
import eShop.local.persistence.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;

public class KundenVerwaltung {
    private ArrayList<Kunde> kundenliste = new ArrayList<Kunde>();

    private PersistenceManager pm = new FilePersistenceManager();

    public void liesDaten(String datei) throws IOException {
        pm.openForReading(datei);
        Kunde einKunde;
        do {
            einKunde = pm.ladeKunde();
            if(einKunde != null) {
                try{
                    registrieren(einKunde);
                } catch (KundeExistiertBereitsException e) {
                    // ...
                }
            }
        } while (einKunde != null);
        pm.close();
    }

    public void schreibeDaten(String datei) throws IOException {
        pm.openForWriting(datei);

        for (Kunde kunde : kundenliste){
            pm.speichereKunde(kunde);
        }

        pm.close();
    }

    /**
     * Methode zum Registrieren eines neuen Kunden
     * @return Kundenobjekt
     * @throws KundeExistiertBereitsException
     */
    public Kunde registrieren(Kunde kunde) throws KundeExistiertBereitsException {
        if (kundenliste.contains(kunde)) {
            throw new KundeExistiertBereitsException(kunde);
        } else {
            kundenliste.add(kunde);
            return kunde;
        }
    }

    /**
     * Methode zum Einloggen als Kunde
     * @param benutzername
     * @param passwort
     * @return Kundenobjekt
     * @throws KundeExistiertNichtException
     */
    public Kunde login(String benutzername, String passwort) throws KundeExistiertNichtException {
        for (Kunde k : kundenliste){
            if(k.getBenutzername().equals(benutzername) && k.getPasswort().equals(passwort)){
                return k;
            }
        }
        throw new KundeExistiertNichtException(benutzername);
    }

    /**
     * Gibt eine Liste aller Kunden als ArrayList<Kunde> zurück
     * @return Kundenliste
     */
    public ArrayList<Kunde> getKundenliste(){
        return new ArrayList<Kunde>(kundenliste);
    }
}
