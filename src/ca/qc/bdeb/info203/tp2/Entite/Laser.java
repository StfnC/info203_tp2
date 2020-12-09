package ca.qc.bdeb.info203.tp2.Entite;

import ca.qc.bdeb.info203.tp2.Collisionable;
import ca.qc.bdeb.info203.tp2.Direction;
import ca.qc.bdeb.info203.tp2.Jeu;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.ArrayList;
import java.util.Random;

public class Laser extends Entite {
    private static final int VITESSE_LASER = 5;

    private final float initialY;
    private final ArrayList<Sound> sonLaser = new ArrayList<>();
    private Sound effect1;
    private Sound effect2;

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

    @Override
    public void mouvementEntite(Direction direction, int delta) {
        if (y >= (initialY - Jeu.getHEIGHT() / 2)) {
            y -= Jeu.getScalingVitesse() * VITESSE_LASER * delta;
        } else {
            detruire = true;
        }
    }

    private void playSFX() {
        Random random = new Random();
        sonLaser.get(random.nextInt(sonLaser.size())).play();
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision) {
        if (objetEnCollision instanceof Asteroide) {
            detruire = true;
        }
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {

    }
}
