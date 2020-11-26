package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.*;

public class Vaisseau extends Entite {
    private static final int VITESSE_VAISSEAU = 7;

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

    public Vaisseau(float x, float y, float width, float height, String imagepath) throws SlickException {
        super(x, y, width, height, imagepath);
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {

    }
}
