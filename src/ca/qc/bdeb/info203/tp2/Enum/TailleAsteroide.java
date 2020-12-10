package ca.qc.bdeb.info203.tp2.Enum;

/**
 * Tailles qu'un Asteroide peut avoir
 */
public enum TailleAsteroide {
    TRES_GRAND(5),
    GRAND(4),
    MOYEN(3),
    PETIT(2),
    TRES_PETIT(1);

    public final int valeurNumerique;

    /**
     * Construit une TailleAsteroide avec une valeur numérique associée
     *
     * @param valeurNumerique Valeur numérique de la taille
     */
    TailleAsteroide(int valeurNumerique) {
        this.valeurNumerique = valeurNumerique;
    }

    /**
     * Renvoie la TailleAsteroide associée à une valeur numérique
     *
     * @param valeurNumerique Valeur numérique dont ont cherche la TailleAsteroide
     * @return TailleAsteroide associée à la valeur numérique
     */
    public static TailleAsteroide tailleParValeurNumerique(int valeurNumerique) {
        for (TailleAsteroide taille : TailleAsteroide.values()) {
            if (taille.valeurNumerique == valeurNumerique) {
                return taille;
            }
        }
        return null;
    }
}