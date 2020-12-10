package ca.qc.bdeb.info203.tp2.entites;

import ca.qc.bdeb.info203.tp2.interfaces.Collisionable;
import ca.qc.bdeb.info203.tp2.interfaces.Deplacable;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.awt.*;

/**
 * Classe mère qui va gérer les entités
 */
public abstract class Entite implements Deplacable, Collisionable {
    protected float x, y, width, height;
    protected Image image;
    protected boolean detruire = false;

    /**
     * Constructeur d'Entite avec image sur le disque
     *
     * @param x         position de l'entité dans l'écran - x
     * @param y         position de l'entité dans l'écran - y
     * @param width     largeur de l'image
     * @param height    hauteur de l'image
     * @param imagePath chemin d'accès de l'image sur le disque
     */
    public Entite(float x, float y, float width, float height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            this.image = new Image(imagePath);
        } catch (SlickException e) {
            System.out.println("Image introuvable: " + getClass());
        }
    }

    /**
     * Constructeur qui n'inclut que la position de l'entité
     *
     * @param x Position x sur l'écran
     * @param y Position y sur l'écran
     */
    public Entite(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Méthode setter pour la position de l'entité
     *
     * @param x Position x sur l'écran
     * @param y Position y sur l'écran
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Getter pour le rectangle autour de l'entité
     */
    public Rectangle getRectangle() {
        return new Rectangle((int) x, (int) y, (int) width, (int) height);
    }

    /**
     * Getter pour la position x
     */
    public float getX() {
        return x;
    }

    /**
     * Getter pour la position y
     */
    public float getY() {
        return y;
    }

    /**
     * Getter pour la largeur
     */
    public float getWidth() {
        return width;
    }

    /**
     * Getter pour la hauteur
     */
    public float getHeight() {
        return height;
    }

    /**
     * Getter pour l'image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Getter pour le boolean détruire
     */
    public boolean isDetruire() {
        return detruire;
    }

    /**
     * Setter pour le boolean détruire
     */
    public void setDetruire(boolean detruire) {
        this.detruire = detruire;
    }
}
