package ca.qc.bdeb.info203.tp2;

import java.awt.*;

import static ca.qc.bdeb.info203.tp2.Direction.*;
import static ca.qc.bdeb.info203.tp2.Direction.RIGHT;

public class Bordure implements Collisionable, Mur {
    private Rectangle rectangle;

    public Bordure(int largeurVaisseau, int hauteurVaisseau) {
        // TODO: Rendre la ligne beaucoup plus lisible, si besoin
        // Crée un rectangle qui a l'espace d'un vaisseau entre lui et les bords de la fenêtre
        rectangle = new Rectangle(largeurVaisseau, hauteurVaisseau, Jeu.getWIDTH() - 2 * largeurVaisseau, Jeu.getHEIGHT() - 2 * hauteurVaisseau);
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {
        // TODO: -Maybe changer gererCollision pour qu'on obtienne la direction de la collision directement
        //       -Pour trouver ça on pourrait avoir une classe MoteurCollision qui va s'occuoer de passer à travers une liste de collisionnable et dire si y a une collision
        if (objetEnCollision instanceof Vaisseau) {
            Vaisseau vaisseau = (Vaisseau) objetEnCollision;
            // FIXME: Beaucoup trop de if's faut trouver une façon de rendre ça plus clean
            // FIXME: Fonctionne, mais seulement pour UP et LEFT, car j'account pas pour la taille de l'image en DOWN et RIGHT
            if (directionCollision.equals(UP)) {
                vaisseau.setLocation(vaisseau.getX(), 0f);
            } else if (directionCollision.equals(DOWN)) {
                vaisseau.setLocation(vaisseau.getX(), Jeu.getHEIGHT() - vaisseau.getHeight());
            } else if (directionCollision.equals(LEFT)) {
                vaisseau.setLocation(0f, vaisseau.getY());
            } else if (directionCollision.equals(RIGHT)) {
                vaisseau.setLocation(Jeu.getWIDTH() - vaisseau.getWidth(), vaisseau.getY());
            }
        }

    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }
}
