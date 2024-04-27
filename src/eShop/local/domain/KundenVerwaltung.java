package eShop.local.domain;

import eShop.local.domain.exceptions.KundeExistiertBereitsException;
import eShop.local.entities.Kunde;

import java.util.ArrayList;

public class KundenVerwaltung {
    private ArrayList<Kunde> kundenliste = new ArrayList<Kunde>();

    // ein Nutzer kann sich als Kunden registrieren

    public Kunde registrieren(String name, String str, String plz, String benutzer, String passwort) throws KundeExistiertBereitsException {
        Kunde kunde = new Kunde(getKundenliste().size() + 1, name, str, plz, benutzer, passwort);

        if (kundenliste.contains(kunde)) {
            throw new KundeExistiertBereitsException();
        } else {
            kundenliste.add(kunde);
            System.out.println(kundenliste);
            return kunde;
        }
    }

    // ein bestehender Kunde kann sich mit seinen Daten einloggen
    public void login(){}

    public ArrayList<Kunde> getKundenliste(){
        return new ArrayList<Kunde>(kundenliste);
    }
}
