package ca.qc.bdeb.info203.tp2;

import java.util.ArrayList;

public class MoteurCollision {
    // TODO: À utiliser plus tard

    public void gererCollisions(ArrayList<Collisionable> collisionables) {
        for (Collisionable c1 : collisionables) {
            for (Collisionable c2: collisionables) {
                if (c1 != c2) {
                    // TODO: Donner une interface Mur pour rendre la verification simple
                    if (c1 instanceof Vaisseau) {
                        if (!(c1.getRectangle().intersects(c2.getRectangle()))) {
                            detecterCollisionMur(c1, c2);
                        }
                    } else if (c2 instanceof Vaisseau) {
                        if (!(c1.getRectangle().intersects(c2.getRectangle()))) {
                            detecterCollisionMur(c2, c1);
                        }
                    }
                }
            }
        }
    }

    public void detecterCollisionMur(Collisionable objetEnCollision, Collisionable murs) {
        // FIXME: Très hacky et hardcoded pour le moment, juste pour test, à modifier
        boolean topOut = objetEnCollision.getRectangle().getY() < 0;
        boolean bottomOut = objetEnCollision.getRectangle().getY() + objetEnCollision.getRectangle().getHeight() > Jeu.getHEIGHT();
        boolean leftOut = objetEnCollision.getRectangle().getX() < 0;
        boolean rightOut = objetEnCollision.getRectangle().getX() + objetEnCollision.getRectangle().getWidth() > Jeu.getWIDTH();

        // FIXME: Y a une facon plus clean de faire
        if (topOut) {
            murs.gererCollision(objetEnCollision, Direction.UP);
        } else if (bottomOut) {
            murs.gererCollision(objetEnCollision, Direction.DOWN);
        } else if (leftOut) {
            murs.gererCollision(objetEnCollision, Direction.LEFT);
        } else if (rightOut) {
            murs.gererCollision(objetEnCollision, Direction.RIGHT);
        }
    }
}
