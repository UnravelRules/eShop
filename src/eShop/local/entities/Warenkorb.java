package eShop.local.entities;

import java.util.HashMap;

public class Warenkorb {
    HashMap<Artikel, Integer> warenkorb;

    public Warenkorb(){
        this.warenkorb = new HashMap<>();
    }

    public HashMap<Artikel, Integer> getHashmap() {
        return warenkorb;
    }
}
