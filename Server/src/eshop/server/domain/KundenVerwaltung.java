package eshop.server.domain;

import eShop.common.exceptions.KundeExistiertBereitsException;
import eShop.common.exceptions.KundeExistiertNichtException;
import eShop.common.exceptions.LoginFehlgeschlagenException;
import eShop.common.entities.Kunde;
import eshop.server.persistence.FilePersistenceManager;
import eshop.server.persistence.PersistenceManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Verwaltet alle Kunden
 */
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
     * @throws KundeExistiertBereitsException Es existiert bereits ein Kunde mit diesem Benutzernamen oder dieser Nummer
     */
    public Kunde registrieren(Kunde kunde) throws KundeExistiertBereitsException {
        if (kundenliste.contains(kunde)) {
            throw new KundeExistiertBereitsException(kunde.getBenutzername());
        } else {
            kundenliste.add(kunde);
            return kunde;
        }
    }

    /**
     * Methode zum Einloggen als Kunde
     * @param benutzername Benutzername
     * @param passwort Passwort
     * @return Kundenobjekt
     * @throws LoginFehlgeschlagenException Benutzername oder Passwort sind nicht korrekt
     */
    public Kunde login(String benutzername, String passwort) throws LoginFehlgeschlagenException {
        for (Kunde k : kundenliste){
            if(k.getBenutzername().equals(benutzername) && k.getPasswort().equals(passwort)){
                return k;
            }
        }
        throw new LoginFehlgeschlagenException(benutzername, passwort);
    }

    /**
     * Gibt eine Liste aller Kunden als ArrayList zurück
     * @return Kundenliste
     */
    public ArrayList<Kunde> getKundenliste(){
        return new ArrayList<Kunde>(kundenliste);
    }
}
