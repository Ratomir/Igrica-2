/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import static arkanoid.Board.Y_SPACE_TARGET;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa vrši detekciju preklpanja objekata u igri. Implementira interfejs
 * Runnable i upravlja igricom.
 *
 * @author Ratomir
 */
public class TargetThread implements Runnable {

    private static ArrayList<Ball> listBalls = null;

    private ArrayList<Target> listTargets = null;

    private Thread thread;
    private Board board;
    private Pad pad;

    private int yStarPosition = 200;

    private Boolean startCollision = false;

    private int hitTarget = 0;

    private StarThread starThread = null;

    /**
     * Konstruktor klase vrši inicijalizaciju parametara.
     *
     * @param board polje za igru
     * @param pad reket igrača
     * @param starThread zvjezdice
     */
    public TargetThread(Board board, Pad pad, StarThread starThread) {

        this.board = board;
        this.pad = pad;

        this.starThread = starThread;

        thread = new Thread(this, "Target thread");
        thread.start();
    }

    /**
     * Proces klase koji vrši detekciju.
     */
    @Override
    public void run() {
        while (true) {
            detectCollision();

            try {
                Thread.sleep(20); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * Funkcija vrši provjeru detekcije preklapanja bilo koje loptice za
     * reketom.
     */
    public void detectBallAndPad() {

        if (this.listBalls != null) {

            for (Ball listBall : this.listBalls) {
                if (listBall.getBounds().intersects(getPad().getBounds())) {
                    this.hitTarget = 0;
                    listBall.bouceVertical();
                }
            }
        }

    }

    /**
     * Funkcija detektuje poklapanje loptice sa reketom i loptice sa metom.
     */
    private void detectCollision() {
        if (Board.gameState == Board.GameState.PLAY) {

            detectBallAndPad();

            /*
             Prolazimo kroz sve objekte meta i ispituje da li se poklapaju sa lopticom.
             U slucaju poklapanja testiramo koja je boja i u zavisnosti toga dodeljujemo odredjen broj bodova.
             Kasnije se pogodjena meta uklanja iz liste.
             */
            Target tempTarget = null;
            Ball tempBall = null;

            for (int i = 0; i < this.listTargets.size(); i++) {
                tempTarget = this.listTargets.get(i);

                for (int j = 0; j < this.listBalls.size(); j++) {

                    tempBall = this.listBalls.get(j);

                    if (tempTarget.getBounds().intersects(tempBall.getBounds())) {

                        this.hitTarget++;

                        //Pogođene tri mete za redom, dodaje se zvjezdica
                        if (getHitTarget() == 3) {
                            setHitTarget(0);
                            Star newStar = new Star(5, yStarPosition);
                            yStarPosition += 20;
                            this.starThread.getListStart().add(newStar);

                            this.board.add(newStar);

                            this.board.setMainMessageAndTick("Nova zvjezdica! Super! :D");
                        }

                        if (tempTarget.getColor() == Color.LIGHT_GRAY) {
                            this.board.countScore(1);
                        } else if (tempTarget.getColor() == Color.BLUE) {
                            this.board.countScore(2);
                        } else if (tempTarget.getColor() == Color.WHITE) {

                            //Pogođena loša meta, usporava se reket
                            if (tempTarget.getBad()) {
                                this.pad.setSpeed(250);
                                this.pad.setTick(0);

                                this.board.setMainMessageAndTick("Reket je usporen na 2 sekunde.");

                                this.board.playSound("src/sounds/im-in-touble.wav");
                            } else {
                                //Pogođena dobra meta, dodaje se nova loptica

                                Ball newBall = new Ball(board);
                                newBall.reset();
                                this.listBalls.add(newBall);

                                if (this.listBalls.size() % 3 == 0) {
                                    this.board.playSound("src/sounds/yes-hahahaa.wav");
                                }

                                this.board.add(newBall);

                                this.listBalls.get(0).setSpeed(this.listBalls.get(0).getSpeed() - 10);

                                this.board.setMainMessageAndTick("Nova loptica! Super! :D");
                            }

                        } else {
                            this.board.countScore(3);
                        }

                        //Uklanjanje mete sa polja za igru
                        this.board.remove(this.listTargets.get(i));
                        this.board.invalidate();

                        this.listTargets.remove(i);
                        tempBall.bouceVertical();

                        break;
                    }
                }

            }

            //U slucaju da je pogodjena zadanja meta, zaustavlja se igrica i korisniku se cestita na pobedi.
            if (this.listTargets.isEmpty()) {
                this.board.newLevelMessage("Čestitamo!!! Prošli ste " + this.board.getLevel() + " level. Vaš skor je " + this.board.getMyScore() + ".\n\rPritisnite bilo koji taster za sljedeći nivo.");
                int number = this.board.getLevel();
                this.board.setLevel(++number);
            }

        }
    }

    /**
     * Metoda restertuje sve loptice koje su bile u igri.
     */
    public void restartBalls() {

        if (this.listBalls != null && this.listBalls.size() > 0) {
            for (Ball ball : this.listBalls) {

                ball.terminateThread();
//            ball.getThreadBall().interrupt();

                this.board.remove(ball);
            }
        }

        this.listBalls = null;
        this.listBalls = new ArrayList<>();

        Ball ball = new Ball(this.board);
        ball.reset();

        this.listBalls.add(ball);

        ball.startThread();

        this.board.add(ball);

//        if(!ball.getRunningBall())
//            this.getBall().getThreadBall().start();
    }

    /**
     * Funkcija generise mete sa lokaciom i smesta ih u listu.
     */
    public void generateTargets() {
        //Lista meta
        this.listTargets = null;
        setListTargets(new ArrayList<Target>());

        Random random = new Random();
        int numberOfColor = random.nextInt(3); //biramo jedan slucajan broj do 3 i na osnovu njega stavljamo boju za pravougaonik

        if (numberOfColor == 1) {
            drawOne();
        } else if (numberOfColor == 2) {
            drawSecound();
        } else {
            drawOne();
        }

        Target oneQuestionMark = listTargets.get(random.nextInt(listTargets.size()));
        oneQuestionMark.setImage(true);
        oneQuestionMark.setColor(Color.WHITE);

        int badSuprise = random.nextInt(2);

        if (badSuprise == 0) {
            oneQuestionMark.setBad(true);
        }

        oneQuestionMark = listTargets.get(random.nextInt(listTargets.size()));
        oneQuestionMark.setImage(true);
        oneQuestionMark.setColor(Color.WHITE);

        badSuprise = random.nextInt(2);

        if (badSuprise == 0) {
            oneQuestionMark.setBad(true);
        }
    }

    /**
     * Prvi način generisanja meta.
     */
    public void drawOne() {
        int yLocal = 50;

        int xLocal = 50;
        for (int i = 0; i < 6; i++, xLocal += 125) {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 50;
        yLocal += Y_SPACE_TARGET;
        for (int i = 6; i < 12; i++, xLocal += 125) {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 50;
        yLocal += Y_SPACE_TARGET;
        for (int i = 12; i < 18; i++, xLocal += 125) {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 50;
        yLocal += Y_SPACE_TARGET;
        for (int i = 18; i < 24; i++, xLocal += 125) {
            getListTargets().add(new Target(xLocal, yLocal));
        }
    }

    /**
     * Drugi način generisanja meta.
     */
    public void drawSecound() {
        int yLocal = 50;

        int xLocal = 150;
        for (int i = 0; i < 4; i++, xLocal += 125) {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 100;
        yLocal += Y_SPACE_TARGET;
        for (int i = 4; i < 9; i++, xLocal += 125) {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 50;
        yLocal += Y_SPACE_TARGET;
        for (int i = 9; i < 15; i++, xLocal += 125) {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 100;
        yLocal += Y_SPACE_TARGET;
        for (int i = 15; i < 20; i++, xLocal += 125) {
            getListTargets().add(new Target(xLocal, yLocal));
        }

        xLocal = 150;
        yLocal += Y_SPACE_TARGET;
        for (int i = 20; i < 24; i++, xLocal += 125) {
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

    /**
     * @return the pad
     */
    public Pad getPad() {
        return pad;
    }

    /**
     * @param pad the pad to set
     */
    public void setPad(Pad pad) {
        this.pad = pad;
    }

    /**
     * @return the listBalls
     */
    public ArrayList<Ball> getListBalls() {
        return listBalls;
    }

    /**
     * @param listBalls the listBalls to set
     */
    public void setListBalls(ArrayList<Ball> listBalls) {
        this.listBalls = listBalls;
    }

    /**
     * @return the hitTarget
     */
    public int getHitTarget() {
        return hitTarget;
    }

    /**
     * @param hitTarget the hitTarget to set
     */
    public void setHitTarget(int hitTarget) {
        this.hitTarget = hitTarget;
    }

}
