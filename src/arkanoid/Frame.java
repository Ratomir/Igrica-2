/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Klasa koja nasleđuje JFrame komponentu. Kreira board i osnovne postavke
 * prozora.
 *
 * @author Ratomir
 */
class Frame extends JFrame {

    Board board = new Board();

    /**
     * Osnovni konstruktor koji inicijalizuje prozor i postavlja meni.
     */
    public Frame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }

        add(board);
        
        addWindowListener(new WindowController());

        setJMenuBar(initMenu());
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Arkanoid");

        this.setIconImage(new ImageIcon("src/img/icon.png").getImage());

        setVisible(true);
    }

    /**
     * Funkcija kreira meni bar sa jednom stavkom new game.
     *
     * @return menuBar
     */
    final JMenuBar initMenu() {
        // Napravimo liniju menija
        JMenuBar menuBar = new JMenuBar();

        // Napravimo meni
        JMenu gameMenu = new JMenu("Game");

        // Napravimo stavku za meni
        JMenuItem newGame = new JMenuItem("New game");

        /*
         Dodajemo osluskivac na newGame meni stavku.
         */
        newGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                board.startGame();
            }
        });

        // Dodamo stavku u meni
        gameMenu.add(newGame);

        // Dodamo meni u liniju menija
        menuBar.add(gameMenu);

        return menuBar;
    }
    
}
