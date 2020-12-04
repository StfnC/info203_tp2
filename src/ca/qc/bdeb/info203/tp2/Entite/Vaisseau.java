package ca.qc.bdeb.info203.tp2.Entite;

import ca.qc.bdeb.info203.tp2.Collisionable;
import ca.qc.bdeb.info203.tp2.Direction;
import ca.qc.bdeb.info203.tp2.Jeu;
import org.newdawn.slick.*;

public class Vaisseau extends Entite {
    private static final int VITESSE_VAISSEAU = 7;

    private int lives;
    private boolean vulnerable = true;

    public Vaisseau(float x, float y, float width, float height, String imagepath) throws SlickException {
        super(x, y, width, height, imagepath);
        lives = 3;
    }

    @Override
    public void mouvementEntite(Direction direction, int delta) {
        switch (direction) {
            case UP:
                y -= Jeu.getScalingVitesse() * VITESSE_VAISSEAU * delta;
                break;
            case LEFT:
                x -= Jeu.getScalingVitesse() * VITESSE_VAISSEAU * delta;
                break;
            case DOWN:
                y += Jeu.getScalingVitesse() * VITESSE_VAISSEAU * delta;
                break;
            case RIGHT:
                x += Jeu.getScalingVitesse() * VITESSE_VAISSEAU * delta;
                break;
        }
    }

    public void enleverVie(){
        lives--;
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision) {
        if (objetEnCollision instanceof Asteroide) {
            Asteroide asteroide = (Asteroide) objetEnCollision;
            if ((asteroide.getHeight() >= this.getHeight()) && vulnerable) {
                this.enleverVie();
            }
        }

    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {

    }

    public int getLives() {
        return this.lives;
    }
}
