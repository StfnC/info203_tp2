package ca.qc.bdeb.info203.tp2;

import java.awt.*;

/**
 * Interface qui définit la structure des objets qui peuvent entrer en collision
 */
public interface Collisionable {
    /**
     * Gère la collision avec un objet Collisionable
     *
     * @param objetEnCollision Objet avec qui la collision doit être gérée
     */
    void gererCollision(Collisionable objetEnCollision);

    /**
     * Renvoie un Rectangle qui entoure l'objet
     *
     * @return Rectangle qui entoure l'objet
     */
    Rectangle getRectangle();
}
