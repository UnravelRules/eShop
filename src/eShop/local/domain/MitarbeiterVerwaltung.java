package eShop.local.domain;

import eShop.local.domain.exceptions.MitarbeiterExistiertBereitsException;
import eShop.local.domain.exceptions.MitarbeiterExistiertNichtException;
import eShop.local.entities.Mitarbeiter;

import java.util.ArrayList;

public class MitarbeiterVerwaltung {
    private ArrayList<Mitarbeiter> registrierteMitarbeiter = new ArrayList<Mitarbeiter>();

    public Mitarbeiter registrieren(int nummer,String name, String benutzername, String passwort) throws MitarbeiterExistiertBereitsException {
        for(Mitarbeiter aktuellerMitarbeiter: this.registrierteMitarbeiter){
            if((benutzername.equals(aktuellerMitarbeiter.getBenutzername()) || nummer==aktuellerMitarbeiter.getMitarbeiterNummer())) {
                throw new MitarbeiterExistiertBereitsException();
            }
        }
        Mitarbeiter neuerMitarbeiter = new Mitarbeiter(nummer, name, benutzername, passwort);
        this.registrierteMitarbeiter.add(neuerMitarbeiter);
        return neuerMitarbeiter;
    }

    public  Mitarbeiter anmelden(String benutzername, String passwort) throws MitarbeiterExistiertNichtException {
        for(Mitarbeiter aktuellerMitarbeiter: this.registrierteMitarbeiter){
            if(benutzername.equals(aktuellerMitarbeiter.getBenutzername()) && (passwort.equals(aktuellerMitarbeiter.getPasswort()))){
                return aktuellerMitarbeiter;
            }
        }
        throw new MitarbeiterExistiertNichtException();
    }

}