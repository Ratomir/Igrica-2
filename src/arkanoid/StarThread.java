/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.util.ArrayList;

/**
 *
 * @author Ratomir
 */
public class StarThread implements Runnable {

    private ArrayList<Star> listStart = null;

    private Thread thread;

    private Board board;

    public StarThread(Board board) {

        this.board = board;

        this.listStart = new ArrayList<>();

        this.thread = new Thread(this, "Star thread");
        this.thread.start();
    }

    public void restartStar() {
        if (this.getListStart() != null && this.getListStart().size() > 0) {
            for (Star star : this.getListStart()) {

                this.getBoard().remove(star);
            }
        }

        this.setListStart(null);
        this.setListStart(new ArrayList<>());
    }

    @Override
    public void run() {
        while (true) {

            if (Board.gameState == Board.GameState.PLAY && getListStart() != null && getListStart().size() > 0) {
                for (Star star : getListStart()) {
                    star.repaint();
                }
            }

            try {
                Thread.sleep(30); //pauziramo izvrsavanje programa
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * @return the listStart
     */
    public ArrayList<Star> getListStart() {
        return listStart;
    }

    /**
     * @param listStart the listStart to set
     */
    public void setListStart(ArrayList<Star> listStart) {
        this.listStart = listStart;
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
}
