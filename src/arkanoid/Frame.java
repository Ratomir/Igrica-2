/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Klasa koja nasleđuje JFrame komponentu.
 * Kreira board i osnovne postavke prozora.
 * 
 * @author Ratomir
 */
class Frame extends JFrame
{
    Board board = new Board();
    
    /**
     * Osnovni konstruktor koji inicijalizuje prozor i postavlja meni.
     */
    public Frame()
    {
        add(board);
        
        setJMenuBar(initMenu());
        
        pack();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Arkanoid");
        
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
