/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Ratomir
 */
class Pad extends Rectangle.Double implements GameObject 
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
    
    /**
     * Inicijalizuje reket na prosedjenoj lokaciji na tabli.
     * 
     * @param board Tabla kojoj reket pripada.
     * @param x x koordinata gornjeg levog temena reketa.
     * @param y y koordinata gornjeg leveg temena reketa.
     */
    public Pad(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
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
        if (state == MovingState.MOVING_RIGHT)
            x += dx;
        else if (state == MovingState.MOVING_LEFT)
            x -= dx;
        
        if (x < 0) //slucaj kada je skroz levo reket
            x = 0;
        else if (x + w > board.PANEL_WIDTH) //slucaj kada je skoz desno
            x = board.PANEL_WIDTH - w;
    }
    
    /**
     * Funkcija vrsi resetovanje koordinata za iscrtavanje reketa.
     */
    public void reset()
    {
        this.x = board.PANEL_WIDTH/2 - Pad.w/2;
    }
    
    /**
     * Iscrtava reket na tabli.
     * 
     * @param g2 Graphics2D objekat na kome se vrsi iscrtavanje.
     */
    public void draw(Graphics2D g2) {
        g2.setPaint(fillColor);
        g2.fill(this);
    }
}
