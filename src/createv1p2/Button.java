/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createv1p2;

/**
 *
 * @author Elder
 */
public class Button {
    public int x;
    public int y;
    public int width;
    public int height;
    public int xTextOffset;
    public int yTextOffset;
    public String text;
    
    public Button(int x, int y, int width, int height, int xTextOffset, int yTextOffset, String text){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xTextOffset = xTextOffset;
        this.yTextOffset = yTextOffset;
        this.text = text;
    }
    
    public boolean isMouseOver(int xpos, int ypos){
        if((xpos >= x && xpos <= x + width) && (ypos >= y && ypos <= y + height)){
            return true;
        }
        return false;
    }
}
