package ca.qc.bdeb.info203.tp2.Entite;

import ca.qc.bdeb.info203.tp2.*;
import ca.qc.bdeb.info203.tp2.Enum.Direction;
import org.newdawn.slick.*;

import java.util.ArrayList;

/**
 * Classe qui gère l'entité de type Vaisseau
 */
public class Vaisseau extends Entite implements Observable {
    private static final int VITESSE_VAISSEAU = 7;

    private final Sound shieldDown;
    private final Cargo cargo;
    private int lives;
    private boolean vulnerable = true;
    private boolean peutTirer = true;
    private boolean peutSeDeplacer = true;
    private boolean seDeplace = false;
    private Direction direction;
    private final ArrayList<Observateur> observateurs = new ArrayList<>();

    /**
     * Constructeur du vaisseau, on initialise les positions et les images, ainsi que les vies et les sons.
     *
     * @param x         Position x sur l'écran
     * @param y         Position y sur l'écran
     * @param width     Largeur de l'image
     * @param height    Hauteur de l'image
     * @param imagePath Nom du fichier de l'image
     * @throws SlickException
     */
    public Vaisseau(float x, float y, float width, float height, String imagePath) throws SlickException {
        super(x, y, width, height, imagePath);
        this.cargo = new Cargo(this);
        lives = 3;
        shieldDown = new Sound("res/Sounds/sfx_shieldDown.wav");
    }

    /**
     * Méthode responsable du déplacement du vaisseau selon la direction
     */
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

    /**
     * On enlève une vie grâce à cette méthode et on joue le son associé.
     */
    public void enleverVie() {
        if (lives > 1) {
            shieldDown.play();
        }
        lives--;
    }

    /**
     * On gère la collision différemment selon le type d'entité avec lequel on collisionne.
     *
     * @param objetEnCollision L'objet avec lequel on collisionne
     */
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

    /**
     * Méthode pour retourner les vies du vaisseau.
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Vérifier si le vaisseau est vulnérable
     */
    public boolean getVulnerable() {
        return this.vulnerable;
    }

    /**
     * Setter pour le boolean Vulnerable
     */
    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }


    /**
     * Ajout simple de l'observateur à l'entité
     */
    @Override
    public void addObservateur(Observateur observateur) {
        this.observateurs.add(observateur);
    }

    /**
     * On gère la liste des observateurs et mettons à jour les temps de chaque entité
     */
    @Override
    public void updateObservateurs() {
        for (Observateur observateur : observateurs) {
            observateur.update(System.currentTimeMillis());
        }
    }

    /**
     * Setter pour le boolean seDeplace
     */
    public void setSeDeplace(boolean seDeplace) {
        this.seDeplace = seDeplace;
    }

    /**
     * Setter pour la direction du vaisseau
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Setter pour le boolean peutSeDeplacer
     */
    public void setPeutSeDeplacer(boolean peutSeDeplacer) {
        this.peutSeDeplacer = peutSeDeplacer;
    }

    /**
     * Getter pour le boolean peutTirer
     */
    public boolean getPeutTirer() {
        return peutTirer;
    }

    /**
     * Setter pour le boolean peutTirer
     */
    public void setPeutTirer(boolean peutTirer) {
        this.peutTirer = peutTirer;
    }

    /**
     * Getter pour la cargaison de ce vaisseau spécifique
     */
    public Cargo getCargo() {
        return cargo;
    }
}
