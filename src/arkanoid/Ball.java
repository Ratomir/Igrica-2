package arkanoid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

/**
 *
 * Klasa Ball nasleđuje klasu Rectangle.Double i implementira interface
 * GameObject.
 *
 * @author Ratomir
 */
public class Ball extends JPanel implements GameObject, Runnable {

    private final int w = 24;
    private final int h = 24;

    // Minimalni, maksimalni intenzitet brzine lopte i korak ubrzanja
    private final int DX = 4;
    private final int DY = 4;

    // Predstavljaju intenzitet po x i po y koordinati
    private int dx;
    private int dy;

    // Predstavljaju smer po x i po y koordinati
    private int directionX;
    private int directionY;

    private Board board;
    private Ellipse2D.Double ellipseForDrawing = new Ellipse2D.Double();

    private final Color fillColor = Color.RED;
    private final Color borderColor = Color.BLACK;

    private Boolean runningBall = true;
    private Thread threadBall;
    private Point position;
    
    private int speed;

    /**
     * Inicijalizuje loptu na tabli. Postavlja lopticu odmah iznad reketa i
     * postavlja brzinu na minimum.
     *
     * @param board Tabla kojoj lopta pripada.
     */
    public Ball(Board board) {
        this.board = board;
        this.directionX = 1;
        this.directionY = 1;
        this.setOpaque(false);
        this.setSize(w, h);
        
        this.speed = 40;
        
        this.threadBall = new Thread(this);
        this.threadBall.start();
    }

    /**
     * Menja smer lopte po x osi.
     */
    public void bouceHorizontal() {
        this.directionX = -this.directionX;
    }

    /**
     * Menja smer lopte po y osi.
     */
    public void bouceVertical() {
        this.directionY = -this.directionY;
    }

    /**
     * Resetuje poziciju lopte i postavlja je na pocetnu poziciju.
     */
    public void reset() {
        this.setLocation(Board.PANEL_WIDTH / 2 - w / 2, Board.PANEL_HEIGHT - Pad.getH() - h - 250);

        this.dx = DX;
        this.dy = DY;
    }

    /**
     * Vrsi pomeranje lopte. Ispituje poziciji lopte.
     */
    @Override
    public void move() {
        this.position = this.getLocation();
        int tempX = (int) position.getX() + dx * directionX;
        int tempY = (int) position.getY() + dy * directionY;
        this.setLocation(tempX, tempY);

        /*Ako je lokacija od lopte plus njena duzina veca ili jednaka duzini panela ili
         ako je lokacija lopte manja od 0, vrsi pomeranje horizontalno. */
        if (tempX + this.getSize().width >= board.PANEL_WIDTH || tempX <= 0) {
            this.bouceHorizontal();
        }

        //Ako je lopta prosla pored reketa vrsi se smanjivanje broja zivota.
        if (tempY + this.getSize().width >= board.PANEL_HEIGHT) {

            if (board.getTargetThread().getListBalls().size() > 1) {
                board.getTargetThread().getListBalls().remove(this);
                board.remove(this);
//                this.getThreadBall().interrupt();
                this.terminateThread();
            } else {

                board.setNumberOfLife(board.getNumberOfLife() - 1);

                if (board.getNumberOfLife() == 0) //testiranje na poslednji zivot
                {
                    board.stopGame("IGRICA GOTOVA, ZDRAVO!");
                } else {
                    reset();
                    this.bouceVertical();
                }
            }

        }

        /*
         Slucaj kada je loptica dosla do vrha prozora.
         */
        if (tempY <= 0) {
            this.bouceVertical();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (Board.gameState == Board.GameState.PLAY) {
            // Saveti pri iscrtavanju

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            this.draw(g2);

            // Sinhronizovanje sa grafickom kartom
            Toolkit.getDefaultToolkit().sync();

            // Optimizacija upotrebe RAM-a, 
            g.dispose();
        }
    }

    /**
     * Vrsi iscrtavanje lopte na tabli.
     *
     * @param g2 Graphics2D objekat na kojem se vrsi iscrtavanje.
     */
    @Override
    public void draw(Graphics2D g2) {
        ellipseForDrawing.setFrame(1, 1,
                this.getSize().getWidth() - 2, this.getSize().getHeight() - 2);

        g2.setPaint(fillColor); //postavljamo boju
        g2.fill(ellipseForDrawing); //sa postavljenom bojom filujemo objekat

        g2.setPaint(borderColor); //postavljamo boju
        g2.draw(ellipseForDrawing); //crtamo lopticu
    }

    @Override
    public void run() {

        while (getRunningBall()) {
            if (Board.gameState == Board.GameState.PLAY) {
                move();
                repaint();
            }

            try {
                Thread.sleep(speed); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
//                this.threadBall.interrupt();
            }
        }
    }

    /**
     * @return the threadBall
     */
    public Thread getThreadBall() {
        return threadBall;
    }

    /**
     * @param threadBall the threadBall to set
     */
    public void setThreadBall(Thread threadBall) {
        this.threadBall = threadBall;
    }

    @Override
    public void terminateThread() {
        this.setRunningBall((Boolean) false);
    }

    @Override
    public void startThread() {
        this.setRunningBall((Boolean) true);
    }

    /**
     * @return the runningBall
     */
    public Boolean getRunningBall() {
        return runningBall;
    }

    /**
     * @param runningBall the runningBall to set
     */
    public void setRunningBall(Boolean runningBall) {
        this.runningBall = runningBall;
    }

    /**
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
