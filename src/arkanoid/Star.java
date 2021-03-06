/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Klasa vrši instanciranje jedne zvjezdice sa sličicom.
 *
 * @author Ratomir
 */
public class Star extends JPanel implements GameObject {

    //Duzina, visina izmedju pravougaonika.
    private float WIDTH = 18;
    private float HEIGHT = 18;

    //lokacija pravougaonika
    float locationX;
    float locationY;

    URL img1URL = getClass().getClassLoader().getResource("img/star.png");
    Image img1 = new ImageIcon(img1URL).getImage();

    /**
     * Konstruktor koji na osnovu koordinata kreira zvjezdicu.
     *
     * @param x x koordinata na kojoj se iscrtava zvjezdica
     * @param y y koordinata na kojoj se iscrtava zvjezdica
     */
    public Star(int x, int y) {
        locationX = x;
        locationY = y;

        this.setLocation(x, y);
        this.setSize((int) WIDTH, (int) HEIGHT);

        this.setOpaque(false);
    }

    @Override
    public void move() {
    }

    /**
     * Funkcija vrši iscrtavanje zvjezdice
     *
     * @param g objekat za iscrtvanje
     */
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
     * Funkcija vrsi iscrtavanje pravougaonika za metu.
     *
     * @param g2 Graphics2D objekat na kome se vrsi iscrtavanje.
     */
    @Override
    public void draw(Graphics2D g2) {

        g2.drawImage(img1, 2, 2, this);
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
