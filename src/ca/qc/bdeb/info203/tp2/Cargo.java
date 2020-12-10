package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Asteroide;
import ca.qc.bdeb.info203.tp2.Entite.Vaisseau;

/**
 * Classe qui va gérer le score et la cargaison des minerais
 */
public class Cargo {
    private final int CARGAISON_VAISSEAU_MAX;
    private int cargaisonVaisseau = 0;
    private int cargaisonMars = 0;

    /**
     * Constructeur de la cargaison
     *
     * @param vaisseau Vaisseau qui a le Cargo
     */
    public Cargo(Vaisseau vaisseau) {
        CARGAISON_VAISSEAU_MAX = (int) Math.pow(vaisseau.getHeight(), 2);
    }

    /**
     * Lorsque la touche 'E' est appuyée on transfère le score du vaisseau sur Mars
     */
    public void transferCargaison() {
        cargaisonMars += cargaisonVaisseau;
        cargaisonVaisseau = 0;
    }

    /**
     * Méthode appelée pour ajouter au score
     *
     * @param asteroide L'astéroïde qui sera broyé et ajouté au score
     */
    public void addCargaisonVaisseau(Asteroide asteroide) {
        int cargaison = (int) (Math.pow(asteroide.getWidth(), 2) / 2);

        if (cargaison + cargaisonVaisseau >= CARGAISON_VAISSEAU_MAX) {
            cargaisonVaisseau = CARGAISON_VAISSEAU_MAX;
        } else {
            cargaisonVaisseau += cargaison;
        }
    }

    /**
     * Getter de la cargaison maximale
     */
    public int getCargaisonVaisseauMax() {
        return CARGAISON_VAISSEAU_MAX;
    }

    /**
     * Getter de la cargaison sur le vaisseau
     */
    public int getCargaisonVaisseau() {
        return cargaisonVaisseau;
    }

    /**
     * Getter de la cargaison sur Mars
     */
    public int getCargaisonMars() {
        return cargaisonMars;
    }
}
