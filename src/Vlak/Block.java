/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Vlak;

import java.awt.Image;

/**
 *
 * @author Mirek
 */
public class Block implements Comparable<Block> {
    private int width;
    private int height;
    private Image image;
    private Image doorChanged;
    private String imageName;
    private String doorImageName;
    public Position position;
    public BlockType type;
    
    public Block(int x, int y, int inWidth, int inHeight, Image inImage, String imName) {
        this.init(new Position(x,y), inWidth, inHeight, inImage, imName);
        }
    
    public Block(Position inPosition, int inWidth, int inHeight, Image inImage, String imName) {
        this.init(inPosition, inWidth, inHeight, inImage, imName);
        }
    
    public Block(Position inPosition, int inWidth, int inHeight, Image inImage, String imName, Image inDoorChange, String DImName) {
        this.doorChanged = inDoorChange;
        this.doorImageName = DImName;
        this.type = BlockType.DOOR;
        this.init(inPosition, inWidth, inHeight, inImage, imName);
        }
    
    private void init(Position inPosition, int inWidth, int inHeight, Image inImage, String imName) {
        this.width = inWidth;
        this.height = inHeight;
        this.image = inImage;
        this.position = inPosition;
        this.imageName = imName;
        
    }
    
    public void setType(BlockType inType) {
        this.type = inType;
        }
    
    public Image getImage() { return this.image; }
    
    public void setImage(Image inIm) {
        this.image = inIm;
    }
    
    public Image getOpenedImage() { return this.doorChanged; }
    
    public String getImageName() { return this.imageName; }
    
    public String getOpenedImageName() { return this.doorImageName; }
    
    public void setImageName(String name) { this.imageName = name; }
    
    
    @Override
    public int compareTo(Block t) {
        if ((this.imageName!=null)&&(this.image!=null)&&(t.imageName!=null)&&(t.getImage()!=null)) { 
            if ((this.getImage().equals(t.getImage()))&&(this.type==t.type)) {
            return 0; }        
        }
        return 1;
    }
    
    
    
}

