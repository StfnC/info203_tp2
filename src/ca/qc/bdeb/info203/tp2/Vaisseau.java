package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.*;

public class Vaisseau extends Entite {

    @Override
    public void mouvementEntite(dir dir, int delta) {
        switch (dir){
            case UP:
                y = y - 0.7f * delta;
                break;
            case LEFT:
                x = x - 0.7f * delta;
                break;
            case DOWN:
                y = y + 0.7f * delta;
                break;
            case RIGHT:
                x = x + 0.7f * delta;
                break;
        }
    }

    public Vaisseau(float x, float y, float width, float height, String imagepath) throws SlickException {
        super(x, y, width, height, imagepath);
    }
}
