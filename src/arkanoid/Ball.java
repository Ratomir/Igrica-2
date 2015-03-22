package arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

/**
 *
 * Klasa Ball nasleđuje klasu Rectangle.Double i implementira interface GameObject.
 * 
 * @author Ratomir
 */
public class Ball extends Rectangle.Double implements GameObject {
    private final int w = 20;
    private final int h = 20;
    
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
    private Ellipse2D.Double ellipseForDrawing;
    
    private Color fillColor = Color.RED;
    private Color borderColor = Color.BLACK;

    /**
     * Inicijalizuje loptu na tabli.
     * Postavlja lopticu odmah iznad reketa i postavlja brzinu na minimum.
     * 
     * @param board Tabla kojoj lopta pripada.
     */
    public Ball(Board board) {
        this.board = board;
        directionX = 1;
        directionY = 1;
        width = w;
        height = h;
    }
    
    /**
     * Menja smer lopte po x osi.
     */
    public void bouceHorizontal() {
        directionX = -directionX;
    }
    
    /**
     * Menja smer lopte po y osi.
     */
    public void bouceVertical() {
        directionY = -directionY;
    }
    
    /**
     * Resetuje poziciju lopte i postavlja je na pocetnu poziciju.
     */
    public void reset() {
        x = Board.PANEL_WIDTH/2 - w/2;
        y = Board.PANEL_HEIGHT - Pad.h - h;
        
        dx = DX;
        dy = DY;
    }
    
    /**
     * Vrsi pomeranje lopte.
     * Ispituje poziciji lopte.
     */
    public void move() 
    {
        x += dx * directionX;
        y += dy * directionY;
        
        /*Ako je lokacija od lopte plus njena duzina veca ili jednaka duzini panela ili
        ako je lokacija lopte manja od 0, vrsi pomeranje horizontalno. */
        if (x + w >= board.PANEL_WIDTH || x <= 0)
            bouceHorizontal();
        
        //Ako je lopta prosla pored reketa vrsi se smanjivanje broja zivota.
        if (y + w >= board.PANEL_HEIGHT) 
        {
            board.setNumberOfLife(board.getNumberOfLife() - 1);
            
            if(board.getNumberOfLife() == 0) //testiranje na poslednji zivot
            {
                board.stopGame("IGRICA GOTOVA, ZDRAVO!");
            }
            else
            {
                reset();
                bouceVertical();
            }
        }
        
        /*
        Slucaj kada je loptica dosla do vrha prozora.
        */
        if(y <= 0)
        {
            bouceVertical();
        }
    }
    
    /**
     * Vrsi iscrtavanje lopte na tabli.
     * @param g2 Graphics2D objekat na kojem se vrsi iscrtavanje.
     */
    public void draw(Graphics2D g2) 
    {
        ellipseForDrawing = new Ellipse2D.Double(x, y, w, h);
        
        g2.setPaint(fillColor); //postavljamo boju
        g2.fill(ellipseForDrawing); //sa postavljenom bojom filujemo objekat
        
        g2.setPaint(borderColor); //postavljamo boju
        g2.draw(ellipseForDrawing); //crtamo lopticu
    }
}
