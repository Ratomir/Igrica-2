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
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * Klasa Board nasledjuje JPanel i implementira interface Runnable.
 * 
 * @author Ratomir
 */
class Board extends JPanel implements Runnable
{
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
    
    static Boolean inGame;
    
    // Objekti u igri
    
    private Ball ball;
    private Pad pad;
    
    private String message;
    
    //broj zivota
    private int numberOfLife;
    
    final Thread runner;
    private TargetThread targetThread;
    
    /**
     * Osnovni konstruktor koji postavlja osnovna podesavanja prozora za igricu.
     */
    public Board()
    {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);
        setFocusable(true);
        setLayout(null);
        setFont(getFont().deriveFont(Font.BOLD, 18f));
        setDoubleBuffered(true);
        
        inGame = false;
        message = "ARKANOID";
        
        ball = new Ball(this);
        
        pad = new Pad(this, PANEL_WIDTH/2 - Pad.getW()/2, PANEL_HEIGHT - Pad.getH());
        
        //dodajemo osluskivac na Board za tastaturu
        addKeyListener(new GameKeyAdapter());
        
        targetThread = new TargetThread(this, ball, pad);
        
        //dodajemo proces za board
        runner = new Thread(this);
        
        //startujemo proces
        runner.start();
    }
    
    /**
     * Funkcija postavlja parametre za pocetak igre.
     */
    public void startGame() 
    {
        
        if(this.targetThread.getListTargets().size() > 0)
        {
            for (int i = 0; i < this.targetThread.getListTargets().size(); i++) {
                this.remove(this.targetThread.getListTargets().get(i));
            }
        }
        
        setMyScore(0);
        inGame = true;
        
        setNumberOfLife(5);
        
        targetThread.generateTargets();
        
        this.add(ball);
        this.add(pad);
        
        int numberOfTargets = targetThread.getListTargets().size();
        
        for (int i = 0; i < numberOfTargets; i++) {
            this.add(targetThread.getListTargets().get(i));
        }
        
        if(this.targetThread.getStartCollision() == false)
        {
            this.targetThread.setStartCollision(true);
            targetThread.getThread().start();
        }
        
        ball.reset();
        
        pad.reset();
        
        
    }
    
    /**
     * Funkcija postavlja parametre za stopiranje igre.
     * 
     * @param message Poruka koja se ispisuje na ekran.
     */
    public void stopGame(String message) 
    {
        inGame = false;
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
     * Funckija vrsi iscrtavanje osnovnih parametara prozora, kao sto su loptica, reket, poruke za bodove i zivote, kao i mete.
     * 
     * @param g Paramtar koji sluzi za dohvatanje funkcija za iscrtavanje.
     */
    @Override
    public void paint(Graphics g) 
    {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        if (inGame) 
        {
            // Saveti pri iscrtavanju
        
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Iscrtaj rezultat

            g2.drawString("" + getMyScore(), 10, 20);
            
            //Iscrtaj broj zivota
            
            g2.drawString("Broj života: " + getNumberOfLife(), PANEL_WIDTH-160, 20);

            // Sinhronizovanje sa grafickom kartom
            Toolkit.getDefaultToolkit().sync();

            // Optimizacija upotrebe RAM-a, 
            g.dispose();
        } else {
            int messageWidth = getFontMetrics(getFont()).stringWidth(message);
            g2.drawString(message, PANEL_WIDTH/2 - messageWidth/2, PANEL_HEIGHT/2);
        }
    }
    
    /**
     * Metoda vrsi azuriranje loptice i reketa.
     */
    private void update() 
    {
        //pad.move();
    }
    
    /**
     * Funkcija detektuje poklapanje loptice sa reketom i loptice sa metom.
     */
    private void detectCollision()
    {
        if (ball.getBounds().intersects(pad.getBounds())) //ako se loptica poklapa sa reketom
        {
            ball.bouceVertical();
        }
        else
        {
            Rectangle2D ballBounds = ball.getBounds();

            /*
            Prolazimo kroz sve objekte meta i ispituje da li se poklapaju sa lopticom.
            U slucaju poklapanja testiramo koja je boja i u zavisnosti toga dodeljujemo odredjen broj bodova.
            Kasnije se pogodjena meta uklanja iz liste.
            */

            int numberOfTargets = targetThread.getListTargets().size();

            for (int i = 0; i < numberOfTargets; i++)
            {
                Target tempTarget = targetThread.getListTargets().get(i);

                if (tempTarget.getBounds().intersects(ball.getBounds()))
                {
                    if(tempTarget.getColor() == Color.LIGHT_GRAY)
                    {
                        countScore(1);
                    }
                    else if(tempTarget.getColor() == Color.BLUE)
                    {
                        countScore(2);
                    }
                    else
                    {
                        countScore(3);
                    }

                    this.targetThread.getListTargets().remove(i);
                    ball.bouceVertical();
                }
            }

            //U slucaju da je pogodjena zadanja meta, zaustavlja se igrica i korisniku se cestita na pobedi.
            if(numberOfTargets == 0)
            {
                stopGame("Čestitamo pobedili ste, Vaš skor je " + this.getMyScore() + ".");
            }
        }
        
        
    }
    
    /**
     * Funkcija pokrece proces za board.
     */
    @Override
    public void run()
    {
        while(true) 
        {
            //update();
            
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
    public int getNumberOfLife()
    {
        return numberOfLife;
    }

    /**
     * @param numberOfLife the numberOfLife to set
     */
    public void setNumberOfLife(int numberOfLife)
    {
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

    private class GameKeyAdapter extends KeyAdapter
    {
        /**
         * Funkcija se izvrsava na svako pritisnuto dugme sa tastature.
         * 
         * @param e Parametar koji nosi podatke o dugmetu koje je pozvalo osluskivac.
         */
        @Override
        public void keyPressed(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            
            if (keyCode == KeyEvent.VK_LEFT) //dugme lijevo
                pad.moveLeft();
            else if (keyCode == KeyEvent.VK_RIGHT) //dugme desno
                pad.moveRight();
        }

        /**
         * Funkcija se izvršava kada se dugme otpusti.
         * 
         * @param e Parametar koji nosi podatke o dugmetu koje ga je pozvalo.
         */
        @Override
        public void keyReleased(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            
            if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT)
                pad.stopMoving();
        }
    }
}
