package ca.qc.bdeb.info203.tp2.entites;

import ca.qc.bdeb.info203.tp2.interfaces.Collisionable;
import ca.qc.bdeb.info203.tp2.Jeu;
import ca.qc.bdeb.info203.tp2.Main;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe qui gère l'entité de type Laser
 */
public class Laser extends Entite {
    private static final int VITESSE_LASER = 5;

    private final float initialY;
    private final ArrayList<Sound> sonLaser = new ArrayList<>();
    private Sound effect1;
    private Sound effect2;

    /**
     * Constructeur du laser, on initialise les positions et les images, ainsi que les sons.
     *
     * @param x         Position x sur l'écran
     * @param y         Position y sur l'écran
     * @param width     Largeur de l'image
     * @param height    Hauteur de l'image
     * @param imagepath Nom du fichier de l'image
     */
    public Laser(float x, float y, float width, float height, String imagepath) {
        super(x, y, width, height, imagepath);
        initialY = y;

        try {
            effect1 = new Sound("res/Sounds/sfx_laser1.wav");
            effect2 = new Sound("res/Sounds/sfx_laser2.wav");
        } catch (SlickException e) {
            e.printStackTrace();
        }

        sonLaser.add(effect1);
        sonLaser.add(effect2);

        playSFX();
    }

    /**
     * Méthode responsable du déplacement du laser et qui s'assure que le laser ne va pas trop loin
     */
    @Override
    public void deplacer(int delta) {
        if (y >= (initialY - Main.HEIGHT / 2)) {
            y -= Jeu.getScalingVitesse() * VITESSE_LASER * delta;
        } else {
            detruire = true;
        }
    }

    /**
     * Méthode qui gère le son du laser quand il est tiré
     */
    private void playSFX() {
        Random random = new Random();
        sonLaser.get(random.nextInt(sonLaser.size())).play();
    }


    /**
     * Méthode qui va gérer la collision avec un asteroide
     *
     * @param objetEnCollision L'objet avec lequel on collisionne
     */
    @Override
    public void gererCollision(Collisionable objetEnCollision) {
        if (objetEnCollision instanceof Asteroide) {
            detruire = true;
        }
    }
}
