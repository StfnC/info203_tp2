package ca.qc.bdeb.info203.tp2.Entite;

import ca.qc.bdeb.info203.tp2.*;
import org.lwjgl.Sys;
import org.newdawn.slick.*;

import java.util.ArrayList;

public class Vaisseau extends Entite implements Observable {
    private static final int VITESSE_VAISSEAU = 7;

    private int lives;
    private boolean vulnerable = true;
    private ArrayList<Observateur> observateurs = new ArrayList<>();

    public Vaisseau(float x, float y, float width, float height, String imagepath) throws SlickException {
        super(x, y, width, height, imagepath);
        lives = 3;
    }

    @Override
    public void mouvementEntite(Direction direction, int delta) {
        switch (direction) {
            case UP:
                y -= Jeu.getScalingVitesse() * VITESSE_VAISSEAU * delta;
                break;
            case LEFT:
                x -= Jeu.getScalingVitesse() * VITESSE_VAISSEAU * delta;
                break;
            case DOWN:
                y += Jeu.getScalingVitesse() * VITESSE_VAISSEAU * delta;
                break;
            case RIGHT:
                x += Jeu.getScalingVitesse() * VITESSE_VAISSEAU * delta;
                break;
        }
    }

    public void enleverVie(){
        lives--;
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision) {
        if (objetEnCollision instanceof Asteroide) {
            Asteroide asteroide = (Asteroide) objetEnCollision;
            this.updateObservateurs();
            if ((asteroide.getHeight() >= this.getHeight()) && this.vulnerable) {
                this.enleverVie();
                this.vulnerable = false;
            }
        }
        System.out.println(lives);
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {

    }

    public int getLives() {
        return this.lives;
    }

    public boolean getVulnerable() {
        return this.vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    @Override
    public void addObservateur(Observateur observateur) {
        this.observateurs.add(observateur);
    }

    @Override
    public void updateObservateurs() {
        for (Observateur observateur : observateurs) {
            observateur.update(System.currentTimeMillis());
        }
    }
}
