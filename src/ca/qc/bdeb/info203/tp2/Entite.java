package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import java.awt.*;

//TODO: Ajouter interface Movable ou méthodes Movables

public abstract class Entite {
    protected float x, y, width, height;
    protected Image image;
    protected boolean detruire = false;

    public abstract void mouvementEntite(dir dir, int delta);

    /**
     * Constructeur d'Entite avec image sur le disque
     * @param x position de l'entité dans l'écran - x
     * @param y position de l'entité dans l'écran - y
     * @param width largeur de l'image
     * @param height hauteur de l'image
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
     * Constructeur d'Entite #2 - Avec SpriteSheet
     * @param x position de l'entité dans l'écran - x
     * @param y position de l'entité dans l'écran - y
     * @param spriteSheet SpriteSheet qui contient l'image
     * @param ligne la ligne dans le SpriteSheet de l'image
     * @param colonne la colonne dans le SpriteSheet de l'image
     */
    public Entite(float x, float y, SpriteSheet spriteSheet, int ligne, int colonne) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // FIXME: Devrait être colonne ligne?
        this.image = spriteSheet.getSubImage(ligne, colonne);
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
