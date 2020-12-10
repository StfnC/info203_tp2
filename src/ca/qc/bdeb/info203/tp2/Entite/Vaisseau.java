package ca.qc.bdeb.info203.tp2.Entite;

import ca.qc.bdeb.info203.tp2.*;
import ca.qc.bdeb.info203.tp2.Enum.Direction;
import org.newdawn.slick.*;

import java.util.ArrayList;

public class Vaisseau extends Entite implements Observable {
    private static final int VITESSE_VAISSEAU = 7;

    private Sound shieldDown;
    private Cargo cargo;
    private int lives;
    private boolean vulnerable = true;
    private boolean peutTirer = true;
    private boolean peutSeDeplacer = true;
    private boolean seDeplace = false;
    private Direction direction;
    private ArrayList<Observateur> observateurs = new ArrayList<>();

    public Vaisseau(float x, float y, float width, float height, String imagePath) throws SlickException {
        super(x, y, width, height, imagePath);
        this.cargo = new Cargo(this);
        lives = 3;
        shieldDown = new Sound("res/Sounds/sfx_shieldDown.wav");
    }

    @Override
    public void deplacer(int delta) {
        if (seDeplace && peutSeDeplacer) {
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
    }

    public void enleverVie() {
        if (lives > 1) {
            shieldDown.play();
        }
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
            } else if (asteroide.getHeight() < this.getHeight()) {
                this.cargo.addCargaisonVaisseau(asteroide);
            }
        }
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

    public boolean getSeDeplace() {
        return seDeplace;
    }

    public void setSeDeplace(boolean seDeplace) {
        this.seDeplace = seDeplace;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean getPeutSeDeplacer() {
        return peutSeDeplacer;
    }

    public void setPeutSeDeplacer(boolean peutSeDeplacer) {
        this.peutSeDeplacer = peutSeDeplacer;
    }

    public boolean getPeutTirer() {
        return peutTirer;
    }

    public void setPeutTirer(boolean peutTirer) {
        this.peutTirer = peutTirer;
    }

    public Cargo getCargo() {
        return cargo;
    }
}
