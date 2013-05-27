/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vlak;

/**
 *
 * @author Mirek
 */
public class Position implements Comparable<Position>{
    private int X;
    private int Y;
    
    public Position(int x,int y) {
    this.X = x;
    this.Y = y;
    }
    
    public void setx(int x)
    { this.X = x; }
    
    public int getx() { return this.X; }
    
    public void sety(int y)
    { this.Y = y; }
    
    public int gety() { return this.Y; }
    
    public void setxy(int x,int y) {
    this.X = x;
    this.Y = y;
    }
    
    public void setPosition(Position in) {
    this.X = in.X;
    this.Y = in.Y;
    }
    
    public Position getPosition()
    { return this; }

    @Override
    public int compareTo(Position t) {
        if ((this.X == t.X)&&(this.Y == t.Y)) { return 0; }
        else {
            if ( ((this.X - t.X)+(this.Y - t.Y)) >0) { return -1; }
            else { return 1; }
        }
    }

}
