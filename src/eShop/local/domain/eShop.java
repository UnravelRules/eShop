package eShop.local.domain;

import eShop.local.domain.exceptions.KundeExistiertBereitsException;
import eShop.local.entities.Kunde;

public class eShop {
    private KundenVerwaltung kundenVW;
    private MitarbeiterVerwaltung mitarbeiterVW;
    private ArtikelVerwaltung artikelVW;

    public eShop(){
        kundenVW = new KundenVerwaltung();
        mitarbeiterVW = new MitarbeiterVerwaltung();
        artikelVW = new ArtikelVerwaltung();
    }

    public Kunde kundenAnlegen(String name, String str, String plz, String benutzer, String passwort) throws KundeExistiertBereitsException {
        // Kunde k = new Kunde(kundenVW.getKundenliste().size() + 1, name, str, plz, benutzer, passwort);
        return kundenVW.registrieren(name, str, plz, benutzer, passwort);
    }
    public void kundenEinloggen(){

    }
}
