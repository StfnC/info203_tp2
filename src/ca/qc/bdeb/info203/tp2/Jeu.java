package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.Entite.Asteroide;
import ca.qc.bdeb.info203.tp2.Entite.Entite;
import ca.qc.bdeb.info203.tp2.Entite.Laser;
import ca.qc.bdeb.info203.tp2.Entite.Vaisseau;
import ca.qc.bdeb.info203.tp2.Enum.Direction;
import ca.qc.bdeb.info203.tp2.Enum.TailleAsteroide;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.Random;

import static ca.qc.bdeb.info203.tp2.Enum.Direction.*;

public class Jeu extends BasicGame implements Observateur {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 800;
    private static final int DELAI_INVULNERABILITE = 3000;
    private static final int DELAI_SPAWN_ASTEROIDES = 1500;

    private static final float SCALING_VITESSE = 0.1f;
    private static final String GAME_TITLE = "SS Temp";

    private static final TailleAsteroide[] TAILLES_ASTEROIDES_GENERES = {TailleAsteroide.TRES_GRAND, TailleAsteroide.GRAND};
    private static final Direction[] DIRECTIONS_POSSIBLES_ASTEROIDES = {Direction.DOWN, Direction.RIGHT, Direction.LEFT};

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

        vaisseau = new Vaisseau(0, 0, 128, 128, "res/ship.png");
        vaisseau.setLocation((WIDTH / 2 - vaisseau.getWidth() / 2), (float) (HEIGHT * 0.7));
        vaisseau.addObservateur(this);

        Collisionable bordures = new Bordure((int) vaisseau.getWidth(), (int) vaisseau.getHeight());

        entiteListe.add(vaisseau);

        collisionables.add(bordures);
        collisionables.add(vaisseau);
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        // TODO: Verifier que les noms des constantes respectent les conventions

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
            if (currentEntity instanceof Asteroide) {
                Asteroide asteroide = (Asteroide) currentEntity;
                if (asteroide.isSeparer()) {
                    separerAsteroide(asteroide);
                }
            }
            currentEntity.deplacer(delta);
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

            // FIXME: Utile pour debug les collisions, mais à enlever plus tard
//            g.drawLine(currentEntity.getX(), currentEntity.getY(), (currentEntity.getX() + currentEntity.getWidth()), (currentEntity.getY() + currentEntity.getHeight()));
        }

        switch (vaisseau.getLives()) {
            // TODO: Créer une fonction qui permet de dessiner un nombre dynamique de coeurs, sans hardcoder
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

        g.drawString("Minerai dans le vaisseau: " + String.valueOf(vaisseau.getCargo().getCargaisonVaisseau()) + " / " + vaisseau.getCargo().getCargaisonVaisseauMax(), 10, 84);
        g.drawString("Minerai envoyé sur Mars: " + String.valueOf(vaisseau.getCargo().getCargaisonMars()), 10, 104);
    }

    public void genererAsteroideRandom() throws SlickException {
        Random r = new Random();
        TailleAsteroide tailleAsteroide = TAILLES_ASTEROIDES_GENERES[r.nextInt(TAILLES_ASTEROIDES_GENERES.length)];
        // Choisi une direction au hasard
        Direction direction = DIRECTIONS_POSSIBLES_ASTEROIDES[r.nextInt(DIRECTIONS_POSSIBLES_ASTEROIDES.length)];
        Asteroide asteroide = new Asteroide(0, 0, tailleAsteroide, direction);
        trouverPositionDepartAsteroide(asteroide);
        entiteListe.add(asteroide);
        collisionables.add(asteroide);
    }

    private void trouverPositionDepartAsteroide(Asteroide asteroide) {
        Random r = new Random();

        float posX = 0;
        float posY = 0;

        switch (asteroide.getDirection()) {
            case UP:
                break;
            case LEFT:
                posX = WIDTH + asteroide.getWidth();
                posY = r.nextInt(WIDTH / 4);
                break;
            case DOWN:
                posX = r.nextInt(WIDTH - (int) asteroide.getWidth());
                posY = -asteroide.getHeight();
                break;
            case RIGHT:
                posX = -asteroide.getWidth();
                posY = r.nextInt(WIDTH / 4);
                break;
        }
        asteroide.setLocation(posX, posY);
    }

    public void separerAsteroide(Asteroide asteroide) throws SlickException {
        if (!asteroide.getTailleAsteroide().equals(TailleAsteroide.TRES_PETIT)) {
            float positionXAst1 = asteroide.getX() - asteroide.getWidth() / 8;
            float positionYAst1 = asteroide.getY() + asteroide.getHeight() / 2;

            float positionXAst2 = asteroide.getX() + (5 * asteroide.getWidth()) / 8;
            float positionYAst2 = asteroide.getY() + asteroide.getHeight() / 2;

            TailleAsteroide taillePlusPetite = TailleAsteroide.tailleParValeurNumerique(asteroide.getTailleAsteroide().valeurNumerique - 1);
            Asteroide ast1 = new Asteroide(positionXAst1, positionYAst1, taillePlusPetite, asteroide.getDirection());
            Asteroide ast2 = new Asteroide(positionXAst2, positionYAst2, taillePlusPetite, asteroide.getDirection());

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
                vaisseau.setDirection(UP);
                vaisseau.setSeDeplace(true);
                break;
            case 'a':
                vaisseau.setDirection(LEFT);
                vaisseau.setSeDeplace(true);
                break;
            case 's':
                vaisseau.setDirection(DOWN);
                vaisseau.setSeDeplace(true);
                break;
            case 'd':
                vaisseau.setDirection(RIGHT);
                vaisseau.setSeDeplace(true);
                break;
            case ' ':
                float positionX = (vaisseau.getX() + vaisseau.getWidth() / 2) - 8;
                float positionY = vaisseau.getY() - 32;

                Laser laser = new Laser(positionX, positionY, 16, 32, "res/laser.png");

                entiteListe.add(laser);
                collisionables.add(laser);
                break;
            case 'e':
                vaisseau.getCargo().transferCargaison();
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
                vaisseau.setSeDeplace(false);
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