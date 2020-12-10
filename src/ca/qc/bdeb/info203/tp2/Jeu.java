package ca.qc.bdeb.info203.tp2;

import ca.qc.bdeb.info203.tp2.entites.*;
import ca.qc.bdeb.info203.tp2.enums.Direction;
import ca.qc.bdeb.info203.tp2.enums.TailleAsteroide;
import ca.qc.bdeb.info203.tp2.interfaces.Collisionable;
import ca.qc.bdeb.info203.tp2.interfaces.Observateur;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe qui contient la logique du jeu
 */
public class Jeu extends BasicGame implements Observateur {
    private static final int DELAI_INVULNERABILITE = 3000;
    private static final int DELAI_SPAWN_ASTEROIDES = 1500;
    private static final int ESPACE_ENTRE_COEURS = 10;

    private static final float SCALING_VITESSE = 0.1f;

    private static final TailleAsteroide[] TAILLES_ASTEROIDES_GENERES = {TailleAsteroide.TRES_GRAND, TailleAsteroide.GRAND};
    private static final Direction[] DIRECTIONS_POSSIBLES_ASTEROIDES = {Direction.DOWN, Direction.RIGHT, Direction.LEFT};

    private ArrayList<Integer> listeTouchesMouvement = new ArrayList<>();
    private ArrayList<Entite> listeEntites = new ArrayList<>();
    private ArrayList<Collisionable> listeCollisionables = new ArrayList<>();
    private ArrayList<Entite> listeEntiteDetruites = new ArrayList<>();
    private ArrayList<Entite> listeEntiteCrees = new ArrayList<>();

    private Image backgroundTile;
    private Image heart;

    private Vaisseau vaisseau;

    private GameContainer gc;
    private Input input;

    private Sound bgMusic;
    private Sound gameOverSound;
    private boolean gameOver;

    private MoteurCollision moteurCollision;

    private long momentCollision;
    private long momentSpawnAsteroide;
    private long scalingValueBackground = 0;

    /**
     * Construit un Jeu avec un nom en paramêtre
     *
     * @param title Nom du jeu
     */
    public Jeu(String title) {
        super(title);
    }

    /**
     * Initialise le conteneur du jeu, l'input, le moteur de collision
     * Initialise les sons, la musique et les images
     * Nettoie les différentes listes
     * Initialise le vaisseau
     * Initialise la bordure
     *
     * @param gameContainer Conteneur du jeu
     * @throws SlickException Exception Slick qui peut être levée lors de l'initialisation
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        this.gc = gameContainer;
        this.input = gc.getInput();
        this.moteurCollision = new MoteurCollision();

        bgMusic = new Sound("res/Sounds/sfx_bgMusic.wav");
        bgMusic.loop();

        gameOverSound = new Sound("res/Sounds/sfx_lose.wav");
        gameOver = false;

        backgroundTile = new Image("res/bgTile.png");
        heart = new Image("res/health.png");

        // On nettoie les listes, utile dans le cas où le joueur a recommencé une partie
        this.listeTouchesMouvement.clear();
        this.listeEntites.clear();
        this.listeCollisionables.clear();
        this.listeEntiteCrees.clear();
        this.listeEntiteDetruites.clear();

        vaisseau = new Vaisseau(0, 0, 128, 128, "res/ship.png");
        vaisseau.setLocation((Main.WIDTH / 2 - vaisseau.getWidth() / 2), (float) (Main.HEIGHT * 0.7));
        vaisseau.addObservateur(this);

        Collisionable bordures = new Bordure((int) vaisseau.getWidth(), (int) vaisseau.getHeight());

        listeEntites.add(vaisseau);

        listeCollisionables.add(bordures);
        listeCollisionables.add(vaisseau);
    }

    /**
     * Boucle du jeu
     * Capture et traite les touches de mouvement
     * Fait apparaître un astéroide à un intervalle régulier
     * Sépare les astéroides
     * Déplace les entités
     * Détruit les entités à détruire
     *
     * @param gameContainer Conteneur du jeu
     * @param delta         Intervalle de temps entre la frame précédente et celle actuelle
     * @throws SlickException Exception Slick qui peut être levée lors de la boucle de jeu
     */
    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        // FIXME: -Bug qui fait que parfois le temps d'invulnérabilité est pas respecté
        //        -Arrive dans les parties lorsqu'on fait rejouer
        long tempsActuel = System.currentTimeMillis();

