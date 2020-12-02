package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.*;
import org.newdawn.slick.*;

import java.util.ArrayList;

import static ca.qc.bdeb.info203.tp2.Direction.*;

public class Jeu extends BasicGame {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 800;
    private static final float SCALING_VITESSE = 0.1f;
    private static final String gameTitle = "SS Temp";

    private ArrayList<Entite> entiteListe = new ArrayList<>();
    private ArrayList<Collisionable> collisionables = new ArrayList<>();

    private Image background;
    private SpriteSheet spriteAsteroides;

    private Vaisseau vaisseau;
    private Laser laser;
    private Asteroide asteroide;

    private GameContainer gc;

    private boolean vaisseauMoving = false;
    private Direction directionVaisseau;

    private MoteurCollision moteurCollision;

    private Collisionable bordures;

    public static void main(String[] args) throws SlickException {
        AppGameContainer jeu = new AppGameContainer(new Jeu(gameTitle));

        jeu.setDisplayMode(WIDTH, HEIGHT, false);
        jeu.setIcon("res/icon.png");
        jeu.setAlwaysRender(true);
        jeu.setShowFPS(false);

        jeu.setTargetFrameRate(60);
        jeu.setVSync(true);

        jeu.start();
    }

    public Jeu(String title) {
        super(title);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        this.gc = gameContainer;
        this.moteurCollision = new MoteurCollision();

        background = new Image("res/background.png").getScaledCopy(WIDTH, HEIGHT);

        spriteAsteroides = new SpriteSheet("res/SpriteAsteroide.png", 16, 16);
        asteroide = new Asteroide(0, 0, spriteAsteroides, 0, 0);

        vaisseau = new Vaisseau(0, 0, 128, 128, "res/ship.png");
        vaisseau.setLocation((WIDTH / 2 - vaisseau.getWidth() / 2), (float) (HEIGHT * 0.7));

        bordures = new Bordure((int) vaisseau.getWidth(), (int) vaisseau.getHeight());

        entiteListe.add(vaisseau);

        collisionables.add(bordures);
        collisionables.add(vaisseau);
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        ArrayList<Entite> listeTemp = new ArrayList<>();
        moteurCollision.detecterCollisions(collisionables);

        //TODO: Generation des asteroides

        for (Entite currentEntity : entiteListe) {
            boolean destruction = currentEntity.isDetruire();

            // TODO: -Refactor cette partie de code pour prendre avantage des interfaces
            //       -On devrait pouvoir seulement appeler currentEntity.mouvementEntite sans utiliser instanceof
            if (currentEntity instanceof Vaisseau && vaisseauMoving) {
                currentEntity.mouvementEntite(directionVaisseau, delta);
            } else if (currentEntity instanceof Asteroide) {
                currentEntity.mouvementEntite(DOWN, delta);
            } else if (currentEntity instanceof Laser) {
                if (destruction) {
                    listeTemp.add(currentEntity);
                } else {
                    currentEntity.mouvementEntite(UP, delta);
                }
            }
        }
        entiteListe.removeAll(listeTemp);
        listeTemp.clear();
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException {
        background.draw(0, 0);

        for (Entite currentEntity : entiteListe) {
            float entityX = currentEntity.getX();
            float entityY = currentEntity.getY();

            currentEntity.getImage().draw(entityX, entityY);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        switch (c) {
            case 'w':
                directionVaisseau = UP;
                vaisseauMoving = true;
                break;
            case 'a':
                directionVaisseau = Direction.LEFT;
                vaisseauMoving = true;
                break;
            case 's':
                directionVaisseau = Direction.DOWN;
                vaisseauMoving = true;
                break;
            case 'd':
                directionVaisseau = Direction.RIGHT;
                vaisseauMoving = true;
                break;
            case ' ':
                float positionX = (vaisseau.getX() + vaisseau.getWidth() / 2) - 16;
                float positionY = vaisseau.getY() - 32;

                //TODO: ajouter differents types de lasers aleatoires avec un SpriteSheet

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
        // Utiliser un switch avec key permet de rendre la lecture plus facile et permet d'utiliser un switch pout toutes les touches
        switch (key) {
            case Input.KEY_ESCAPE:
                this.gc.exit();
                break;
            case Input.KEY_W:
            case Input.KEY_A:
            case Input.KEY_S:
            case Input.KEY_D:
                vaisseauMoving = false;
                break;
        }
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static float getScalingVitesse() {
        return SCALING_VITESSE;
    }

    public ArrayList<Entite> getEntiteListe() {
        return entiteListe;
    }

    public ArrayList<Collisionable> getCollisionables() {
        return collisionables;
    }
}