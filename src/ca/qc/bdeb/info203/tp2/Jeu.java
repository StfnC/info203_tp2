package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.*;

import java.util.ArrayList;

import static ca.qc.bdeb.info203.tp2.dir.*;

public class Jeu extends BasicGame {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 800;
    private static final int vitesseVaisseau = 7;
    private static final int vitesseLaser = 4;
    private static final int vitesseAsteroide = 2;


    private ArrayList<Entite> entiteListe;
    private Image background;

    private Entite vaisseau;
    private Entite laser;

    private boolean vaisseauMoving = false;
    private dir directionVaisseau;

    public static void main(String[] args) throws SlickException {
        AppGameContainer jeu = new AppGameContainer(new Jeu("Jeu"));

        jeu.setDisplayMode(WIDTH, HEIGHT, false);
        jeu.setAlwaysRender(true);
        jeu.setVSync(true);

        jeu.start();
    }

    public Jeu(String title) {
        super(title);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        background = new Image("res/background.png").getScaledCopy(WIDTH, HEIGHT);

        vaisseau = new Vaisseau(0, 0, 96, 96, "res/ship.png");
        vaisseau.setLocation((WIDTH/2 - vaisseau.getWidth()/2), (float) (HEIGHT*0.7));

        entiteListe = new ArrayList<Entite>();
        entiteListe.add(vaisseau);
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        if (vaisseauMoving){
            vaisseau.mouvementEntite(directionVaisseau, delta, vitesseVaisseau);
        }

        for (int i = 1; i < entiteListe.size(); i++) {
            Entite currentEntity = entiteListe.get(i);
            if (currentEntity instanceof Asteroide){
                //currentEntity.mouvementEntite();
            }else if (currentEntity instanceof Laser){
                currentEntity.mouvementEntite(UP, delta, vitesseLaser);
            }
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException {
        background.draw(0, 0);
        for (int i = 0; i < entiteListe.size(); i++) {
            Entite currentEntity = entiteListe.get(i);
            float entityX = currentEntity.getX();
            float entityY = currentEntity.getY();
            Image currEntityImage = currentEntity.getImage();
            currEntityImage.draw(entityX, entityY);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        switch (c){
            case 'w':
                System.out.println(c);
                directionVaisseau = UP;
                vaisseauMoving = true;
                break;
            case 'a':
                System.out.println(c);
                directionVaisseau = dir.LEFT;
                vaisseauMoving = true;
                break;
            case 's':
                System.out.println(c);
                directionVaisseau = dir.DOWN;
                vaisseauMoving = true;
                break;
            case 'd':
                System.out.println(c);
                directionVaisseau = dir.RIGHT;
                vaisseauMoving = true;
                break;
            case ' ':
                float positionX = (vaisseau.getX() + vaisseau.getWidth()/2) - 16;
                float positionY = vaisseau.getY() - 32;
                laser = new Laser(positionX, positionY, 32, 32, "res/laser.png");
                entiteListe.add(laser);
                break;
            case 'e':
                System.out.println("vider");
                break;
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        vaisseauMoving = false;
    }
}

