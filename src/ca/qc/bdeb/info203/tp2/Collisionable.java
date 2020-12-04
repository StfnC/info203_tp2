package ca.qc.bdeb.info203.tp2;

import java.awt.*;

public interface Collisionable {
    void gererCollision(Collisionable objetEnCollision);
    void gererCollision(Collisionable objetEnCollision, Direction directionCollision);

    Rectangle getRectangle();
}
