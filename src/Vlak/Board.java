package Vlak;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {

    private final int DELAY = 450;

    private int apple_x;
    private int apple_y;

    private boolean inGame = true;
    private boolean gameActive = true;
    private boolean win = false;
    
    Vlak menu;
     
    private Timer timer;
    
    Graphics graphics_local;

    private Train train;
    private Table table;
    
    public Board(Vlak inMenu) {
        addKeyListener(new Board.TAdapter());

        setBackground(Color.black);
        
        setFocusable(true);
        this.menu = inMenu;
        initGame();
    }


    public void initGame() {
        
        table = new Table(32,32);
        table.setDoorOpened(false);
            try {
                table.loadMap(menu.status.mapName,(menu.status.homePath+"maps/"));
                }
            catch (Throwable e) { System.out.printf("loading map failed\n"); }        
        
        train = new Train(table.getStartP());
            
        timer = new Timer(DELAY, this);
        timer.start();
    }


    public void paint(Graphics g) { //input train.paint
        super.paint(g);
        
        if ((inGame)&&(gameActive)) {
                        
            train.paint(g, this);
            table.paint(g, this);
            this.paintHelp(g, Constants.BLOCK_H*Constants.BLOCK_COUNT_H);
            
            Toolkit.getDefaultToolkit().sync();
            g.dispose();

        } else {
            if (gameActive) gameOver(g);
        }
    }

    public void paintHelp(Graphics g, int pozy) {
         String msg = "";
         Font small = new Font("Helvetica", Font.BOLD, 14);
         FontMetrics metr = this.getFontMetrics(small);
         int text_y = pozy+metr.getHeight()+2;
         int text_x = 10;
         g.drawLine(0, pozy, Constants.BLOCK_COUNT_W*Constants.BLOCK_W, pozy);
         
         g.setColor(Color.white);
         g.drawLine(0, pozy+1, Constants.BLOCK_COUNT_W*Constants.BLOCK_W, pozy+1);
         g.setFont(small);
         
         msg = "Space = pause"; 
         g.drawString(msg, text_x, text_y);
         text_x+=metr.stringWidth(msg)+10;
         g.drawLine(text_x, pozy, text_x, pozy+32);
         text_x+=10;
         
         
         msg = "F2 = extended help"; 
         g.drawString(msg, text_x, text_y);
         text_x+=metr.stringWidth(msg)+10;
         g.drawLine(text_x, pozy, text_x, pozy+32);
         text_x+=5;
         msg = "F1 = show/hide info"; 
         g.drawString(msg, text_x, text_y);
         text_x+=metr.stringWidth(msg)+10;
         g.drawLine(text_x, pozy, text_x, pozy+32);
         text_x+=10;
         
    }

    public void gameOver(Graphics g) {
        String msg = "Game Over";
        if (!win) { msg += " - you LOST"; 
                    menu.status.gameState = 4; }
        else { msg += " - you WON!"; menu.status.won = true; menu.status.gameState=4; }
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2,
                     HEIGHT / 2);
        return;
        
    }


    public void move() { //train.move
        train.move();
    }
    
    public void exit() {
        this.timer.stop();
        this.menu.status.gameState = 4;
        System.out.printf("board exit called\n");
    }
    
    public void checkCollision() {

        inGame = train.checkColision();
        
        Block tmp = table.getBlock(train.head.position.getx(),train.head.position.gety());
        //System.out.print(tmp.type+"\n");
        if ((tmp.type!=BlockType.NONE)&&(tmp.type!=BlockType.TRAIN)) {
            if (tmp.type==BlockType.MOVEABLE) { 
                train.addItem(tmp); 
                if (table.isAllColected()) { table.setDoorOpened(true); }
                }
            if (tmp.type==BlockType.SOLID) { inGame = false; }
            if (tmp.type==BlockType.DOOR) {
                if (table.getDoorOpened()) {
                inGame = false;
                win=true;    
                if (menu.status.gameType==1) {
                    JOptionPane.showMessageDialog(null,"Congratulations !\n-- Level UP --\n" );//add  level code: xxx-xxx
                    }
                else {
                    JOptionPane.showMessageDialog(null,"Congratulations !\n-- Map completed --\n" );//add  level code: xxx-xxx
                    }
                
                }
                else { inGame = false; }
                }   
            }
    }

    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkCollision();
            move();
        }

        repaint();
    }


    private class TAdapter extends KeyAdapter {
        boolean helpEnabled = true;
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
             
            if (key == KeyEvent.VK_F1) {
                if (helpEnabled) {
                menu.gameFrame.setSize(1024, 673); helpEnabled=false; }
                else { menu.gameFrame.setSize(1024, 705); helpEnabled=true; }               
            }
            
            if (key == KeyEvent.VK_F2) {
                JOptionPane.showMessageDialog(null, "Principle:\nYou have to collect all moveable objects\nin order to open door, doors are open when they change to the green exit icon.\n\nMove:\nChange direction: Arrow keys", "Extended help", 1);                 
            }
            
            if ((key == KeyEvent.VK_SPACE)&&(gameActive)) {
                    timer.stop();
                    gameActive = false;
                    JOptionPane.showMessageDialog(null,"Game paused" );
                    gameActive = true;
                    timer.start();   
                    }
            
            if (gameActive) {
                if ((key == KeyEvent.VK_LEFT)&&(train.getDirection()!=Direction.RIGHT)) {
                    train.setDirection(Direction.LEFT);
                }

                if ((key == KeyEvent.VK_RIGHT)&&(train.getDirection()!=Direction.LEFT)) {
                    train.setDirection(Direction.RIGHT);
                }

                if ((key == KeyEvent.VK_UP)&&(train.getDirection()!=Direction.DOWN)) {
                    train.setDirection(Direction.UP);
                }

                if ((key == KeyEvent.VK_DOWN)&&(train.getDirection()!=Direction.UP)) {
                    train.setDirection(Direction.DOWN);
                }
            train.setTHeadImByDirection();
            }
            
        }
    }
}