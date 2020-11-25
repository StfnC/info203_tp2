package ca.qc.bdeb.info203.tp2;

public class Laser extends Entite {

    private float initialY;

    @Override
    public void mouvementEntite(Direction direction, int vitesse) {
        if (y >= (initialY - Jeu.getHEIGHT() / 2)) {
            y = y - 0.1f * vitesse;
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
