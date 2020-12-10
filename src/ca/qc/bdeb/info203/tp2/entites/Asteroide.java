package ca.qc.bdeb.info203.tp2.entites;

import ca.qc.bdeb.info203.tp2.interfaces.Collisionable;
import ca.qc.bdeb.info203.tp2.enums.Direction;
import ca.qc.bdeb.info203.tp2.enums.TailleAsteroide;
import ca.qc.bdeb.info203.tp2.Jeu;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;

/**
 * Classe qui gère les entités de type asteroide
 */
public class Asteroide extends Entite {
    private static final float VITESSE_ASTEROIDE = 0.5f;

    private final SpriteSheet SPRITE_ASTEROIDES = new SpriteSheet("res/SpriteAsteroide.png", 16, 16);
    private final Image AST_TRES_GRAND;
    private final Image AST_GRAND;
    private final Image AST_MOYEN;
    private final Image AST_PETIT;
    private final Image AST_TRES_PETIT;

    private final TailleAsteroide tailleAsteroide;
    private Direction direction;
    private boolean isSeparer;

    private final Sound sonAsteroideBroye;

    /**
     * Constructeur de chaque asteroide créé
     *
     * @param x               Position x sur l'écran
     * @param y               Position y sur l'écran
     * @param tailleAsteroide La taille de l'asteroide
     * @param direction       La direction de son mouvement
     * @throws SlickException
     */
    public Asteroide(float x, float y, TailleAsteroide tailleAsteroide, Direction direction) throws SlickException {
        super(x, y);

        this.tailleAsteroide = tailleAsteroide;

        AST_TRES_GRAND = SPRITE_ASTEROIDES.getSubImage(0, 0, 256, 256);
        AST_GRAND = SPRITE_ASTEROIDES.getSubImage(256, 0, 128, 128);
        AST_MOYEN = SPRITE_ASTEROIDES.getSubImage(256, 128, 64, 64);
        AST_PETIT = SPRITE_ASTEROIDES.getSubImage(320, 128, 32, 32);
        AST_TRES_PETIT = SPRITE_ASTEROIDES.getSubImage(352, 128, 16, 16);

        setImageAsteroide();

        sonAsteroideBroye = new Sound("res/Sounds/sfx_shieldUp.wav");

        this.direction = direction;
    }

    /**
     * Après avoir choisi la taille de l'astéroïde on associe la bonne image à l'astéroïde.
     */
    private void setImageAsteroide() {
        switch (tailleAsteroide) {
            case TRES_GRAND:
                image = AST_TRES_GRAND;
                break;
            case GRAND:
                image = AST_GRAND;
                break;
            case MOYEN:
                image = AST_MOYEN;
                break;
            case PETIT:
                image = AST_PETIT;
                break;
            case TRES_PETIT:
                image = AST_TRES_PETIT;
                break;
            default:
                this.detruire = true;
                break;
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    /**
     * Méthode qui va effectuer le déplacement des asteroides tout dépendant de la direction.
     */
    @Override
    public void deplacer(int delta) {
        switch (direction) {
            case DOWN:
                y += Jeu.getScalingVitesse() * VITESSE_ASTEROIDE * delta;
                break;
            case RIGHT:
                x += Jeu.getScalingVitesse() * VITESSE_ASTEROIDE * delta;
                break;
            case LEFT:
                x -= Jeu.getScalingVitesse() * VITESSE_ASTEROIDE * delta;
                break;
        }
    }

    /**
     * On gère la collision différemment selon le type d'entité avec lequel on collisionne.
     *
     * @param objetEnCollision L'objet avec lequel il y a collision
     */
    @Override
    public void gererCollision(Collisionable objetEnCollision) {
        if (objetEnCollision instanceof Vaisseau) {
            Vaisseau vaisseau = (Vaisseau) objetEnCollision;
            if (vaisseau.getHeight() > this.getHeight()) {
                this.detruire = true;
                sonAsteroideBroye.play();
            }
        } else if (objetEnCollision instanceof Laser) {
            this.detruire = true;

            this.isSeparer = true;
        }
    }

    /**
     * Méthode pour verifier si l'astéroïde doit se séparer
     */
    public boolean isSeparer() {
        return isSeparer;
    }

    /**
     * Méthode getter pour la direction de l'astéroïde
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Méthode setter pour la direction de l'astéroïde
     *
     * @param direction Nouvelle direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Méthode getter pour la taille de l'astéroïde
     */
    public TailleAsteroide getTailleAsteroide() {
        return tailleAsteroide;
    }
}
