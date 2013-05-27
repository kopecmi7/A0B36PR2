/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vlak;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author LittleHill
 */
public class GameFrame extends JFrame{
        private JPanel desk;
        private int gameType; //1=reg game, 2=creator
        
    public GameFrame(JPanel inDesk, int widthpx, int heightpx, int type) {
        super();
        this.setSize(widthpx,heightpx);
        this.desk = inDesk;
        this.add(desk);
        this.gameType = type;
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                
                if (gameType==1) {
                ((Board)desk).exit(); }
                else {
                ((Creator)desk).exit(); }
                
                System.out.print("window closed\n");
            }
        });
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }
    
    
    public void close() {
    WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSED);
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }
    
}
