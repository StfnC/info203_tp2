package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Vaisseau;

import java.awt.*;

/**
 * Classe qui représente la bordure qu'un Vaisseau ne peut pas dépasser
 */
public class Bordure implements Collisionable, Mur {
    private final Rectangle RECTANGLE;

    /**
     * Construit une Bordure adaptée à la taille du Vaisseau
     *
     * @param largeurVaisseau Largeur du Vaisseau
     * @param hauteurVaisseau Hauteur du Vaisseau
     */
    public Bordure(int largeurVaisseau, int hauteurVaisseau) {
        // Crée un rectangle qui a l'espace d'un vaisseau entre lui et les bords de la fenêtre
        RECTANGLE = new Rectangle(largeurVaisseau, hauteurVaisseau, Main.WIDTH - 2 * largeurVaisseau, Main.HEIGHT - 2 * hauteurVaisseau);
    }

    /**
     * Gère la collision avec un objet
     *
     * @param objetEnCollision Objet avec qui la collision doit être gérée
     */
    @Override
    public void gererCollision(Collisionable objetEnCollision) {
        if (objetEnCollision instanceof Vaisseau) {
            // On regarde quel côté de la bordure le Vaisseau a dépassé
            boolean topOut = objetEnCollision.getRectangle().getY() < 0;
            boolean bottomOut = objetEnCollision.getRectangle().getY() + objetEnCollision.getRectangle().getHeight() > Main.HEIGHT;
            boolean leftOut = objetEnCollision.getRectangle().getX() < 0;
            boolean rightOut = objetEnCollision.getRectangle().getX() + objetEnCollision.getRectangle().getWidth() > Main.WIDTH;

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

    /**
     * Renvoie le Rectangle qui représente l'aire que le Vaisseau ne peut pas quitter
     *
     * @return Rectangle qui représente l'aire que le Vaisseau ne peut pas quitter
     */
    @Override
    public Rectangle getRectangle() {
        return RECTANGLE;
    }
}
