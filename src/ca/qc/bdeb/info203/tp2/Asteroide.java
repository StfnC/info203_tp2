package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.SpriteSheet;

public class Asteroide extends Entite {
    @Override
    public void mouvementEntite(Direction direction, int vitesse) {

    }

    public Asteroide(float x, float y, SpriteSheet spriteSheet, int ligne, int colonne) {
        super(x, y, spriteSheet, ligne, colonne);
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {

    }
}
