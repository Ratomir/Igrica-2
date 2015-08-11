/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author Ratomir
 */
class Pad extends JPanel implements GameObject, Runnable {

    enum MovingState {

        STANDING, MOVING_LEFT, MOVING_RIGHT
    }

    /**
     * Sirina reketa
     */
    private static int w = 100;
    /**
     * Visina reketa
     */
    private static int h = 20;

    private int dx = 10;

    private Board board;
    private MovingState state;

    private Color fillColor = Color.BLACK;

    private Point position;
    private RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float();

    private Thread threadPad;

    private int upDown = 1;

    /**
     * Inicijalizuje reket na prosedjenoj lokaciji na tabli.
     *
     * @param board Tabla kojoj reket pripada.
     * @param x x koordinata gornjeg levog temena reketa.
     * @param y y koordinata gornjeg leveg temena reketa.
     */
    public Pad(Board board, int x, int y) {
        this.board = board;
        this.setLocation(x, y);
        this.setOpaque(false);
        this.setSize(w, h);
        this.state = MovingState.STANDING;

        threadPad = new Thread(this);
        threadPad.start();
    }

    /**
     * Postavlja stanje kretanja reketa u desno.
     */
    public void moveRight() {
        setState(MovingState.MOVING_RIGHT);
    }

    /**
     * Postavlja stanje kretanja reketa u levo.
     */
    public void moveLeft() {
        setState(MovingState.MOVING_LEFT);
    }

    /**
     * Postavlja stanje kretanja reketa u stajanje.
     */
    public void stopMoving() {
        setState(MovingState.STANDING);
    }

    /**
     * Izvrsava pomeranje reketa u zavisnosti od stanja.
     */
    public void move() {
        setPosition(this.getLocation());

        int tempX = (int) getPosition().getX();
        int tempY = (int) getPosition().getY();

        if (getState() == MovingState.MOVING_RIGHT) {
            tempX += getDx();
        } else if (getState() == MovingState.MOVING_LEFT) {
            tempX -= getDx();
        }

        if (tempX < 0) //slucaj kada je skroz levo reket
        {
            tempX = 0;
        } else if (tempX + this.getSize().width > getBoard().PANEL_WIDTH) //slucaj kada je skoz desno
        {
            tempX = getBoard().PANEL_WIDTH - this.getSize().width;
        }

        this.setLocation(tempX, tempY);
    }

    /**
     * Funkcija vrsi resetovanje koordinata za iscrtavanje reketa.
     */
    public void reset() {
        this.setW(100);
        this.setLocation(getBoard().PANEL_WIDTH / 2 - Pad.getW() / 2, Board.PANEL_HEIGHT - Pad.getH() + 2);
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (Board.gameState == Board.GameState.PLAY) {
            // Saveti pri iscrtavanju

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            draw(g2);

            // Sinhronizovanje sa grafickom kartom
            Toolkit.getDefaultToolkit().sync();

            // Optimizacija upotrebe RAM-a, 
            g.dispose();
        }
    }

    /**
     * Iscrtava reket na tabli.
     *
     * @param g2 Graphics2D objekat na kome se vrsi iscrtavanje.
     */
    public void draw(Graphics2D g2) {
        setRoundedRectangle(new RoundRectangle2D.Float(1, 1, getW() - 2, getH() - 2, 10, 10));

        g2.setPaint(getFillColor());
        g2.fill(getRoundedRectangle());
        g2.draw(getRoundedRectangle()); //crtamo reket
    }

    @Override
    public void terminateThread() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startThread() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void update() {
        if (this.board.getLevel() % 2 == 0) {
           if(getW() == 50)
               upDown = 1;
           else if(getW() == 100)
               upDown = -1;
           
           setW(getW() + upDown);
        }
    }

    @Override
    public void run() {

        while (true) {
            move();
            update();
            repaint();

            try {
                Thread.sleep(30); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * @return the w
     */
    public static int getW() {
        return w;
    }

    /**
     * @param aW the w to set
     */
    public static void setW(int aW) {
        w = aW;
    }

    /**
     * @return the h
     */
    public static int getH() {
        return h;
    }

    /**
     * @param aH the h to set
     */
    public static void setH(int aH) {
        h = aH;
    }

    /**
     * @return the dx
     */
    public int getDx() {
        return dx;
    }

    /**
     * @param dx the dx to set
     */
    public void setDx(int dx) {
        this.dx = dx;
    }

    /**
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @return the state
     */
    public MovingState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(MovingState state) {
        this.state = state;
    }

    /**
     * @return the fillColor
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * @param fillColor the fillColor to set
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * @return the roundedRectangle
     */
    public RoundRectangle2D getRoundedRectangle() {
        return roundedRectangle;
    }

    /**
     * @param roundedRectangle the roundedRectangle to set
     */
    public void setRoundedRectangle(RoundRectangle2D roundedRectangle) {
        this.roundedRectangle = roundedRectangle;
    }

    /**
     * @return the threadPad
     */
    public Thread getThreadPad() {
        return threadPad;
    }

    /**
     * @param threadPad the threadPad to set
     */
    public void setThreadPad(Thread threadPad) {
        this.threadPad = threadPad;
    }
}
