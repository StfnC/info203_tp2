package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Asteroide;
import ca.qc.bdeb.info203.tp2.Entite.Vaisseau;

public class Cargo {
    private final int CARGAISON_VAISSEAU_MAX;
    private int cargaisonVaisseau = 0;
    private int cargaisonMars = 0;

    public Cargo(Vaisseau vaisseau) {
        CARGAISON_VAISSEAU_MAX = (int) Math.pow(vaisseau.getHeight(), 2);
    }

    public void transferCargaison() {
        cargaisonMars += cargaisonVaisseau;
        cargaisonVaisseau = 0;
    }

    public void addCargaisonVaisseau(Asteroide asteroide) {
        int cargaison = (int) (Math.pow(asteroide.getWidth(), 2) / 2);

        if (cargaison + cargaisonVaisseau >= CARGAISON_VAISSEAU_MAX) {
            cargaisonVaisseau = CARGAISON_VAISSEAU_MAX;
        } else {
            cargaisonVaisseau += cargaison;
        }
    }

    public int getCargaisonVaisseauMax() {
        return CARGAISON_VAISSEAU_MAX;
    }

    public int getCargaisonVaisseau() {
        return cargaisonVaisseau;
    }

    public int getCargaisonMars() {
        return cargaisonMars;
    }
}
