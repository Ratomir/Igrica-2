/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import static arkanoid.Board.Y_SPACE_TARGET;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Ratomir
 */
public class TargetThread implements Runnable{

    private ArrayList<Target> listTargets = null;

    private Thread thread;
    private Board board;
    private Ball ball;
    private Pad pad;
    
    private Boolean startCollision = false;
    
    public TargetThread(Board board, Ball ball, Pad pad){
        generateTargets();
        
        this.ball = ball;
        this.board = board;
        this.pad = pad;
        
        thread = new Thread(this);
    }
    
    @Override
    public void run() {
        while(this.getStartCollision()) 
        {
            detectCollision();
            
            try {
                Thread.sleep(30); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
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
            /*
            Prolazimo kroz sve objekte meta i ispituje da li se poklapaju sa lopticom.
            U slucaju poklapanja testiramo koja je boja i u zavisnosti toga dodeljujemo odredjen broj bodova.
            Kasnije se pogodjena meta uklanja iz liste.
            */

            for (int i = 0; i < this.listTargets.size(); i++)
            {
                Target tempTarget = this.listTargets.get(i);

                if (tempTarget.getBounds().intersects(ball.getBounds()))
                {
                    if(tempTarget.getColor() == Color.LIGHT_GRAY)
                    {
                        this.board.countScore(1);
                    }
                    else if(tempTarget.getColor() == Color.BLUE)
                    {
                        this.board.countScore(2);
                    }
                    else
                    {
                        this.board.countScore(3);
                    }
                    
                    this.board.remove(this.listTargets.get(i));
                    this.board.invalidate();
                    
                    this.listTargets.remove(i);
                    ball.bouceVertical();
                }
            }

            //U slucaju da je pogodjena zadanja meta, zaustavlja se igrica i korisniku se cestita na pobedi.
            if(this.listTargets.size() == 0)
            {
                this.board.newLevelMessage("Čestitamo!!! Prošli ste " + this.board.getLevel() + ". Vaš skor je " + this.board.getMyScore() + ".");
                int number = this.board.getLevel();
                this.board.setLevel(number++);
            }
        }
    }
    /**
     * Funkcija generise mete sa lokaciom i smesta ih u listu.
     */
    public void generateTargets()
    {
         //Lista meta
        setListTargets(new ArrayList<Target>());
        
        drawOne();
    }
    
    public void drawOne()
    {
        int yLocal = 50;
        
        int xLocal = 50;
        for (int i = 0; i < 1; i++, xLocal += 125)
        {
            getListTargets().add(new Target(xLocal, yLocal));
        }

//        xLocal = 50;
//        yLocal += Y_SPACE_TARGET;
//        for (int i = 6; i < 12; i++, xLocal += 125)
//        {
//            getListTargets().add(new Target(xLocal, yLocal));
//        }
//
//        xLocal = 50;
//        yLocal += Y_SPACE_TARGET;
//        for (int i = 12; i < 18; i++, xLocal += 125)
//        {
//            getListTargets().add(new Target(xLocal, yLocal));
//        }
//
//        xLocal = 50;
//        yLocal += Y_SPACE_TARGET;
//        for (int i = 18; i < 24; i++, xLocal += 125)
//        {
//            getListTargets().add(new Target(xLocal, yLocal));
//        }
    }
    
    public void drawSecound()
    {
        int yLocal = 50;
        
        int xLocal = 150;
        for (int i = 0; i < 4; i++, xLocal += 125)
        {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 100;
        yLocal += Y_SPACE_TARGET;
        for (int i = 4; i < 9; i++, xLocal += 125)
        {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 50;
        yLocal += Y_SPACE_TARGET;
        for (int i = 9; i < 15; i++, xLocal += 125)
        {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 100;
        yLocal += Y_SPACE_TARGET;
        for (int i = 15; i < 20; i++, xLocal += 125)
        {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 150;
        yLocal += Y_SPACE_TARGET;
        for (int i = 20; i < 24; i++, xLocal += 125)
        {
            getListTargets().add(new Target(xLocal, yLocal));
        }
    }

    /**
     * @return the listTargets
     */
    public ArrayList<Target> getListTargets() {
        return listTargets;
    }

    /**
     * @param listTargets the listTargets to set
     */
    public void setListTargets(ArrayList<Target> listTargets) {
        this.listTargets = listTargets;
    }

    /**
     * @return the thread
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * @param thread the thread to set
     */
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    /**
     * @return the startCollision
     */
    public Boolean getStartCollision() {
        return startCollision;
    }

    /**
     * @param startCollision the startCollision to set
     */
    public void setStartCollision(Boolean startCollision) {
        this.startCollision = startCollision;
    }
    
}
