package ca.qc.bdeb.info203.tp2;

import java.awt.*;

public interface Collisionable {
    void gererCollision(Collisionable objetEnCollision);

    Rectangle getRectangle();
}
