package ca.qc.bdeb.info203.tp2.Entite;

import ca.qc.bdeb.info203.tp2.Collisionable;
import ca.qc.bdeb.info203.tp2.Direction;
import ca.qc.bdeb.info203.tp2.Jeu;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Asteroide extends Entite {
    private static final double VITESSE_ASTEROIDE = 0.5;

    private final SpriteSheet spriteAsteroides = new SpriteSheet("res/SpriteAsteroide.png", 16, 16);
    private final Image ast0;
    private final Image ast1;
    private final Image ast2;
    private final Image ast3;
    private final Image ast4;

    private boolean isSeparer;

    public Asteroide(float x, float y, int index) throws SlickException {
        super(x, y, index);

        ast0 = spriteAsteroides.getSubImage(0, 0, 256, 256);
        ast1 = spriteAsteroides.getSubImage(256, 0, 128, 128);
        ast2 = spriteAsteroides.getSubImage(256, 128, 64, 64);
        ast3 = spriteAsteroides.getSubImage(320, 128, 32, 32);
        ast4 = spriteAsteroides.getSubImage(352, 128, 16, 16);

        setImageAsteroide();
    }

    private void setImageAsteroide() {
        switch (index) {
            case 0:
                image = ast0;
                break;
            case 1:
                image = ast1;
                break;
            case 2:
                image = ast2;
                break;
            case 3:
                image = ast3;
                break;
            case 4:
                image = ast4;
                break;
            default:
                this.detruire = true;
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void mouvementEntite(Direction direction, int delta) {
        y += Jeu.getScalingVitesse() * VITESSE_ASTEROIDE * delta;
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision) {
        if (objetEnCollision instanceof Vaisseau) {
            Vaisseau vaisseau = (Vaisseau) objetEnCollision;
            if (vaisseau.getHeight() > this.getHeight()) {
                this.detruire = true;
            }
        } else if (objetEnCollision instanceof Laser) {
            this.detruire = true;

            this.isSeparer = true;
        }
    }

    @Override
    public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {

    }

    public boolean isSeparer() {
        return isSeparer;
    }
}
