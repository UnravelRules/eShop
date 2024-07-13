package eShop.common.entities;

import java.util.HashMap;

public class Warenkorb {
    HashMap<Artikel, Integer> inhalt;

    public Warenkorb(){
        inhalt = new HashMap<>();
    }

    public void artikelInWarenkorb(Artikel a, int anzahl){
        if (inhalt.containsKey(a)){
            int alteAnzahl = inhalt.get(a);
            inhalt.put(a, alteAnzahl + anzahl);
            return;
        }

        inhalt.put(a, anzahl);
    }

    public void inhaltVeraendern(Artikel a, int anzahl){
        inhalt.put(a, anzahl);
    }

    public void warenkorbLeeren(){
        inhalt.clear();
    }

    public HashMap<Artikel, Integer> getInhalt(){
        return inhalt;
    }
}
