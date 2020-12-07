package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Asteroide;

public class Cargo {

    public static final int CARGAISON_VAISSEAU_MAX = 128*128;
    private static int cargaisonVaisseau = 0;
    private static int cargaisonMars = 0;

    public static void transferCargaison(){
        cargaisonMars += cargaisonVaisseau;
        cargaisonVaisseau = 0;
    }

    public static void addCargaisonVaisseau(Asteroide asteroide){
        int cargaison = (int) (Math.pow(asteroide.getWidth(), 2) / 2);

        if (cargaison + cargaisonVaisseau >= CARGAISON_VAISSEAU_MAX){
            cargaisonVaisseau = CARGAISON_VAISSEAU_MAX;
        } else {
            cargaisonVaisseau += cargaison;
        }
    }

    public static int getCargaisonVaisseau() {
        return cargaisonVaisseau;
    }

    public static int getCargaisonMars() {
        return cargaisonMars;
    }
}
