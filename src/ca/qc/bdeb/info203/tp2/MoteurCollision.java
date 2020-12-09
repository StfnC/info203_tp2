package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Vaisseau;
import ca.qc.bdeb.info203.tp2.Enum.Direction;

import java.util.ArrayList;

public class MoteurCollision {
    // TODO: À utiliser plus tard

    public void detecterCollisions(ArrayList<Collisionable> collisionables) {
        for (Collisionable c1 : collisionables) {
            for (Collisionable c2 : collisionables) {
                if (c1 != c2) {
                    // TODO: Trouver une façon de rendre ce bloc plus clean
                    // Cas spécial où le vaisseau pourrait sortir de la fenêtre
                    if ((c1 instanceof Mur || c2 instanceof Mur) && (c1 instanceof Vaisseau || c2 instanceof Vaisseau)) {
                        if (c1 instanceof Vaisseau) {
                            if (!(c1.getRectangle().intersects(c2.getRectangle()))) {
                                detecterCollisionMur(c1, c2);
                            }
                        } else if (c2 instanceof Vaisseau) {
                            if (!(c1.getRectangle().intersects(c2.getRectangle()))) {
                                detecterCollisionMur(c2, c1);
                            }
                        }
                    } else if (!(c1 instanceof Mur) && !(c2 instanceof Mur)) {
                        // On délègue aux objets collisionables la façon dont ils veulent gérer la collision
                        if (c1.getRectangle().intersects(c2.getRectangle())) {
                            c1.gererCollision(c2);
                        }
                    }
                }
            }
        }
    }

    public void detecterCollisionMur(Collisionable objetEnCollision, Collisionable mur) {
        // FIXME: Très hacky et hardcoded pour le moment, juste pour test, à modifier
        boolean topOut = objetEnCollision.getRectangle().getY() < 0;
        boolean bottomOut = objetEnCollision.getRectangle().getY() + objetEnCollision.getRectangle().getHeight() > Main.HEIGHT;
        boolean leftOut = objetEnCollision.getRectangle().getX() < 0;
        boolean rightOut = objetEnCollision.getRectangle().getX() + objetEnCollision.getRectangle().getWidth() > Main.WIDTH;

        // FIXME: Y a une facon plus clean de faire
        if (topOut) {
            mur.gererCollision(objetEnCollision, Direction.UP);
        } else if (bottomOut) {
            mur.gererCollision(objetEnCollision, Direction.DOWN);
        } else if (leftOut) {
            mur.gererCollision(objetEnCollision, Direction.LEFT);
        } else if (rightOut) {
            mur.gererCollision(objetEnCollision, Direction.RIGHT);
        }
    }
}
