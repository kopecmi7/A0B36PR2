/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vlak;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author littlehill
 */
public class ButtonInterface implements ActionListener {
    
    private int id;
    private Vlak mainwin;
    
    public ButtonInterface(int inid, Vlak hlavniokno) {
    this.id = inid;    
    this.mainwin = hlavniokno;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        switch (this.id) {
        
            case 1:
                this.mainwin.runGame();
                break;
            case 2:
                 String fName = JOptionPane.showInputDialog(null, "*.map", "Vlozte nazev mapy:", 1);
                 
                 File mapF = new File(this.mainwin.status.homePath+"maps/"+fName);
                 if (mapF.exists()) { 
                    this.mainwin.status.mapName = fName;
                    this.mainwin.status.gameType = 2;        
                    this.mainwin.reloadStrings();
                    JOptionPane.showMessageDialog(null, "Mapa nalezena :)", "Succes", 1);
                    }
                 else {
                    JOptionPane.showMessageDialog(null, "Mapa zadaneho nazvu nenalezena. :(", "Error", 1);
                 }
                 
                break;
            case 3:
                this.mainwin.runCreator("");
                break;
            case 4:
                this.mainwin.refresh.stop();
                this.mainwin.close();
                break;
        }
    }
    
}
