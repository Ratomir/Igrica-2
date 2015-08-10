/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arkanoid;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Ratomir
 */
public class NextLevel extends JPanel{
    
    private String message;
    
    private JButton btnNext;
    public Board board;
    
    public NextLevel(Board board)
    {
        this.board = board;
        
        setLayout(new BorderLayout(10,10));
        
        btnNext = new JButton("Sljedeći nivo");
            
//            btnNext.setBorderPainted(false);
//            btnNext.setOpaque(false);
//            btnNext.setBackground(null);
            
        this.add(btnNext, BorderLayout.SOUTH);
        btnNext.setPreferredSize(new Dimension(150, 50));
        btnNext.addActionListener(new NextController());
        
        this.setPreferredSize(new Dimension(350, 350));
        this.setSize(350, 350);
        this.setLocation(100, 100);
    }
    
    public class NextController implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            board.newLevel();
        }
    }
    
}
