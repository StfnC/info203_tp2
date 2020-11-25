package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.*;

public class Vaisseau extends Entite {

    @Override
    public void mouvementEntite(Direction direction, int delta, int vitesse) {
        switch (direction){
            case UP:
                y = y - 0.1f * vitesse * delta;
                break;
            case LEFT:
                x = x - 0.1f * vitesse * delta;
                break;
            case DOWN:
                y = y + 0.1f * vitesse * delta;
                break;
            case RIGHT:
                x = x + 0.1f * vitesse * delta;
                break;
        }
    }

    public Vaisseau(float x, float y, float width, float height, String imagepath) throws SlickException {
        super(x, y, width, height, imagepath);
    }
}
