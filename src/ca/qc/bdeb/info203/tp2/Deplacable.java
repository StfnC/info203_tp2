package ca.qc.bdeb.info203.tp2;

/**
 * Interface qui définit la structure des objets qui peuvent se déplacer
 */
public interface Deplacable {
    /**
     * Définit comment l'objet se déplace
     *
     * @param delta Intervalle de temps entre la frame précédente et celle actuelle
     */
    void deplacer(int delta);
}
