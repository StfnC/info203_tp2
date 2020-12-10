package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Vaisseau;
import ca.qc.bdeb.info203.tp2.Enum.Direction;

import java.awt.*;

public class Bordure implements Collisionable, Mur {
    private final Rectangle RECTANGLE;

    public Bordure(int largeurVaisseau, int hauteurVaisseau) {
        // Crée un rectangle qui a l'espace d'un vaisseau entre lui et les bords de la fenêtre
        RECTANGLE = new Rectangle(largeurVaisseau, hauteurVaisseau, Main.WIDTH - 2 * largeurVaisseau, Main.HEIGHT - 2 * hauteurVaisseau);
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision) {
        boolean topOut = objetEnCollision.getRectangle().getY() < 0;
        boolean bottomOut = objetEnCollision.getRectangle().getY() + objetEnCollision.getRectangle().getHeight() > Main.HEIGHT;
        boolean leftOut = objetEnCollision.getRectangle().getX() < 0;
        boolean rightOut = objetEnCollision.getRectangle().getX() + objetEnCollision.getRectangle().getWidth() > Main.WIDTH;

        if (objetEnCollision instanceof Vaisseau) {
            Vaisseau vaisseau = (Vaisseau) objetEnCollision;

            if (topOut) {
                vaisseau.setLocation(vaisseau.getX(), 0f);
            } else if (bottomOut) {
                vaisseau.setLocation(vaisseau.getX(), Main.HEIGHT - vaisseau.getHeight());
            } else if (leftOut) {
                vaisseau.setLocation(0f, vaisseau.getY());
            } else if (rightOut) {
                vaisseau.setLocation(Main.WIDTH - vaisseau.getWidth(), vaisseau.getY());
            }
        }
    }

    @Override
    public Rectangle getRectangle() {
        return RECTANGLE;
    }
}
