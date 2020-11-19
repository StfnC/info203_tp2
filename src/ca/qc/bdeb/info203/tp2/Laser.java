package ca.qc.bdeb.info203.tp2;

public class Laser extends Entite{

    @Override
    public void mouvementEntite(dir dir, int delta) {
        y = y - 0.3f * delta;
    }

    public Laser(float x, float y, float width, float height, String imagepath) {
        super(x, y, width, height, imagepath);
    }
}
