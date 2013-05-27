/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vlak;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author littlehill
 */
public class Creator extends JPanel implements ActionListener {

    
    private Vlak menu;
    private int pointerB;
    
    private Timer timer;
    private String fileName = null;
    private Graphics graphics_local;

    private Train train;
    private Table table;
    private Block activeB;
    private Table availableB;
    
    private boolean helpEnabled = true;
    private Block startB = null;
    
    public Creator(Vlak inMenu) {
        addKeyListener(new Creator.TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        this.menu = inMenu;
        initGame();
    }


    public void initGame() {
        Image pointer = new ImageIcon(this.getClass().getResource("./textures/pointer.png")).getImage();    
        train = new Train(new Position(4,4),pointer);
        /*
        String maplink = JOptionPane.showInputDialog(null, "Vlozte nazev mapy kterou chcete nacist:\n(pro novou mapu nechte prazdne)\n", "", 1);
        if (!maplink.isEmpty()){
            try {
                table.loadMap(maplink,"");
                }
            catch (Throwable e) { System.out.printf("loading map failed\n"); }        
            }*/
        
        table = new Table(32,20);
        table.fillEmpty();
        availableB = new Table(0,0);
            
        try {
        reloadBlockList("default.blocks"); }
        catch (Throwable e) { System.out.printf("stavebni bloky se nepodarilo nacist"); }
        pointerB = 0;
        activeB = availableB.getBlock(pointerB, 0);
        
        System.out.printf("DOOR: %d, moveable: %d train: %d, solid: %d none:%d",BlockType.DOOR.ordinal(),BlockType.MOVEABLE.ordinal(),BlockType.TRAIN.ordinal(),BlockType.SOLID.ordinal(),BlockType.NONE.ordinal());
    }

    

    public void paint(Graphics g) { //input train.paint
        super.paint(g);
        
            train.paint(g, this);
            table.paint(g, this);
            paintHelp(g,Constants.BLOCK_COUNT_H*Constants.BLOCK_H);
            
            if (startB!=null) { 
                g.drawImage(startB.getImage(), (Constants.BLOCK_H*startB.position.getx()), (Constants.BLOCK_H*startB.position.gety()), this);
                }
            
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
    }
    
    public void exit() {
        menu.status.gameState = 4;
    }

    public void paintHelp(Graphics g, int pozy) {
         String msg = "Active Block:";
         Font small = new Font("Helvetica", Font.BOLD, 14);
         FontMetrics metr = this.getFontMetrics(small);
         int text_y = pozy+metr.getHeight()+2;
         int text_x = 10;
         g.drawLine(0, pozy, Constants.BLOCK_COUNT_W*Constants.BLOCK_W,pozy);
         g.setColor(Color.white);
         g.drawLine(0, pozy+1, Constants.BLOCK_COUNT_W*Constants.BLOCK_W,pozy+1);
         
         g.setFont(small);
         g.drawString(msg, text_x, text_y);
         text_x = metr.stringWidth(msg)+20;
         g.drawImage(activeB.getImage(),text_x,pozy , this);
         text_x+=32;
         g.drawLine(text_x, pozy, text_x, pozy+32);
         text_x+=10;
         msg = String.format("BlockType: "+activeB.type); 
         g.drawString(msg, text_x, text_y);
         text_x=355;
         g.drawLine(text_x, pozy, text_x, pozy+32);
         text_x+=5;
         
         msg = "F4 = save map"; 
         g.drawString(msg, text_x, text_y);
         text_x+=metr.stringWidth(msg)+10;
         g.drawLine(text_x, pozy, text_x, pozy+32);
         text_x+=5;
         msg = "F5 = place start"; 
         g.drawString(msg, text_x, text_y);
         text_x+=metr.stringWidth(msg)+10;
         g.drawLine(text_x, pozy, text_x, pozy+32);
         text_x+=5; 
         msg = "F2 = extended help"; 
         g.drawString(msg, text_x, text_y);
         text_x+=metr.stringWidth(msg)+10;
         g.drawLine(text_x, pozy, text_x, pozy+32);
         text_x+=5;
         msg = "F1 = show/hide info"; 
         g.drawString(msg, text_x, text_y);
         text_x+=metr.stringWidth(msg)+10;
         g.drawLine(text_x, pozy, text_x, pozy+32);
         text_x+=5;
    }
        
    public void actionPerformed(ActionEvent e) {

    }
    
    public void reloadBlockList(String setName) {
        System.out.printf("new blocklist\n");
        availableB.loadMap(setName, menu.status.homePath+"blocks/");
        Block deleteB = new Block(1,1,Constants.BLOCK_W,Constants.BLOCK_H,new ImageIcon(this.getClass().getResource("./textures/destroy.png")).getImage(),"destroy.png");
        deleteB.type = BlockType.NONE;
        availableB.addBlock(deleteB);
        }
    
    public boolean exportMap() {
        if (!table.isDoorPlaced()) {
            JOptionPane.showMessageDialog(null, "Map can not be saved without door.\nPlease place a DOOR block into the map.\n","Problem detected", JOptionPane.WARNING_MESSAGE);
            return false;
            }
        if (fileName==null) { fileName = JOptionPane.showInputDialog(null, "*.map", "Zadejte jmeno noveho souboru:", 1); }
        File mapf = new File("./src/Vlak/maps/"+fileName);
        PrintWriter fout;
        FileWriter fWrite;
                       
        try {
        if (!mapf.exists()) { 
                mapf.createNewFile(); 
                JOptionPane.showMessageDialog(null, "Info","New file created", JOptionPane.INFORMATION_MESSAGE);
                }
        }
        catch (Throwable e) { 
                JOptionPane.showMessageDialog(null, "Error","New file creation failed\nbecause: "+e.getMessage(), JOptionPane.INFORMATION_MESSAGE); return false;}
        try {
        fWrite = new FileWriter(mapf,false);
        fout = new PrintWriter(fWrite);
        } catch (Throwable e) { JOptionPane.showMessageDialog(this, "Error","Unable to create file write stream\nbecause: "+e.getMessage(), JOptionPane.INFORMATION_MESSAGE); return false;}
        
        if (startB!=null) {
        System.out.printf("print start: %d %d\n", startB.position.getx(),startB.position.gety());
        table.printMap(fout,startB.position); }
        
        fout.close();
        JOptionPane.showMessageDialog(null, "Map exported :)\n","Info", JOptionPane.INFORMATION_MESSAGE);
        menu.status.mapName = fileName;
        menu.status.gameType = 2;
        menu.reloadStrings();
        return true;
        }   
    

    private class TAdapter extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            
            Block tmp = train.head;
            int key = e.getKeyCode();
            
            
                if (key == KeyEvent.VK_LEFT) {
                    train.setDirection(Direction.LEFT); }

                if (key == KeyEvent.VK_RIGHT) {
                    train.setDirection(Direction.RIGHT); }

                if (key == KeyEvent.VK_UP) {
                    train.setDirection(Direction.UP); }

                if (key == KeyEvent.VK_DOWN) {
                    train.setDirection(Direction.DOWN); }
                train.move();
                train.setDirection(Direction.NONE);
        
                 if (key == KeyEvent.VK_SPACE) {
                     Block TB = availableB.getBlock(pointerB,0);
                     pointerB += 1;
                     if (pointerB==availableB.getSize()) { pointerB = 0; }
                     activeB = TB;
                     activeB.setType(TB.type);
                 }
        
                 if (key == KeyEvent.VK_F4) {
                     exportMap();
                 } 
                 
                 if (key == KeyEvent.VK_F2) {
                     JOptionPane.showMessageDialog(null, "| Control: ............\n| Change Active block: Space |\n| Place Active Block: Enter      |\n| Save map: F4                          |\n| Place start: F5                        |\n| Change block-set: F6            |\n\n Move: Arrow keys\nTip:\nYou should place at least\none door block.\n", "Extended help", 1);                 
                 }
                 
                 if (key == KeyEvent.VK_F1) {
                    if (helpEnabled) {
                        menu.gameFrame.setSize(1024, 673); helpEnabled=false; }
                    else { menu.gameFrame.setSize(1024, 705); helpEnabled=true; }               
                 }
                 
                 if (key == KeyEvent.VK_F5) {
                     if (startB!=null) {
                     startB.position.setxy(train.head.position.getx(), train.head.position.gety());
                     table.getBlock(train.head.position.getx(), train.head.position.gety()).type = BlockType.NONE;
                     }
                     else { 
                     startB = new Block(train.head.position.getx(),train.head.position.gety(),0,0,new ImageIcon(this.getClass().getResource("./textures/start.png")).getImage(),null);
                     }
                     //place start
                 }
                 
                 if (key == KeyEvent.VK_F6) {
                     reloadBlockList("add.blocks");
                 }
                 
                 if (key == KeyEvent.VK_ENTER) {
                     Position xyP = new Position(train.head.position.getx(),train.head.position.gety());
  
                     Block tempB = table.getBlock(xyP);
                     tempB = new Block(xyP,Constants.BLOCK_W,Constants.BLOCK_H,activeB.getImage(),activeB.getImageName(),activeB.getOpenedImage(),activeB.getOpenedImageName()); 
                     tempB.type = activeB.type;
                     table.setBlock(xyP, tempB);
                     System.out.print(table.getBlock(xyP).type.ordinal()+" <-type intable loaded\n");
                 }
                 
                 
        Creator crbox = (Creator)e.getSource(); 
        crbox.repaint();
        }
    
    }
    
}
