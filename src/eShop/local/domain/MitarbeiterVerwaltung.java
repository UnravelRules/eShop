package eShop.local.domain;

import eShop.local.domain.exceptions.MitarbeiterExistiertBereitsException;
import eShop.local.domain.exceptions.MitarbeiterExistiertNichtException;
import eShop.local.entities.Mitarbeiter;

import java.util.ArrayList;

public class MitarbeiterVerwaltung {
    private ArrayList<Mitarbeiter> registrierteMitarbeiter = new ArrayList<Mitarbeiter>();
    // NUR ZUM TESTEN (brauchen einen Mitarbeiter, um als Mitarbeiter anzumelden & neue Mitarbeiter anzulegen)
    public MitarbeiterVerwaltung(){
        Mitarbeiter m = new Mitarbeiter(1, "Fabian", "fharjes", "ekb");
        Mitarbeiter j = new Mitarbeiter(2, "Jan", "jsteinmueller", "admin");
        registrierteMitarbeiter.add(m);
        registrierteMitarbeiter.add(j);
    }

    /**
     * Methode zum Registrieren von Mitarbeitern
     *
     * @param nummer Mitarbeiternummer des neuen Mitarbeiters
     * @param name Name des neuen Mitarbeiters
     * @param benutzername Benutzername des neuen Mitarbeiters
     * @param passwort Passwort des neuen Mitarbeiters
     *
     * @throws MitarbeiterExistiertBereitsException
     * */
    public Mitarbeiter registrieren(int nummer, String name, String benutzername, String passwort) throws MitarbeiterExistiertBereitsException {
        Mitarbeiter neuerMitarbeiter = new Mitarbeiter(nummer, name, benutzername, passwort);
        if(registrierteMitarbeiter.contains(neuerMitarbeiter)){
            throw new MitarbeiterExistiertBereitsException();
        }
        registrierteMitarbeiter.add(neuerMitarbeiter);
        return neuerMitarbeiter;
    }

    /**
     * Methode zum Anmelden eines Mitarbeiters
     *
     * @param benutzername Benutzername des einloggenden Mitarbeiters
     * @param passwort Passwort des einloggenden Mitarbeiters
     * @throws MitarbeiterExistiertNichtException Falls der Mitarbeiter nicht existiert
     */
    public  Mitarbeiter anmelden(String benutzername, String passwort) throws MitarbeiterExistiertNichtException {
        for(Mitarbeiter aktuellerMitarbeiter: registrierteMitarbeiter){
            if(benutzername.equals(aktuellerMitarbeiter.getBenutzername()) && (passwort.equals(aktuellerMitarbeiter.getPasswort()))){
                return aktuellerMitarbeiter;
            }
        }
        throw new MitarbeiterExistiertNichtException();
    }

    public void entfernen(String benutzername){
        registrierteMitarbeiter.removeIf(m -> m.getBenutzername().equals(benutzername));
    }
}