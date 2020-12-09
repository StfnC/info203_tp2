package ca.qc.bdeb.info203.tp2.Enum;

public enum TailleAsteroide {
    TRES_GRAND(5),
    GRAND(4),
    MOYEN(3),
    PETIT(2),
    TRES_PETIT(1);

    public final int valeurNumerique;

    private TailleAsteroide(int valeurNumerique) {
        this.valeurNumerique = valeurNumerique;
    }

    public static TailleAsteroide tailleParValeurNumerique(int valeurNumerique) {
        for (TailleAsteroide taille : TailleAsteroide.values()) {
            if (taille.valeurNumerique == valeurNumerique) {
                return taille;
            }
        }
        return null;
    }
}