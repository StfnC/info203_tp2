package ca.qc.bdeb.info203.tp2.interfaces;

/**
 * Interface qui définit le contrat de l'Observateur dans le Patron Observer
 */
public interface Observateur {
    /**
     * Action que prend l'Observateur lorsque l'état d'un Observable à changé
     *
     * @param millis Temps en millisecondes lorsque l'update est survenue
     */
    void update(long millis);
}
