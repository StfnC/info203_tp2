package ca.qc.bdeb.info203.tp2;

import com.sun.deploy.net.MessageHeader;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.ArrayList;
import java.util.Random;

public class Laser extends Entite {
    private static final int VITESSE_LASER = 5;

    private float initialY;
    private ArrayList<Sound> sonLaser = new ArrayList<>();

    @Override
    public void mouvementEntite(Direction direction, int delta) {
        if (y >= (initialY - Jeu.getHEIGHT() / 2)) {
            y -= Jeu.getScalingVitesse() * VITESSE_LASER * delta;
        } else {
            detruire = true;
        }

    }

    public Laser(float x, float y, float width, float height, String imagepath) throws SlickException {
        super(x, y, width, height, imagepath);
        initialY = y;

        Sound effect1 = new Sound("res/sfx_laser1.wav");
        Sound effect2 = new Sound("res/sfx_laser2.wav");
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
