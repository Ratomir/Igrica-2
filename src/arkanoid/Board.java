/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

/**
 * Klasa Board nasledjuje JPanel i implementira interface Runnable.
 *
 * @author Ratomir
 */
class Board extends JPanel implements Runnable {

    public static enum GameState {

        INIT, PLAY, WON, LOSE, NEXTLEVEL, PAUSE
    }

    /**
     * Sirina table
     */
    public static final int PANEL_WIDTH = 800;

    /**
     * Visina table
     */
    public static final int PANEL_HEIGHT = 615;

    /*
     Rastojanje izmedju meta po y koordinati.
     */
    public static int Y_SPACE_TARGET = 35;

    final Color BACKGROUND_COLOR = Color.CYAN;

    // Bodovi u igri
    private int myScore = 0;

    private int level = 1;

    // Objekti u igri
    private Pad pad;

    private String message;

    //broj zivota
    private int numberOfLife;

    final Thread runner;
    private TargetThread targetThread;

    public static GameState gameState;

    Image hello = Toolkit.getDefaultToolkit().getImage("src/img/hello.png");
    Image nextLevel = Toolkit.getDefaultToolkit().getImage("src/img/next.png");
    Image gameOver = Toolkit.getDefaultToolkit().getImage("src/img/gameover.png");
    Image pauseGame = Toolkit.getDefaultToolkit().getImage("src/img/pause.png");

