package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Asteroide;
import ca.qc.bdeb.info203.tp2.Entite.Entite;
import ca.qc.bdeb.info203.tp2.Entite.Laser;
import ca.qc.bdeb.info203.tp2.Entite.Vaisseau;
import org.newdawn.slick.*;

import java.util.ArrayList;

import static ca.qc.bdeb.info203.tp2.Direction.DOWN;
import static ca.qc.bdeb.info203.tp2.Direction.UP;

public class Jeu extends BasicGame implements Observateur {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 800;
    private static final float SCALING_VITESSE = 0.1f;
    private static final String GAME_TITLE = "SS Temp";
    // Délai en millisecondes
    private static final int DELAI_INVULNERABILITE = 3000;

    private static final ArrayList<Entite> entiteListe = new ArrayList<>();
    private static final ArrayList<Collisionable> collisionables = new ArrayList<>();

    private Image background;
    private Image heart;

    private Vaisseau vaisseau;

    private GameContainer gc;
    private Sound gameOver;
    private boolean gameOverSoundPlayed = false;

    private boolean vaisseauMoving = false;
    private Direction directionVaisseau;

    private MoteurCollision moteurCollision;

    private long momentCollision;

    public Jeu(String title) {
        super(title);
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer jeu = new AppGameContainer(new Jeu(GAME_TITLE));

        jeu.setDisplayMode(WIDTH, HEIGHT, false);
        jeu.setIcon("res/icon.png");
        jeu.setAlwaysRender(true);
        jeu.setShowFPS(false);

        jeu.setTargetFrameRate(60);

        jeu.start();
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

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        this.gc = gameContainer;
        this.moteurCollision = new MoteurCollision();

        //TODO: Background music
        gameOver = new Sound("res/Sounds/sfx_lose.wav");

        background = new Image("res/background.png").getScaledCopy(WIDTH, HEIGHT);
        heart = new Image("res/health.png");

        Asteroide asteroide = new Asteroide(100, 100, 0);

        vaisseau = new Vaisseau(0, 0, 128, 128, "res/ship.png");
        vaisseau.setLocation((WIDTH / 2 - vaisseau.getWidth() / 2), (float) (HEIGHT * 0.7));
        vaisseau.addObservateur(this);

        Collisionable bordures = new Bordure((int) vaisseau.getWidth(), (int) vaisseau.getHeight());

        entiteListe.add(vaisseau);
        entiteListe.add(asteroide);

        collisionables.add(asteroide);
        collisionables.add(bordures);
        collisionables.add(vaisseau);
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        ArrayList<Entite> listeEntiteDetruites = new ArrayList<>();
        ArrayList<Entite> listeEntiteCrees = new ArrayList<>();

        //TODO: Generation des asteroides

        //TODO: Garbage collector a certains intervalles

        for (Entite currentEntity : entiteListe) {
            boolean destruction = currentEntity.isDetruire();

            if (destruction) {
                listeEntiteDetruites.add(currentEntity);
            }
            // TODO: -Refactor cette partie de code pour prendre avantage des interfaces
            //       -On devrait pouvoir seulement appeler currentEntity.mouvementEntite sans utiliser instanceof
            if (currentEntity instanceof Vaisseau && vaisseauMoving) {
                currentEntity.mouvementEntite(directionVaisseau, delta);
            } else if (currentEntity instanceof Asteroide) {
                if (((Asteroide) currentEntity).isSeparer()) {
                    if (currentEntity.index != 4) {
                        float positionXAst1 = currentEntity.getX() - currentEntity.getWidth() / 8;
                        float positionYAst1 = currentEntity.getY() + currentEntity.getHeight() / 2;

                        float positionXAst2 = currentEntity.getX() + (5 * currentEntity.getWidth()) / 8;
                        float positionYAst2 = currentEntity.getY() + currentEntity.getHeight() / 2;

                        Asteroide ast1 = new Asteroide(positionXAst1, positionYAst1, currentEntity.index + 1);
                        Asteroide ast2 = new Asteroide(positionXAst2, positionYAst2, currentEntity.index + 1);

                        listeEntiteCrees.add(ast1);
                        listeEntiteCrees.add(ast2);
                    } else {
                        listeEntiteDetruites.add(currentEntity);
                    }

                } else {
                    currentEntity.mouvementEntite(DOWN, delta);
                }

            } else if (currentEntity instanceof Laser) {
                currentEntity.mouvementEntite(UP, delta);
            }

        }

        entiteListe.removeAll(listeEntiteDetruites);
        collisionables.removeAll(listeEntiteDetruites);
        listeEntiteDetruites.clear();

        entiteListe.addAll(listeEntiteCrees);
        collisionables.addAll(listeEntiteCrees);
        listeEntiteCrees.clear();

        moteurCollision.detecterCollisions(collisionables);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) {

        //TODO: Scrolling barckground (le prof veut ça)
        background.draw(0, 0);

        for (Entite currentEntity : entiteListe) {
            float entityX = currentEntity.getX();
            float entityY = currentEntity.getY();

            if (currentEntity instanceof Vaisseau && !((Vaisseau) currentEntity).getVulnerable()){
                ((Vaisseau) currentEntity).updateObservateurs();
                currentEntity.getImage().drawFlash(entityX, entityY);
            } else {
                currentEntity.getImage().draw(entityX, entityY);
            }

            //TODO: Utile pour debug les collisions, mais à enlever plus tard
            //g.drawLine(currentEntity.getX(), currentEntity.getY(), (currentEntity.getX() + currentEntity.getWidth()), (currentEntity.getY() + currentEntity.getHeight()));
        }

        switch (vaisseau.getLives()) {
            case 3:
                heart.draw(10, 10);
                heart.draw(84, 10);
                heart.draw(158, 10);
                break;
            case 2:
                heart.draw(10, 10);
                heart.draw(84, 10);
                break;
            case 1:
                heart.draw(10, 10);
                break;
            case 0:
                //TODO: Ajouter fonction qui demande de rejouer (le prof veut ça)

                doGameOver();
        }

        g.drawString("Minerai dans le vaisseau: " + String.valueOf(Cargo.getCargaisonVaisseau()) + " / " + Cargo.CARGAISON_VAISSEAU_MAX, 10, 84);
        g.drawString("Minerai envoyé sur Mars: " + String.valueOf(Cargo.getCargaisonMars()), 10, 104);
    }

    private void doGameOver() {
        if (!gameOverSoundPlayed) {
            gameOver.play();
            gameOverSoundPlayed = true;
        }

        try {
            Thread.sleep(1000);
            gc.exit();
        } catch (InterruptedException ignored) {
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
                float positionX = (vaisseau.getX() + vaisseau.getWidth() / 2) - 8;
                float positionY = vaisseau.getY() - 32;

                Laser laser = new Laser(positionX, positionY, 16, 32, "res/laser.png");

                entiteListe.add(laser);
                collisionables.add(laser);
                break;
            case 'e':
                Cargo.transferCargaison();
                break;
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        // FIXME: Bug en appuyant sur plusieurs direction trop vite
        switch (key) {
            case Input.KEY_ESCAPE:
                doGameOver();
                break;
            case Input.KEY_W:
            case Input.KEY_A:
            case Input.KEY_S:
            case Input.KEY_D:
                vaisseauMoving = false;
                break;
        }
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