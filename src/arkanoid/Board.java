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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

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

    private String mainMessage;

    private int tickMessage = 0;

    //broj zivota
    private int numberOfLife;

    private Thread runner;
    private TargetThread targetThread;
    private StarThread starThread;

    public static GameState gameState;

    URL helloURL = getClass().getClassLoader().getResource("img/hello.png");
    Image hello = new ImageIcon(helloURL).getImage();
    URL nextURL = getClass().getClassLoader().getResource("img/next.png");
    Image nextLevel = new ImageIcon(nextURL).getImage();
    URL gameOverURL = getClass().getClassLoader().getResource("img/gameover.png");
    Image gameOver = new ImageIcon(gameOverURL).getImage();
    URL pauseURL = getClass().getClassLoader().getResource("img/pause.png");
    Image pauseGame = new ImageIcon(pauseURL).getImage();

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

        starThread = new StarThread(this);

        targetThread = new TargetThread(this, getPad(), starThread);

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

        this.starThread.restartStar();

        for (int i = 0; i < getTargetThread().getListTargets().size(); i++) {
            this.add(getTargetThread().getListTargets().get(i));
        }

        getPad().reset();

        this.add(this.getTargetThread().getListBalls().get(0));
        this.add(getPad());

        playSound("sounds/im_so_ready.wav");

        this.setMainMessageAndTick("Srećno!!! :D :P");
    }

    /**
     * Funkcija generiše novi nivo.
     */
    public void newLevel() {
        gameState = GameState.PLAY;

        getPad().reset();

        this.getTargetThread().generateTargets();
        this.getTargetThread().restartBalls();

        for (int i = 0; i < getTargetThread().getListTargets().size(); i++) {
            this.add(getTargetThread().getListTargets().get(i));
        }

        playSound("sounds/yes-1.wav");

        this.setMainMessageAndTick("Novi nivo");
    }

    /**
     * Funkcija postavlja parametre za stopiranje igre.
     *
     * @param message Poruka koja se ispisuje na ekran.
     */
    public void stopGame(String message) {
        gameState = GameState.LOSE;
        this.message = message;
        playSound("sounds/maybe-next-time-huh.wav");

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

            //Napiši glavnu poruku
            int messageWidth = getFontMetrics(getFont()).stringWidth(mainMessage);
            g2.drawString(mainMessage, PANEL_WIDTH / 2 - messageWidth / 2, PANEL_HEIGHT / 2);

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
     * Funkcija postavlja glavnu porku na ekran i postavlja tajmer poruke koja
     * će se prikazivati na 0.
     *
     * @param msg poruka koja se ispisuje
     */
    public void setMainMessageAndTick(String msg) {
        this.setMainMessage(msg);
        this.setTickMessage(0);
    }

    /**
     * Funkcija obrađuje proces runner.
     */
    @Override
    public void run() {
        while (true) {

            if (this.tickMessage == 67) {
                this.setMainMessage("");
                this.tickMessage = -1;
            } else if (this.tickMessage >= 0 && this.tickMessage < 67) {
                this.tickMessage++;
            }

            repaint();

            try {
                Thread.sleep(30); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public void playSound(String path) {
        InputStream inputStream;
        try {
            /*
             inputStream = new FileInputStream(path);
             AudioStream au = new AudioStream(inputStream);
             AudioPlayer.player.start(au);
             */
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    getClass().getClassLoader().getResource(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * @return the mainMessage
     */
    public String getMainMessage() {
        return mainMessage;
    }

    /**
     * @param mainMessage the mainMessage to set
     */
    public void setMainMessage(String mainMessage) {
        this.mainMessage = mainMessage;
    }

    /**
     * @return the pad
     */
    public Pad getPad() {
        return pad;
    }

    /**
     * @param pad the pad to set
     */
    public void setPad(Pad pad) {
        this.pad = pad;
    }

    /**
     * @return the tickMessage
     */
    public int getTickMessage() {
        return tickMessage;
    }

    /**
     * @param tickMessage the tickMessage to set
     */
    public void setTickMessage(int tickMessage) {
        this.tickMessage = tickMessage;
    }

    /**
     * @return the starThread
     */
    public StarThread getStarThread() {
        return starThread;
    }

    /**
     * @param starThread the starThread to set
     */
    public void setStarThread(StarThread starThread) {
        this.starThread = starThread;
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
                    getPad().moveLeft();
                } else if (keyCode == KeyEvent.VK_RIGHT) //dugme desno
                {
                    getPad().moveRight();
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
                getPad().stopMoving();
            }
        }
    }
}