        getTouchesMouvement();
        traiterTouchesMouvement();

        nettoyerAsteroidesHorsEcran();

        if (tempsActuel - momentSpawnAsteroide > DELAI_SPAWN_ASTEROIDES) {
            genererAsteroideRandom();
            momentSpawnAsteroide = tempsActuel;
        }

        for (Entite currentEntity : listeEntites) {
            boolean destruction = currentEntity.isDetruire();

            if (destruction) {
                listeEntiteDetruites.add(currentEntity);
            }
            if (currentEntity instanceof Asteroide) {
                Asteroide asteroide = (Asteroide) currentEntity;
                if (asteroide.isSeparer()) {
                    separerAsteroide(asteroide);
                }
            }
            currentEntity.deplacer(delta);
        }

        listeEntites.removeAll(listeEntiteDetruites);
        listeCollisionables.removeAll(listeEntiteDetruites);
        listeEntiteDetruites.clear();

        listeEntites.addAll(listeEntiteCrees);
        listeCollisionables.addAll(listeEntiteCrees);
        listeEntiteCrees.clear();

        moteurCollision.detecterCollisions(listeCollisionables);

        scalingValueBackground += SCALING_VITESSE * 2 * delta;
    }

    /**
     * On dessine le background avec doBackground.
     * On dessine toutes les entités dans listeEntites.
     * On vérifie la collision du vaisseau et le dessine avec drawFlash() si c'est le cas.
     * On dessine les coeurs si le vaisseau est vivant, sinon on dessine le message du Game Over.
     * Finalement, on dessine le score sur l'écran.
     */
    @Override
    public void render(GameContainer gameContainer, Graphics g) {

        doBackground(g);

        for (Entite currentEntity : listeEntites) {
            float entityX = currentEntity.getX();
            float entityY = currentEntity.getY();

            if (currentEntity instanceof Vaisseau && !((Vaisseau) currentEntity).getVulnerable()) {
                ((Vaisseau) currentEntity).updateObservateurs();
                currentEntity.getImage().drawFlash(entityX, entityY);
            } else {
                currentEntity.getImage().draw(entityX, entityY);
            }
        }

        if (vaisseau.getLives() > 0) {
            dessinerCoeurs();
        } else {
            String messageGameOver = "Voulez-vous rejouer (O pour rejouer, ESC pour quitter)";
            // Le -[...].getWidth(message) est pour centrer le texte
            g.drawString(messageGameOver, (Main.WIDTH - g.getFont().getWidth(messageGameOver)) / 2, Main.HEIGHT / 2);
            doGameOver();
        }

        g.drawString("Minerai dans le vaisseau: " + vaisseau.getCargo().getCargaisonVaisseau() + " / " + vaisseau.getCargo().getCargaisonVaisseauMax(), 10, 84);
        g.drawString("Minerai envoyé sur Mars: " + vaisseau.getCargo().getCargaisonMars(), 10, 104);
    }

    /**
     * Change le boolean detruire si les entités sont en dehors de l'écran
     */
    private void nettoyerAsteroidesHorsEcran() {
        for (Entite entite : listeEntites) {
            if (entite instanceof Asteroide) {
                // Si un asteroide est plus que 2 fois sa largeur et/ou hauteur hors de la fenêtre, on le marque pour être détruit
                boolean horsX = entite.getX() > Main.WIDTH + 2 * entite.getWidth() || entite.getX() < -2 * entite.getWidth();
                boolean horsY = entite.getY() > Main.HEIGHT + 2 * entite.getHeight() || entite.getY() < -2 * entite.getHeight();
                if (horsX || horsY) {
                    entite.setDetruire(true);
                }
            }
        }
    }

    /**
     * Méthode en charge du dessin des vies du vaisseau restantes
     */
    public void dessinerCoeurs() {
        for (int i = 0; i < vaisseau.getLives(); i++) {
            heart.draw(ESPACE_ENTRE_COEURS + (heart.getWidth() + ESPACE_ENTRE_COEURS) * i, 10);
        }
    }

    /**
     * Détecte si les touches de mouvement sont appuyées
     */
    public void getTouchesMouvement() {
        // W
        if (input.isKeyDown(Input.KEY_W)) {
            if (!listeTouchesMouvement.contains(Input.KEY_W)) {
                listeTouchesMouvement.add(Input.KEY_W);
            }
        } else {
            listeTouchesMouvement.remove((Integer) Input.KEY_W);
        }
        // A
        if (input.isKeyDown(Input.KEY_A)) {
            if (!listeTouchesMouvement.contains(Input.KEY_A)) {
                listeTouchesMouvement.add(Input.KEY_A);
            }
        } else {
            listeTouchesMouvement.remove((Integer) Input.KEY_A);
        }
        // S
        if (input.isKeyDown(Input.KEY_S)) {
            if (!listeTouchesMouvement.contains(Input.KEY_S)) {
                listeTouchesMouvement.add(Input.KEY_S);
            }
        } else {
            listeTouchesMouvement.remove((Integer) Input.KEY_S);
        }
        // D
        if (input.isKeyDown(Input.KEY_D)) {
            if (!listeTouchesMouvement.contains(Input.KEY_D)) {
                listeTouchesMouvement.add(Input.KEY_D);
            }
        } else {
            listeTouchesMouvement.remove((Integer) Input.KEY_D);
        }
    }

    /**
     * Change la direction du vaisseau dépendament des touches appuyées
     */
    public void traiterTouchesMouvement() {
        // W
        if (listeTouchesMouvement.contains(Input.KEY_W)) {
            vaisseau.setDirection(Direction.UP);
            vaisseau.setSeDeplace(true);
        }
        // A
        if (listeTouchesMouvement.contains(Input.KEY_A)) {
            vaisseau.setDirection(Direction.LEFT);
            vaisseau.setSeDeplace(true);
        }
        // S
        if (listeTouchesMouvement.contains(Input.KEY_S)) {
            vaisseau.setDirection(Direction.DOWN);
            vaisseau.setSeDeplace(true);
        }
        // D
        if (listeTouchesMouvement.contains(Input.KEY_D)) {
            vaisseau.setDirection(Direction.RIGHT);
            vaisseau.setSeDeplace(true);
        }
    }

    /**
     * Détecte les touches appuyées et agit en conséquence
     * N'est pas en charge des touches pour bouger, car cause des bugs lorsqu'on appuie rapidement sur plusieurs touches
     *
     * @param key Numéro de la touche appuyée
     * @param c   Charactère de la touche appuyée
     */
    @Override
    public void keyPressed(int key, char c) {
        switch (key) {
            case Input.KEY_SPACE:
                // Tirer un laser
                if (vaisseau.getPeutTirer()) {
                    float positionX = (vaisseau.getX() + vaisseau.getWidth() / 2) - 8;
                    float positionY = vaisseau.getY() - 32;

                    Laser laser = new Laser(positionX, positionY, 16, 32, "res/laser.png");

                    listeEntites.add(laser);
                    listeCollisionables.add(laser);
                }
                break;
            case Input.KEY_E:
                // Envoyer la cargaison sur Mars
                vaisseau.getCargo().transferCargaison();
                break;
            case Input.KEY_ESCAPE:
                // Quitter le jeu
                gc.exit();
                break;
            case Input.KEY_O:
                // Recommencer la partie
                if (gameOver) {
                    try {
                        this.init(gc);
                    } catch (SlickException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * S'assure que le vaisseau ne bouge plus lorsque les touches de mouvement sont relâchées
     *
     * @param key Numéro de la touche appuyée
     * @param c   Charactère de la touche appuyée
     */
    @Override
    public void keyReleased(int key, char c) {
        switch (key) {
            case Input.KEY_W:
            case Input.KEY_A:
            case Input.KEY_S:
            case Input.KEY_D:
                vaisseau.setSeDeplace(false);
                break;
        }
    }

    /**
     * Génère un Asteroide de taille, position et direction aléatoire
     *
     * @throws SlickException Exception Slick qui peut être levée lors de la construction d'un Asteroide
     */
    public void genererAsteroideRandom() throws SlickException {
        Random r = new Random();
        // Choisi une taille au hasard
        TailleAsteroide tailleAsteroide = TAILLES_ASTEROIDES_GENERES[r.nextInt(TAILLES_ASTEROIDES_GENERES.length)];
        // Choisi une direction au hasard
        Direction direction = DIRECTIONS_POSSIBLES_ASTEROIDES[r.nextInt(DIRECTIONS_POSSIBLES_ASTEROIDES.length)];
        Asteroide asteroide = new Asteroide(0, 0, tailleAsteroide, direction);
        // Donne une position au hasard
        trouverPositionDepartAsteroide(asteroide);
        listeEntites.add(asteroide);
        listeCollisionables.add(asteroide);
    }

    /**
     * Donne une position aléatoire à un Asteroide
     *
     * @param asteroide Asteroide à qui il faut donner une position aléatoire
     */
    private void trouverPositionDepartAsteroide(Asteroide asteroide) {
        Random r = new Random();

        float posX = 0;
        float posY = 0;

        // Pour la gauche et la droite, on s'assure de faire apparaître l'astéroide à un y où le vaisseau peut le détruire
        switch (asteroide.getDirection()) {
            case UP:
                break;
            case LEFT:
                posX = Main.WIDTH + asteroide.getWidth();
                posY = r.nextInt(Main.WIDTH / 4);
                break;
            case DOWN:
                posX = r.nextInt(Main.WIDTH - (int) asteroide.getWidth());
                posY = -asteroide.getHeight();
                break;
            case RIGHT:
                posX = -asteroide.getWidth();
                posY = r.nextInt(Main.WIDTH / 4);
                break;
        }
        asteroide.setLocation(posX, posY);
    }

    /**
     * On définit la position des deux nouveaux astéroïdes et on les crée en les ajoutant à listeEntiteCrees
     *
     * @param asteroide L'asteroide à séparer
     */
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

    /**
     * Méthode qui gère le son du GameOver et qui empeche l'utilisateur d'interragir avec le vaisseau
     */
    public void doGameOver() {
        if (!gameOver) {
            gameOverSound.play();
            gameOver = true;
            vaisseau.setPeutSeDeplacer(false);
            vaisseau.setPeutTirer(false);
        }
    }

    /**
     * Méthode qui utilise les modulos pour s'assurer que l'écran est toujours rempli avec bgTile
     *
     * @param g Permet de dessiner sur l'écran
     */
    public void doBackground(Graphics g) {
        for (int i = 0; i < Main.WIDTH; i = i + 256) {
            for (long j = scalingValueBackground % 256 - 256; j < Main.HEIGHT; j = j + 256) {
                g.drawImage(backgroundTile, i, j);
            }
        }
    }


    /**
     * Gère l'invulnérabilité du vaisseau
     *
     * @param millis Temps en millisecondes lorsque l'update est survenue
     */
    @Override
    public void update(long millis) {
        // On regarde si le temps entre la dernière collision et celle actuelle est supérieur au délai d'invulnérabilité
        if (millis - this.momentCollision > DELAI_INVULNERABILITE) {
            vaisseau.setVulnerable(true);
            this.momentCollision = millis;
        }
    }

    /**
     * Getter du facteur par lequelle la vitesse est multipliée
     *
     * @return Facteur par lequelle la vitesse est multipliée
     */
    public static float getScalingVitesse() {
        return SCALING_VITESSE;
    }
}