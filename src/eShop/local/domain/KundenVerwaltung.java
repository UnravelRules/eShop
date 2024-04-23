package eShop.local.domain;

import eShop.local.domain.exceptions.KundeExistiertBereitsException;
import eShop.local.entities.Kunde;
import eShop.local.entities.Mitarbeiter;

import java.util.ArrayList;

public class KundenVerwaltung {
    private ArrayList<Kunde> kundenliste = new ArrayList<Kunde>();

    // ein Nutzer kann sich als Kunden registrieren

    public void einfuegen(Kunde kunde) throws KundeExistiertBereitsException {
        if(kundenliste.contains(kunde)) {
            throw new KundeExistiertBereitsException();
        }
        kundenliste.add(kunde);
    }
    public void registrieren(){}

    // ein bestehender Kunde kann sich mit seinen Daten einloggen
    public void login(){}


}
