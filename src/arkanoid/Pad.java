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
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author Ratomir
 */
class Pad extends JPanel implements GameObject, Runnable
{
    enum MovingState { STANDING, MOVING_LEFT, MOVING_RIGHT }
    
    /**
     * Sirina reketa
     */
    public static final int w = 100;
    /**
     * Visina reketa
     */
    public static final int h = 20;
    
    private int dx = 10;
    
    private Board board;
    private MovingState state;
    
    private Color fillColor = Color.BLACK;
    
    private Point position;
    RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float();
    
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
    }
    
    /**
     * Postavlja stanje kretanja reketa u desno.
     */
    public void moveRight() {
        state = MovingState.MOVING_RIGHT;
    }
    
    /**
     * Postavlja stanje kretanja reketa u levo.
     */
    public void moveLeft() {
        state = MovingState.MOVING_LEFT;
    }
    
    /**
     * Postavlja stanje kretanja reketa u stajanje.
     */
    public void stopMoving() {
        state = MovingState.STANDING;
    }
    
    /**
     * Izvrsava pomeranje reketa u zavisnosti od stanja.
     */
    public void move() {
        position = this.getLocation();
        
        int tempX = (int)position.getX();
        int tempY = (int)position.getY();
        
        if (state == MovingState.MOVING_RIGHT)
            tempX += dx;
        else if (state == MovingState.MOVING_LEFT)
            tempX -= dx;
        
        if (tempX < 0) //slucaj kada je skroz levo reket
            tempX = 0;
        else if (tempX + this.getSize().width > board.PANEL_WIDTH) //slucaj kada je skoz desno
            tempX = board.PANEL_WIDTH - this.getSize().width;
        
        this.setLocation(tempX, tempY);
    }
    
    /**
     * Funkcija vrsi resetovanje koordinata za iscrtavanje reketa.
     */
    public void reset()
    {
        this.setLocation(board.PANEL_WIDTH/2 - Pad.w/2, Board.PANEL_HEIGHT-Pad.h-2);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        if (this.board.inGame) 
        {
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
        roundedRectangle = new RoundRectangle2D.Float(1, 1, w, h, 2, 2);
        
        g2.setPaint(fillColor);
        g2.fill(roundedRectangle);
        g2.draw(roundedRectangle); //crtamo lopticu
    }
    
    @Override
    public void run() {
        
        while(true) 
        {
            move();
            repaint();
            
            try {
                Thread.sleep(30); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }
}
