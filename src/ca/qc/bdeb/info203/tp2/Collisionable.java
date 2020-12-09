package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Enum.Direction;

import java.awt.*;

public interface Collisionable {
    void gererCollision(Collisionable objetEnCollision);

    void gererCollision(Collisionable objetEnCollision, Direction directionCollision);

    Rectangle getRectangle();
}
