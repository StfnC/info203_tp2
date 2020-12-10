package ca.qc.bdeb.info203.tp2.interfaces;

/**
 * Interface qui définit le contrat de l'Observable dans le Patron Observer
 */
public interface Observable {
    /**
     * Ajoute un Observateur
     *
     * @param observateur Observateur à ajouter
     */
    void addObservateur(Observateur observateur);

    /**
     * Informe les Observateurs d'un changement d'état
     */
    void updateObservateurs();
}
