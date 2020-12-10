package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Vaisseau;

import java.util.ArrayList;

public class MoteurCollision {
    public void detecterCollisions(ArrayList<Collisionable> collisionables) {
        for (Collisionable c1 : collisionables) {
            for (Collisionable c2 : collisionables) {
                if (c1 != c2) {
                    // Cas spécial où le vaisseau pourrait sortir de la fenêtre
                    if (c1 instanceof Mur && c2 instanceof Vaisseau) {
                        if (!(c1.getRectangle().intersects(c2.getRectangle()))) {
                            c1.gererCollision(c2);
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
}
