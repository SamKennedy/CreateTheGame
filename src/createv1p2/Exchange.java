package createv1p2;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Elder
 */
public class Exchange extends BasicGameState{
    
    int xpos;
    int ypos;
    boolean mouseDown;
    boolean rightMouseDown;
    boolean osr2 = true;
    boolean osr = true;
    Button createBtn;
    
    public Exchange(int state){
        
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        createBtn = new Button((int)(0.9231f * (float)gc.getWidth()), (int)(0.9479f * (float)gc.getHeight()), 100, 30, 13, 7, "Create");   
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        Image img;
        int i = 0;
        
        
        if((xpos >= createBtn.x && xpos <= createBtn.x + createBtn.width) && (ypos >= createBtn.y && ypos <= createBtn.y + createBtn.height)){
            g.setColor(Color.lightGray);
            g.fillRect(createBtn.x, createBtn.y, createBtn.width, createBtn.height);
            g.setColor(Color.white);
            if(mouseDown == true && osr == false){
                sbg.enterState(0);
                osr = true;
            }
            if(mouseDown == false){
                osr = false;
            }
        }
        g.drawRect(createBtn.x, createBtn.y, createBtn.width, createBtn.height);
        
        g.drawString("Create", createBtn.x + createBtn.xTextOffset, createBtn.y + createBtn.yTextOffset);
        int tileSize = 76;
        String resFolder = "res/";
        int infoOffset = 561; //if we are using a different resolution the info text needs to be further down the window
        if(gc.getWidth() == 1440){
            tileSize = 80;
            resFolder = "res2/";
            infoOffset = 345;
        }
        g.drawString("Multipliers: " + GameHandler.multipliers, (int)(0.7532f * (float)gc.getWidth()), infoOffset-20);
        for(int x = 0; x < gc.getWidth(); x += gc.getWidth()/4){
            for(int y = 0; y < gc.getHeight() - tileSize; y += tileSize){
                if(i < 37){
                    img = new Image(resFolder + GameHandler.fileName[i] + "1.jpg");
                    if((xpos >= x && xpos < x+gc.getWidth()/4) && (ypos >= y && ypos < y + tileSize)){
                        g.setColor(Color.gray);
                        g.fillRect(x, y, gc.getWidth()/4, tileSize);
                        g.setColor(Color.white);
                        String[] para = GameHandler.reqStrBldr(i);
                        for(int a = 0; a < para.length; ++a){
                            g.drawString(para[a], (int)(0.7532f * (float)gc.getWidth()), (a * 20) + infoOffset);
                        }
                        if(mouseDown && osr == false){
                            GameHandler.exchange(i);
                            osr = true;
                        }
                        if(mouseDown == false && osr == true){
                            osr = false;
                        }
                        
                        if(rightMouseDown && osr2 == false){
                            GameHandler.multiply(i);
                            osr2 = true;
                        }
                        if(rightMouseDown == false && osr2 == true){
                            osr2 = false;
                        }
                                
                    }
                    g.drawString(GameHandler.particleTypes[i], x + 100, y + 30);
                    if(GameHandler.particleMask[i]){
                        double particles = GameHandler.particleBucket[i];
                        if(particles <= 999_999_999.9d){
                            g.drawString(String.valueOf((int)particles), x + 250, y + 30);
                        }
                        else {
                            g.drawString(String.valueOf(GameHandler.df.format(GameHandler.particleBucket[i])), x + 250, y + 30);
                        }
                        
                    }
                    g.drawImage(img, x, y);
                    g.drawRect(x, y, gc.getWidth()/4, tileSize);
                    
                    
                    
                    ++i;
                }
            }
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        xpos = Mouse.getX();
        ypos = gc.getHeight() - Mouse.getY();
        if(Mouse.isButtonDown(0)){
            mouseDown = true;
        }
        else {
            mouseDown = false;
        }
        
        rightMouseDown = false;
        if(Mouse.isButtonDown(1)){
            rightMouseDown = true;
        }
                
    }

    
  
    
}
