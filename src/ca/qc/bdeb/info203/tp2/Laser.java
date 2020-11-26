package ca.qc.bdeb.info203.tp2;

public class Laser extends Entite {
    private static final int VITESSE_LASER = 5;

    private float initialY;

    @Override
    public void mouvementEntite(Direction direction, int delta) {
        if (y >= (initialY - Jeu.getHEIGHT() / 2)) {
            y -= Jeu.getScalingVitesse() * VITESSE_LASER * delta;
        } else {
            detruire = true;
        }

    }

    public Laser(float x, float y, float width, float height, String imagepath) {
        super(x, y, width, height, imagepath);
        initialY = y;
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {

    }
}
