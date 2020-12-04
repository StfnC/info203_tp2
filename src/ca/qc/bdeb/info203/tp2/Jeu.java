package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.*;
import org.newdawn.slick.*;

import java.util.ArrayList;

import static ca.qc.bdeb.info203.tp2.Direction.*;

public class Jeu extends BasicGame implements Observateur {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 800;
    private static final float SCALING_VITESSE = 0.1f;
    private static final String GAME_TITLE = "SS Temp";
    // Délai en millisecondes
    private static final int DELAI_INVULNERABILITE = 3000;

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
    private long momentCollision;

    public static void main(String[] args) throws SlickException {
        AppGameContainer jeu = new AppGameContainer(new Jeu(GAME_TITLE));

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
        asteroide = new Asteroide(100, 100, spriteAsteroides, 0, 0);

        vaisseau = new Vaisseau(0, 0, 128, 128, "res/ship.png");
        vaisseau.setLocation((WIDTH / 2 - vaisseau.getWidth() / 2), (float) (HEIGHT * 0.7));
        vaisseau.addObservateur(this);

        bordures = new Bordure((int) vaisseau.getWidth(), (int) vaisseau.getHeight());

        entiteListe.add(vaisseau);
        entiteListe.add(asteroide);

        collisionables.add(asteroide);
        collisionables.add(bordures);
        collisionables.add(vaisseau);
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        ArrayList<Entite> listeTemp = new ArrayList<>();

        //TODO: Generation des asteroides
        for (Entite currentEntity : entiteListe) {
            boolean destruction = currentEntity.isDetruire();

            if (destruction) {
                listeTemp.add(currentEntity);
            } else {
                // TODO: -Refactor cette partie de code pour prendre avantage des interfaces
                //       -On devrait pouvoir seulement appeler currentEntity.mouvementEntite sans utiliser instanceof
                if (currentEntity instanceof Vaisseau && vaisseauMoving) {
                    currentEntity.mouvementEntite(directionVaisseau, delta);
                } else if (currentEntity instanceof Asteroide) {
                    currentEntity.mouvementEntite(DOWN, delta);
                } else if (currentEntity instanceof Laser) {
                    currentEntity.mouvementEntite(UP, delta);
                }
            }
        }
        entiteListe.removeAll(listeTemp);
        collisionables.removeAll(listeTemp);
        listeTemp.clear();

        moteurCollision.detecterCollisions(collisionables);

        if (vaisseau.getLives() <= 0) {
            this.gc.exit();
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) throws SlickException {
        background.draw(0, 0);

        for (Entite currentEntity : entiteListe) {
            float entityX = currentEntity.getX();
            float entityY = currentEntity.getY();

            currentEntity.getImage().draw(entityX, entityY);
            // TODO: Utile pour debug les collisions, mais à enlever plus tard
//            g.drawLine(currentEntity.getX(), currentEntity.getY(), (currentEntity.getX() + currentEntity.getWidth()), (currentEntity.getY() + currentEntity.getHeight()));
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
                collisionables.add(laser);
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

    @Override
    public void update(long millis) {
        if (millis - this.momentCollision > DELAI_INVULNERABILITE) {
            vaisseau.setVulnerable(true);
            this.momentCollision = millis;
        }
    }
}