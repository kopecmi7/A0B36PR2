/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vlak;

import static Vlak.Constants.BLOCK_H;
import static Vlak.Constants.BLOCK_W;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Mirek
 */
public class Train implements Constants{
     
    private Direction direction = Direction.RIGHT; //1=UP, 2=DOWN, 3=LEFT, 4=RIGHT
    
    private Image hup,hdown,hleft,hright;
    
    Block head;
    ArrayList<Block> vagonky;
    
     public Train(Position start) {
         this.setHeadImages();
         this.head = new Block(start,BLOCK_W,BLOCK_H,hleft,null);
         vagonky = new ArrayList<Block>(2);
         this.direction = Direction.NONE;
     }
     
     public Train(Position start, Image overHeadIm) {
         this.hup = overHeadIm; this.hleft = overHeadIm; this.hright = overHeadIm; this.hdown = overHeadIm;
         this.head = new Block(start,BLOCK_W,BLOCK_H,hleft,null);
         vagonky = new ArrayList<Block>(2);
         this.direction = Direction.NONE;
     }
     
     public void setHeadImages() {
         this.hup = new ImageIcon(this.getClass().getResource("./textures/theadup.png")).getImage();             
         this.hdown = new ImageIcon(this.getClass().getResource("./textures/theaddown.png")).getImage();             
         this.hleft = new ImageIcon(this.getClass().getResource("./textures/theadleft.png")).getImage();             
         this.hright = new ImageIcon(this.getClass().getResource("./textures/theadright.png")).getImage(); 
     }
     
     public void setTHeadImByDirection() {
         switch (this.direction) {
             case UP:
                 this.head.setImage(hup);
                 break;
             case DOWN:
                 this.head.setImage(hdown);
                 break;
             case LEFT:
                 this.head.setImage(hleft);
                 break;
             case RIGHT:
                 this.head.setImage(hright);
                 break;
             default:
                 this.head.setImage(hleft);
                 break;
         }
     }
     public Direction getDirection() {  
         if (this.vagonky.isEmpty()) { return Direction.NONE; }
         else return this.direction;
     }
     
     public void setDirection(Direction where) {
         this.direction = where;
     }
     
     public int getLength() {
     return this.vagonky.size();
     }
     
     public void move() {
            
         if (this.direction != Direction.NONE) {
            for (int fi=(this.vagonky.size()-1);fi>0;fi-=1) {
                this.vagonky.get(fi).position.setxy(this.vagonky.get(fi-1).position.getx(), this.vagonky.get(fi-1).position.gety());  
                }
            
            if (!this.vagonky.isEmpty()) {
            this.vagonky.get(0).position.setxy(this.head.position.getx(),this.head.position.gety());
            }
            
            if (this.direction == Direction.UP) {
                if (this.head.position.gety()==0) this.head.position.sety(BLOCK_COUNT_H-1);
                else this.head.position.sety(this.head.position.gety()-1);
                }
            if (this.direction == Direction.DOWN) {
                if (this.head.position.gety()==(BLOCK_COUNT_H-1)) this.head.position.sety(-1);
                this.head.position.sety(this.head.position.gety()+1);            
                }
            if (this.direction == Direction.LEFT) {
                if (this.head.position.getx()==0) this.head.position.setx(BLOCK_COUNT_W);
                this.head.position.setx(this.head.position.getx()-1);            
                }
            if (this.direction == Direction.RIGHT) {
                if (this.head.position.getx()==(BLOCK_COUNT_W-1)) this.head.position.setx(-1);
                this.head.position.setx(this.head.position.getx()+1);            
                }
            }
         
        //##
        }
     
     
    public void paint(Graphics g, ImageObserver io) {
        int len = this.vagonky.size();
        Block tmp;
        
        g.drawImage(this.head.getImage(), (this.head.position.getx()*Constants.BLOCK_H), (this.head.position.gety()*Constants.BLOCK_W),io);
        
        if (!this.vagonky.isEmpty()) {
            for (int fi=0;fi<len;fi+=1) {
                tmp = this.vagonky.get(fi);
                g.drawImage(tmp.getImage(), (tmp.position.getx()*Constants.BLOCK_H), (tmp.position.gety()*Constants.BLOCK_W), io);
                }
            }
        }
       
    public void addItem(Block item) {
    item.setType(BlockType.TRAIN);
    this.vagonky.add(item);    
    }    

    public boolean checkColision() {
        int len = this.vagonky.size();
        Block tmp;
        for (int fi=0;fi<len;fi+=1) {
            if (this.vagonky.get(fi).position.compareTo(this.head.position)==0) { 
                    System.out.print("run into your self");
                    return false; }                
            }
        return true; 
    }
    
    
    
}
     
     
     
     


