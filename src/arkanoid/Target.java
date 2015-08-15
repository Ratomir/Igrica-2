/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
public class Target extends JPanel implements GameObject {

    //Duzina, visina izmedju pravougaonika.
    private float WIDTH = 81;
    private float HEIGHT = 26;

    public RoundRectangle2D.Float ellipseForDrawing = null;

    //lokacija pravougaonika
    float locationX;
    float locationY;

    //boja za pravougaonik
    private Color color = null;

    Image img1 = Toolkit.getDefaultToolkit().getImage("src/img/question.png");

    private Boolean image = false;
    private Boolean bad = false;

    private int upDown = 1;

    /**
     * Konstruktor koji na osnovu koordinata kreira zakrivljeni pravougaonik.
     *
     * @param x x koordinata na kojoj se iscrtava pravougaonik
     * @param y y koordinata na kojoj se iscrtava pravougaonik
     */
    public Target(int x, int y) {
        locationX = x;
        locationY = y;

        this.setLocation(x, y);
        this.setSize((int) WIDTH, (int) HEIGHT);

        this.setOpaque(false);

        this.ellipseForDrawing = new RoundRectangle2D.Float(0, 0, getWIDTH() - 1, getHEIGHT() - 1, 10, 10);

        Random random = new Random();
        int numberOfColor = random.nextInt(3); //biramo jedan slucajan broj do 3 i na osnovu njega stavljamo boju za pravougaonik

        if (numberOfColor == 1) {
            color = Color.LIGHT_GRAY;
        } else if (numberOfColor == 2) {
            color = Color.BLUE;
        } else {
            color = Color.GREEN;
        }
    }

    /**
     * Metoda vrši smanjivanje visine samo zelenih meta.
     */
    @Override
    public void move() {
        if (getWIDTH() == 81) {
            upDown = -1;
        } else if (getWIDTH() == 75) {
            upDown = 1;
        }

        this.setWIDTH(getWIDTH() + upDown);
        this.setHEIGHT(getHEIGHT() + upDown);
    }

    /**
     * Metoda vrši iscrtavanje svake mete na polju za igru.
     * 
     * @param g objekat za crtanje
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
        if (getImage()) {
            g2.setPaint(Color.WHITE);
            g2.fill(ellipseForDrawing);

            g2.drawImage(img1, (int) getWIDTH() / 2 - 8, 4, 16, 16, this);
        } else {

            if (this.getColor() == Color.GREEN) {
                move();
            }

            g2.setPaint(getColor());
            g2.fill(ellipseForDrawing);
        }

        this.ellipseForDrawing = new RoundRectangle2D.Float(0, 0, getWIDTH() - 1, getHEIGHT() - 1, 10, 10);

        g2.setPaint(Color.BLACK);
        g2.draw(ellipseForDrawing);
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
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

    /**
     * @return the image
     */
    public Boolean getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Boolean image) {
        this.image = image;
    }

    /**
     * @return the WIDTH
     */
    public float getWIDTH() {
        return WIDTH;
    }

    /**
     * @param WIDTH the WIDTH to set
     */
    public void setWIDTH(float WIDTH) {
        this.WIDTH = WIDTH;
    }

    /**
     * @return the HEIGHT
     */
    public float getHEIGHT() {
        return HEIGHT;
    }

    /**
     * @param HEIGHT the HEIGHT to set
     */
    public void setHEIGHT(float HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    /**
     * @return the bad
     */
    public Boolean getBad() {
        return bad;
    }

    /**
     * @param bad the bad to set
     */
    public void setBad(Boolean bad) {
        this.bad = bad;
    }
}
