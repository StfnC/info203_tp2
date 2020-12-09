package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Asteroide;
import ca.qc.bdeb.info203.tp2.Entite.Entite;
import ca.qc.bdeb.info203.tp2.Entite.Laser;
import ca.qc.bdeb.info203.tp2.Entite.Vaisseau;
import org.lwjgl.Sys;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.Random;

import static ca.qc.bdeb.info203.tp2.Direction.DOWN;
import static ca.qc.bdeb.info203.tp2.Direction.UP;

public class Jeu extends BasicGame implements Observateur {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 800;
    private static final int DELAI_INVULNERABILITE = 3000;
    private static final int DELAI_SPAWN_ASTEROIDES = 1500;

    private static final float SCALING_VITESSE = 0.1f;
    private static final String GAME_TITLE = "SS Temp";

    private static final int[] TAILLES_ASTEROIDES_GENERES = {0, 1};

    private ArrayList<Entite> entiteListe = new ArrayList<>();
    private ArrayList<Collisionable> collisionables = new ArrayList<>();
    private ArrayList<Entite> listeEntiteDetruites = new ArrayList<>();
    private ArrayList<Entite> listeEntiteCrees = new ArrayList<>();

    private Image backgroundTile;
    private Image heart;

    private Vaisseau vaisseau;

    private GameContainer gc;
    private Sound gameOver;
    private boolean gameOverSoundPlayed = false;

    private boolean vaisseauMoving = false;
    private Direction directionVaisseau;

    private MoteurCollision moteurCollision;

    private long momentCollision;
    private long momentSpawnAsteroide;
    private long scalingValue = 0;

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

        backgroundTile = new Image("res/bgTile.png");
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
        // TODO: S'assurer de detruire les asteroides hors de l'ecran

        // TODO: Generation des asteroides

        // TODO: Garbage collector a certains intervalles
        if (System.currentTimeMillis() - momentSpawnAsteroide > DELAI_SPAWN_ASTEROIDES) {
            genererAsteroideRandom();
            momentSpawnAsteroide = System.currentTimeMillis();
        }

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
                Asteroide asteroide = (Asteroide) currentEntity;
                if (asteroide.isSeparer()) {
                    separerAsteroide(asteroide);
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

        scalingValue += SCALING_VITESSE * 2 * delta;
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g) {

        doBackground(gameContainer, g);

        for (Entite currentEntity : entiteListe) {
            float entityX = currentEntity.getX();
            float entityY = currentEntity.getY();

            if (currentEntity instanceof Vaisseau && !((Vaisseau) currentEntity).getVulnerable()) {
                ((Vaisseau) currentEntity).updateObservateurs();
                currentEntity.getImage().drawFlash(entityX, entityY);
            } else {
                currentEntity.getImage().draw(entityX, entityY);
            }

            //Utile pour debug les collisions, mais à enlever plus tard
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

    public void genererAsteroideRandom() throws SlickException {
        Random r = new Random();
        Asteroide asteroide = new Asteroide(0, 0, TAILLES_ASTEROIDES_GENERES[r.nextInt(2)]);
        float posX = genererEntierDansIntervalle(0, WIDTH - (int) asteroide.getWidth());
        float posY = -asteroide.getHeight();
        asteroide.setLocation(posX, posY);
        entiteListe.add(asteroide);
        collisionables.add(asteroide);
    }

    public float genererEntierDansIntervalle(int plancher, int plafond) {
        Random r = new Random();
        return r.nextInt(plafond) + plancher;
    }

    public void separerAsteroide(Asteroide asteroide) throws SlickException {
        if (asteroide.index != 4) {
            float positionXAst1 = asteroide.getX() - asteroide.getWidth() / 8;
            float positionYAst1 = asteroide.getY() + asteroide.getHeight() / 2;

            float positionXAst2 = asteroide.getX() + (5 * asteroide.getWidth()) / 8;
            float positionYAst2 = asteroide.getY() + asteroide.getHeight() / 2;

            Asteroide ast1 = new Asteroide(positionXAst1, positionYAst1, asteroide.index + 1);
            Asteroide ast2 = new Asteroide(positionXAst2, positionYAst2, asteroide.index + 1);

            listeEntiteCrees.add(ast1);
            listeEntiteCrees.add(ast2);
        } else {
            listeEntiteDetruites.add(asteroide);
        }
    }

    public void doGameOver() {
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

    public void doBackground(GameContainer gc, Graphics g) {
        for (int i = 0; i < WIDTH; i = i + 256) {
            for (long j = scalingValue % 256 - 256; j < HEIGHT; j = j + 256) {
                g.drawImage(backgroundTile, i, j);
            }
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