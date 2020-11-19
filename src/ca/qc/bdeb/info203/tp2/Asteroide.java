package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.SpriteSheet;

public class Asteroide extends Entite{
    @Override
    public void mouvementEntite(dir dir, int delta) {

    }

    public Asteroide(float x, float y, SpriteSheet spriteSheet, int ligne, int colonne) {
        super(x, y, spriteSheet, ligne, colonne);
    }
}
