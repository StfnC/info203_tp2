package ca.qc.bdeb.info203.tp2;

import java.util.ArrayList;

public class MoteurCollision {
    // TODO: À utiliser plus tard
    private ArrayList<Collisionable> collisionables = new ArrayList<>();


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
