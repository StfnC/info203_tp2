package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.*;

public class Jeu extends BasicGame {

    private static final int WIDTH = 1440;
    private static final int HEIGHT = 900;

    public static void main(String[] args) throws SlickException {
        AppGameContainer jeu = new AppGameContainer(new Jeu("Jeu"));

        jeu.setDisplayMode(WIDTH, HEIGHT, false);
        jeu.setAlwaysRender(true);

        jeu.start();
    }

    public Jeu(String title) {
        super(title);
    }
    @Override
    public void init(GameContainer gameContainer) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

    }
}
