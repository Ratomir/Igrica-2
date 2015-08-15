/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Klasa predstavlja kontroler za osnovni prozor, Frame.
 *
 * @author Ratomir
 */
public class WindowController implements WindowListener {

    @Override
    public void windowOpened(WindowEvent we) {
    }

    @Override
    public void windowClosing(WindowEvent we) {
    }

    @Override
    public void windowClosed(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent we) {
    }

    /**
     * Ako prozor nije u fokusu, igirca se pauzira.
     *
     * @param we Prozor nad kojim se vrši događaj.
     */
    @Override
    public void windowDeactivated(WindowEvent we) {
        if (Board.gameState != Board.GameState.INIT) {
            Board.gameState = Board.GameState.PAUSE;
        }
    }

}
