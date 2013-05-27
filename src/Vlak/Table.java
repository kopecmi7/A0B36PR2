/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vlak;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.ImageIcon;

/**
 *
 * @author Mirek
 */
public class Table {
    String name;
    private boolean doorOpened = false;
    private ArrayList<Block> table;
    private int width; //x_prvku
    private int height; //y_prvku
    private Position start;
    
    public Table(int x_prvku,int y_prvku) {
        name = "new";
        this.width = x_prvku;
        this.height = y_prvku;
        this.resizeArray(this.width, this.height);
        this.start = new Position(2,2);
        }
    
    private void resizeArray(int w, int h)
        {
        int size = w*h;
        this.table = new ArrayList<Block>(size);
        }
    
    public void fillEmpty() {
        for (int fi=0;fi<(this.width*this.height);fi+=1) {
        Block tmpB = new Block(fi/this.height,fi%this.height,Constants.BLOCK_W,Constants.BLOCK_H,null,null);
        tmpB.type = BlockType.NONE;
        this.table.add(fi,tmpB);    
        }
    }
    
    public void setDoorOpened(boolean isOpened) {
        this.doorOpened = isOpened; 
    }
    public boolean getDoorOpened() {
        return this.doorOpened; 
    }
    public boolean isDoorPlaced() {
        int size = width*height;
        for (int fi=0;fi<size;fi+=1) {
            if (this.table.get(fi).type==BlockType.DOOR) return true;
            }
        return false;
    }
            
    public int getWidth() {
        return width; }
    public void setWidth(int width) {
        this.width = width; }
    public int getHeight() {
        return height; }
    public void setHeight(int height) {
        this.height = height; }
    public Position getStartP() {
        return this.start; }
    
    public Block getBlock(int souradnice_x,int souradnice_y) {
        return this.table.get( (souradnice_y*(this.width))+souradnice_x);
        }
    public Block getBlock(Position poz) {
        return this.table.get( (poz.gety()*(this.width))+poz.getx());
        }
    
    
    public void addBlock(Block inBlock) {
        this.table.add(inBlock);
        }
    
    public void setBlock(int souradnice_x,int souradnice_y, Block inBlock) {
        int place = ((souradnice_y*(this.width))+souradnice_x);
        this.table.set(place,inBlock);
        }
    
    public void setBlock(Position poz, Block inBlock) {
        int index = ( (poz.gety()*(this.width))+poz.getx());
        this.table.set(index,inBlock);    
        }
    
    public int getSize() {
        return this.table.size();
        }
    
    public boolean loadMap(String pathName,String MAPfolder) {
        File map = new File(MAPfolder + pathName);
        Scanner in;
        try {
        in = new Scanner(map);  }
        catch (Throwable e) { System.out.printf("loadin file not found\n", name); return false;}
        
        this.name = in.nextLine();
        System.out.printf("loaded map name: %s\n", name);
        this.width = in.nextInt();
        this.height = in.nextInt();
        System.out.printf("map size: %d x %d\n", width,height);
        this.resizeArray(this.width, this.height);
        int max = in.nextInt();
        System.out.printf("pocet prvku: %d\n",max );
        
        String IMs[][] = new String[max][2];
        int IDs[] = new int[max];
        
        String IMfolder = in.next();
        System.out.printf("slozka obrazku: '%s'", IMfolder);
        
        for (int fi=0;fi<max;fi+=1) {
            IDs[fi] = in.nextInt();
            IMs[fi][0] = in.next();
            if (IDs[fi]==BlockType.DOOR.ordinal()) { 
                IMs[fi][1] = in.next();
                System.out.printf("[%d] typ:DOOR file:'%s' openedf:'%s'\n",fi,IMs[fi][0],IMs[fi][1] );} 
            else System.out.printf("[%d] typ:%d file:'%s'\n",fi,IDs[fi],IMs[fi][0] );
            }
        
        
        for (int fh=0;fh<this.height;fh=fh+1) {
            for (int fw=0;fw<this.width;fw=fw+1) {
                int tmp = in.nextInt();
                System.out.print(tmp + " ");
                Block tmpB;
                if (tmp==-1) { tmp=0; this.start.setxy(fw, fh); System.out.printf("start found at: %d %d\n",fw,fh); }
                if (tmp!=0) {
                    tmp-=1;
                    System.out.printf("folder:%s file:%s tmp:%d\n",IMfolder, IMs[tmp][0],tmp);
                    Image tmpI =  new ImageIcon(this.getClass().getResource(IMfolder+IMs[tmp][0])).getImage();
                    //System.out.print(IDs[tmp]+" <-type\n");    
                    if (IDs[tmp]==BlockType.DOOR.ordinal()) {
                        tmpB = new Block(new Position(fw,fh),Constants.BLOCK_W,Constants.BLOCK_H,tmpI,IMs[tmp][0],(new ImageIcon(this.getClass().getResource(IMfolder+IMs[tmp][1]))).getImage(),IMs[tmp][1]);
                        }
                    else { tmpB = new Block(new Position(fw,fh),Constants.BLOCK_W,Constants.BLOCK_H,tmpI,IMs[tmp][0]); }
                    if (IDs[tmp]==BlockType.SOLID.ordinal()) { tmpB.setType(BlockType.SOLID); }
                    if (IDs[tmp]==BlockType.MOVEABLE.ordinal()) { tmpB.setType(BlockType.MOVEABLE); }
                    }
                
                else {
                    tmpB = new Block(fw,fh,Constants.BLOCK_W,Constants.BLOCK_H,null,null);
                    tmpB.setType(BlockType.NONE);
                    }
                
                int poz = (fh*this.width+fw);
                this.table.add(poz,tmpB);
                //this.table.set(poz,tmpB);
                //System.out.print("loaded type: "+table.get(poz).type.ordinal() +"\n");
                }
            System.out.print("\n");
            }
        map.exists();   
        return true;
        }
    
