package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.awt.*;
import java.util.ArrayList;

import static ca.qc.bdeb.info203.tp2.Direction.*;

public class Jeu extends BasicGame {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 800;
    private static final int vitesseVaisseau = 7;
    private static final int vitesseLaser = 5;
    private static final int vitesseAsteroide = 3;
    private static final String gameTitle = "SS Temp";

    private ArrayList<Entite> entiteListe;
    private Image background;

    private Vaisseau vaisseau;
    private Laser laser;

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

        bordures = new Collisionable() {
            private final Rectangle RECT = new Rectangle(Jeu.getWIDTH(), Jeu.getHEIGHT());

            @Override
            public void gererCollision(Collisionable objetEnCollision, Direction directionCollision) {
                // TODO: -Maybe changer gererCollision pour qu'on obtienne la direction de la collision directement
                //       -Pour trouver ça on pourrait avoir une classe MoteurCollision qui va s'occuoer de passer à travers une liste de collisionnable et dire si y a une collision
                if (objetEnCollision instanceof Vaisseau) {
                    Vaisseau vaisseau = (Vaisseau) objetEnCollision;
                    // FIXME: Beaucoup trop de if's faut trouver une façon de rendre ça plus clean
                    // FIXME: Fonctionne, mais seulement pour UP et LEFT, car j'account pas pour la taille de l'image en DOWN et RIGHT
                    if (directionCollision.equals(UP)) {
                        vaisseau.setLocation(vaisseau.getX(), 0f);
                    } else if (directionCollision.equals(DOWN)) {
                        vaisseau.setLocation(vaisseau.getX(), Jeu.HEIGHT);
                    } else if (directionCollision.equals(LEFT)) {
                        vaisseau.setLocation(0f, vaisseau.getY());
                    } else if (directionCollision.equals(RIGHT)) {
                        vaisseau.setLocation(Jeu.getWIDTH(), vaisseau.getY());
                    }
                }

            }

            @Override
            public Rectangle getRectangle() {
                return RECT;
            }
        };

        background = new Image("res/background.png").getScaledCopy(WIDTH, HEIGHT);

        vaisseau = new Vaisseau(0, 0, 96, 96, "res/ship.png");
        vaisseau.setLocation((WIDTH / 2 - vaisseau.getWidth() / 2), (float) (HEIGHT * 0.7));

        entiteListe = new ArrayList<>();
        entiteListe.add(vaisseau);
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        moteurCollision.detecterCollisionMur(vaisseau, bordures);
        for (int i = 0; i < entiteListe.size(); i++) {
            Entite currentEntity = entiteListe.get(i);
            boolean destruction = currentEntity.isDetruire();

            // TODO: -Refactor cette partie de code pour prendre avantage des interfaces
            //       -On devrait pouvoir seulement appeler currentEntity.mouvementEntite sans utiliser instanceof
            // TODO: -On devrait peut-être avoir la vitesse dans les différentes classes,
            //       -Ex.: Ça a plus de sens que la vitesse d'un asteroide soit dans la classe Asteroide
            if (currentEntity instanceof Vaisseau && vaisseauMoving) {
                currentEntity.mouvementEntite(directionVaisseau, vitesseVaisseau * delta);
            } else if (currentEntity instanceof Asteroide) {
                currentEntity.mouvementEntite(DOWN, vitesseAsteroide * delta);
            } else if (currentEntity instanceof Laser) {
                if (destruction) {
                    // FIXME: Cette ligne va throw une erreur, car on peut pas modifier un ArrayList pendant qu'on y accède avec un loop
                    entiteListe.remove(i);
                } else {
                    currentEntity.mouvementEntite(UP, vitesseLaser * delta);
                }
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
                //TODO: ajouter sound effects pour le laser

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
}