package ca.qc.bdeb.info203.tp2.Entite;

import ca.qc.bdeb.info203.tp2.Collisionable;
import ca.qc.bdeb.info203.tp2.Deplacable;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.awt.*;

//TODO: Ajouter interface PeutCollisionner

public abstract class Entite implements Deplacable, Collisionable {
    public int index;
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

    public Entite(float x, float y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
    }

    // Permet de déplacer l'entité
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Retourne le rectange qui englobe l'entité
    public Rectangle getRectangle() {
        return new Rectangle((int) x, (int) y, (int) width, (int) height);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Image getImage() {
        return image;
    }

    public boolean isDetruire() {
        return detruire;
    }
}