    public boolean printMap(PrintWriter out, Position startP) {
        ArrayList<Block> list = new ArrayList<Block>(640);
        int outLines[] = new int[this.table.size()];
        int count=0;
        int tmax=this.table.size();
        int lmax=0;
        System.out.print("table size: "+this.table.size()+"\n");
                
        for (int fi=0;fi<tmax;fi+=1) {
            Block tempB = this.table.get(fi); 
            outLines[fi]=0;
            if (tempB.type != BlockType.NONE) { 
                //System.out.print("type:: "+tempB.type.ordinal()+" nonetype :"+BlockType.NONE.ordinal()+ "\n");
                list.add(null);
                list.set(count,tempB);
                count+=1;
                outLines[fi] = count;
                }
            }
        
        out.println(this.name);
        out.printf("%d %d\n",Constants.BLOCK_COUNT_W,Constants.BLOCK_COUNT_H);
        out.printf("%d %s\n",count,"./textures/");
        
        for (int fi=0;fi<count;fi+=1) {
            Block tempB = list.get(fi);
            //System.out.print(tempB.type+"\n");
            if (tempB.type!=BlockType.DOOR) {
                out.printf("%d %s\n",tempB.type.ordinal(),tempB.getImageName()); }
            else { out.printf("%d %s %s\n",tempB.type.ordinal(),tempB.getImageName(),tempB.getOpenedImageName()); 
                //System.out.print(tempB.getOpenedImageName()+"\n"); 
                 }
            }
        
        outLines[startP.getx()+startP.gety()*width] = -1;
        System.out.printf("x:%d y:%d sum:%d\n",startP.getx(),startP.gety(),(startP.getx()+startP.gety()*height));
        
        for (int fa=0;fa<width;fa+=1) {
            for (int fi=0;fi<height;fi+=1) {
                out.printf("%d ",outLines[fi+fa*height]); }
            out.printf("\n");
            }
        
        return true;
        }
    
    public void paint(Graphics g, ImageObserver io) {
        int max = this.width*this.height;
        for (int fi=0;fi<max;fi=fi+1) {
                Block tmp = this.table.get(fi);
                if ((tmp.type!=BlockType.NONE)&&(tmp.type!=BlockType.TRAIN)) {
                    if ((tmp.type!=BlockType.DOOR)||(!doorOpened)) {
                        g.drawImage(tmp.getImage(), (tmp.position.getx()*Constants.BLOCK_W), (tmp.position.gety()*Constants.BLOCK_H), io); }        
                    else { 
                        g.drawImage(tmp.getOpenedImage(), (tmp.position.getx()*Constants.BLOCK_W), (tmp.position.gety()*Constants.BLOCK_H), io); }
                    }
                }
    }
    
    public boolean isAllColected() {
    int size = width*height;
    for (int fi=0;fi<size;fi+=1) {
        if (this.table.get(fi).type==BlockType.MOVEABLE) return false;
        }
    return true;
    }
    
    
}
