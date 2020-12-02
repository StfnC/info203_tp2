package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.ArrayList;
import java.util.Random;

public class Laser extends Entite {
    private static final int VITESSE_LASER = 5;

    private float initialY;
    private ArrayList<Sound> sonLaser = new ArrayList<>();
    private Sound effect1;
    private Sound effect2;

    @Override
    public void mouvementEntite(Direction direction, int delta) {
        if (y >= (initialY - Jeu.getHEIGHT() / 2)) {
            y -= Jeu.getScalingVitesse() * VITESSE_LASER * delta;
        } else {
            detruire = true;
        }
    }

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

    private void playSFX() {
        Random random = new Random();
        sonLaser.get(random.nextInt(sonLaser.size())).play();
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {

    }
}
