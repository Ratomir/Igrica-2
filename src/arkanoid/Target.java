/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;
import javax.swing.JPanel;

/**
 * Klasa Target implementira interface GameObject.
 * 
 * @author Ratomir
 */
public class Target extends JPanel implements GameObject
{
    //Duzina, visina izmedju pravougaonika.
    public static final float WIDTH = 81;
    public static final float HEIGHT = 26;
    
    public RoundRectangle2D.Float ellipseForDrawing = null;
    
    //lokacija pravougaonika
    float locationX;
    float locationY;
    
    //boja za pravougaonik
    private Color color = null;
    
    /**
     * Konstruktor koji na osnovu koordinata kreira zakrivljeni pravougaonik.
     * 
     * @param x x koordinata na kojoj se iscrtava pravougaonik
     * @param y y koordinata na kojoj se iscrtava pravougaonik
     */
    public Target(int x, int y)
    {
        locationX = x;
        locationY = y;
        
        this.setLocation(x, y);
        this.setSize((int)WIDTH, (int)HEIGHT);
        
        this.ellipseForDrawing = new RoundRectangle2D.Float(0, 0, WIDTH-1, HEIGHT-1, 10, 10);
        
        Random random = new Random();
        int numberOfColor = random.nextInt(3); //biramo jedan slucajan broj do 3 i na osnovu njega stavljamo boju za pravougaonik
        
        if(numberOfColor == 1)
        {
            color = Color.LIGHT_GRAY;
        }
        else if(numberOfColor == 2)
        {
            color = Color.BLUE;
        }
        else
        {
            color = Color.GREEN;
        }
    }
    
    @Override
    public void move()
    {
    }
    
     @Override
    public void paint(Graphics g)
    {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        if (Board.inGame) 
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
     * Funkcija vrsi iscrtavanje pravougaonika za metu.
     * 
     * @param g2 Graphics2D objekat na kome se vrsi iscrtavanje.
     */
    @Override
    public void draw(Graphics2D g2)
    {
        g2.setPaint(getColor());
        g2.fill(ellipseForDrawing);
        
        g2.setPaint(Color.BLACK);
        g2.draw(ellipseForDrawing);
    }

    /**
     * @return the color
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color)
    {
        this.color = color;
    }

    @Override
    public void terminateThread() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startThread() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
