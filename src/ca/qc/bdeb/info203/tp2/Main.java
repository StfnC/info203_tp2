package ca.qc.bdeb.info203.tp2;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 800;
    public static final String GAME_TITLE = "SS Temp";

    public static void main(String[] args) throws SlickException {
        AppGameContainer jeu = new AppGameContainer(new Jeu(GAME_TITLE));

        jeu.setDisplayMode(WIDTH, HEIGHT, false);
        jeu.setIcon("res/icon.png");
        jeu.setAlwaysRender(true);
        jeu.setShowFPS(false);

        jeu.setTargetFrameRate(60);

        jeu.start();
    }
}
