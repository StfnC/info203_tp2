package ca.qc.bdeb.info203.tp2.Entite;

import ca.qc.bdeb.info203.tp2.Collisionable;
import ca.qc.bdeb.info203.tp2.Direction;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Asteroide extends Entite {
    private static final int VITESSE_ASTEROIDE = 3;

    private Image ast0;
    private Image ast1;
    private Image ast2;
    private Image ast3;
    private Image ast4;

    public Asteroide(float x, float y, SpriteSheet spriteAsteroides, int ligne, int colonne) {
        super(x, y, spriteAsteroides, ligne, colonne);

        ast0 = spriteAsteroides.getSubImage(0, 0, 256,256);
        ast1 = spriteAsteroides.getSubImage(256, 0, 128, 128);
        ast2 = spriteAsteroides.getSubImage(256, 128, 64, 64);
        ast3 = spriteAsteroides.getSubImage(320, 128, 32, 32);
        ast4 = spriteAsteroides.getSubImage(352, 128, 16, 16);

        image = ast0;
        // FIXME: On devrait pas avoir à faire ça ici aussi, il se fait déjà dans le constructor d'entité
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void mouvementEntite(Direction direction, int delta) {

    }

    @Override
    public void gererCollision(Collisionable objetEnCollision) {
        if (objetEnCollision instanceof Vaisseau) {
            Vaisseau vaisseau = (Vaisseau) objetEnCollision;
            if (vaisseau.getHeight() > this.getHeight()) {
                this.detruire = true;
            }
        } else if (objetEnCollision instanceof Laser) {
            this.separerAsteroid();
        }
    }

    private void separerAsteroid() {
        this.detruire = true;
        System.out.println("Asteroide doit split");
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {

    }
}