    /**
     * Osnovni konstruktor koji postavlja osnovna podesavanja prozora za igricu.
     */
    public Board() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);
        setFocusable(true);
        setLayout(null);
        setFont(getFont().deriveFont(Font.BOLD, 18f));
        setDoubleBuffered(true);

        message = "ARKANOID";

        pad = new Pad(this, PANEL_WIDTH / 2 - Pad.getW() / 2, PANEL_HEIGHT - Pad.getH());

        //dodajemo osluskivac na Board za tastaturu
        addKeyListener(new GameKeyAdapter());

        targetThread = new TargetThread(this, pad);

        //dodajemo proces za board
        runner = new Thread(this);

        gameState = GameState.INIT;

        //startujemo proces
        runner.start();
    }

    /**
     * Funkcija postavlja parametre za pocetak igre.
     */
    public void startGame() {
        gameState = GameState.PLAY;

        if (this.getTargetThread().getListTargets() != null && this.getTargetThread().getListTargets().size() > 0) {
            for (int i = 0; i < this.getTargetThread().getListTargets().size(); i++) {
                this.remove(this.getTargetThread().getListTargets().get(i));
            }
        }

        if (this.getTargetThread().getListBalls() != null && this.getTargetThread().getListBalls().size() > 0) {
            for (int i = 0; i < this.getTargetThread().getListBalls().size(); i++) {
                this.remove(this.getTargetThread().getListBalls().get(i));
            }
        }

        setMyScore(0);
        this.level = 1;
        setNumberOfLife(5);

        this.getTargetThread().generateTargets();
        this.getTargetThread().restartBalls();

        for (int i = 0; i < getTargetThread().getListTargets().size(); i++) {
            this.add(getTargetThread().getListTargets().get(i));
        }

        pad.reset();

        this.add(this.getTargetThread().getListBalls().get(0));
        this.add(pad);
    }

    /**
     * Funkcija generiše novi nivo.
     */
    public void newLevel() {
        gameState = GameState.PLAY;

        pad.reset();

        this.getTargetThread().generateTargets();
        this.getTargetThread().restartBalls();

        for (int i = 0; i < getTargetThread().getListTargets().size(); i++) {
            this.add(getTargetThread().getListTargets().get(i));
        }
    }

    /**
     * Funkcija postavlja parametre za stopiranje igre.
     *
     * @param message Poruka koja se ispisuje na ekran.
     */
    public void stopGame(String message) {
        gameState = GameState.LOSE;
        this.message = message;
    }

    public void newLevelMessage(String message) {
        gameState = GameState.NEXTLEVEL;
        this.message = message;
    }

    /**
     * Dodaju se bodovi.
     *
     * @param number Broj bodova koji se dodaje na postojece bodove.
     */
    public void countScore(int number) {
        setMyScore(getMyScore() + number);
    }

    /**
     * Funckija vrsi iscrtavanje osnovnih parametara prozora, kao sto su
     * loptica, reket, poruke za bodove i zivote, kao i mete.
     *
     * @param g Paramtar koji sluzi za dohvatanje funkcija za iscrtavanje.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        if (gameState == GameState.PLAY) {
            // Saveti pri iscrtavanju
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Iscrtaj rezultat
            g2.drawString("" + getMyScore(), 10, 20);

            //Iscrtaj trenutni level
            g2.drawString("Level " + getLevel(), PANEL_WIDTH / 2 - 20, 20);

            //Iscrtaj broj zivota
            g2.drawString("Broj života: " + getNumberOfLife(), PANEL_WIDTH - 160, 20);

            // Sinhronizovanje sa grafickom kartom
            Toolkit.getDefaultToolkit().sync();

            // Optimizacija upotrebe RAM-a, 
            g.dispose();
        } else if (gameState == GameState.LOSE) {

            int messageWidth = getFontMetrics(getFont()).stringWidth(message);
            g2.drawString(message, PANEL_WIDTH / 2 - messageWidth / 2, PANEL_HEIGHT / 2);

            g2.drawImage(gameOver, PANEL_WIDTH / 2 - 120, PANEL_HEIGHT / 2 + 25, this);

        } else if (gameState == GameState.INIT) {

            int messageWidth = getFontMetrics(getFont()).stringWidth(message);
            g2.drawString(message, PANEL_WIDTH / 2 - messageWidth / 2, PANEL_HEIGHT / 2);

            g2.drawImage(hello, PANEL_WIDTH / 2 - 112 + messageWidth / 2, PANEL_HEIGHT / 2 + 25, this);

        } else if (gameState == GameState.NEXTLEVEL) {

            int messageWidth = getFontMetrics(getFont()).stringWidth(message);
            g2.drawString(message, PANEL_WIDTH / 2 - messageWidth / 2, PANEL_HEIGHT / 2);

            g2.drawImage(nextLevel, PANEL_WIDTH / 2 - nextLevel.getWidth(this) / 2, PANEL_HEIGHT / 2 + 25, this);
        } else if (gameState == GameState.PAUSE) {
            this.message = "Igrica je pauzirana pritisnite bilo koji taster za nastavak.";

            int messageWidth = getFontMetrics(getFont()).stringWidth(message);
            g2.drawString(message, PANEL_WIDTH / 2 - messageWidth / 2, PANEL_HEIGHT / 2);

            g2.drawImage(pauseGame, PANEL_WIDTH / 2 - nextLevel.getWidth(this) - 20, PANEL_HEIGHT / 2 + 25, this);
        }
    }

    /**
     * Funkcija pokrece proces za board.
     */
    @Override
    public void run() {
        while (true) {
            repaint();

            try {
                Thread.sleep(30); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * @return the numberOfLife
     */
    public int getNumberOfLife() {
        return numberOfLife;
    }

    /**
     * @param numberOfLife the numberOfLife to set
     */
    public void setNumberOfLife(int numberOfLife) {
        this.numberOfLife = numberOfLife;
    }

    /**
     * @return the myScore
     */
    public int getMyScore() {
        return myScore;
    }

    /**
     * @param myScore the myScore to set
     */
    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the targetThread
     */
    public TargetThread getTargetThread() {
        return targetThread;
    }

    /**
     * @param targetThread the targetThread to set
     */
    public void setTargetThread(TargetThread targetThread) {
        this.targetThread = targetThread;
    }

    private class GameKeyAdapter extends KeyAdapter {

        /**
         * Funkcija se izvrsava na svako pritisnuto dugme sa tastature.
         *
         * @param e Parametar koji nosi podatke o dugmetu koje je pozvalo
         * osluskivac.
         */
        @Override
        public void keyPressed(KeyEvent e) {
            if (gameState == GameState.NEXTLEVEL) {
                newLevel();
            } else if (gameState == GameState.PAUSE) {
                gameState = GameState.PLAY;
            } else {
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_LEFT) //dugme lijevo
                {
                    pad.moveLeft();
                } else if (keyCode == KeyEvent.VK_RIGHT) //dugme desno
                {
                    pad.moveRight();
                }
            }
        }

        /**
         * Funkcija se izvršava kada se dugme otpusti.
         *
         * @param e Parametar koji nosi podatke o dugmetu koje ga je pozvalo.
         */
        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
                pad.stopMoving();
            }
        }
    }
}
