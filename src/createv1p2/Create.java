/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createv1p2;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author Elder
 */
public class Create extends BasicGameState{
    Random rnd = new Random();
    int xpos; //mouse position
    int ypos;
    boolean clickDown = false; //left mouse button down?
    boolean rightClickDown = false; //right mouse button down?
    Input input;
    Button[] buttons = new Button[6];
    int crossHairX = 0;
    int crossHairY = 0;
    boolean trigger = false;
    
    public Create(int state){
        
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        buttons[0] = new Button((int)(0.9231f * (float)gc.getWidth()), (int)(0.9479f * (float)gc.getHeight()), 100, 30, 13, 7, "Exchange");        
        buttons[1] = new Button((int)(0.59f * (float)gc.getWidth()), (int)(0.9479f * (float)gc.getHeight()), 100, 30, 13, 7, "Load Game");
        buttons[2] = new Button((int)(0.6939f * (float)gc.getWidth()), 231, 100, 30, 13, 7, "");
        buttons[3] = new Button((int)(0.6939f * (float)gc.getWidth()), 194, 100, 30, 13, 7, "");
        buttons[4] = new Button((int)(0.6939f * (float)gc.getWidth()), 157, 100, 30, 13, 7, "");
        buttons[5] = new Button((int)(0.6939f * (float)gc.getWidth()), 120, 100, 30, 13, 7, "");
        /*
        1261, 728, 100x30, 'Exchange', 13, 7
        948, 280, 100, 30, 'Load Game
        231, 'Iterations'
        194, 'Success'
        157, 'Death'
        120, 'Spread'
        */
        gc.getInput().enableKeyRepeat();
    
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        //there are two folders for graphics with slightly different resolutions
        //I did this before I realised I could scale the graphics with code
        //but since I had already gone to the effort of resizing the tiles, I kept it this way
        String resFolder = "res/";
        int tileSize = 76;
        if(gc.getWidth() == 1440){
            tileSize = 80;
            resFolder = "res2/";
        }
        
        Image img = new Image(resFolder + "bg.png");
        g.drawImage(img, 0, 0);
        
        
        for(int x = 0; x < tileSize*10; x += tileSize){ 
            for(int y = 0; y < tileSize*10; y += tileSize){ 
                
                int btnVal = GameHandler.grid[x/tileSize][y/tileSize];
                if(x/tileSize == GameHandler.multiplierX && y/tileSize == GameHandler.multiplierY && GameHandler.multiplierFound == true){
                    img = new Image(resFolder + "mult.png");
                    g.drawImage(img, x, y);
                }
                else if(btnVal <= 110 && btnVal >= 0){
                    if(((xpos >= x && xpos < x + tileSize) && (ypos >= y && ypos < y + tileSize) || (x / tileSize == crossHairX && y / tileSize == crossHairY))){
                        img = new Image(resFolder + btnVal + ".png");
                    }
                    else {
                        img = new Image(resFolder + btnVal + "trans.png");
                    }
                    g.drawImage(img, x, y);
                }
                else if(btnVal <= 480 && btnVal >= 120){
                    if(GameHandler.gridMask[x/tileSize][y/tileSize] == 0){
                        int fileNo = rnd.nextInt(GameHandler.maxFileNo[(btnVal/10) - 12])+1;
                        GameHandler.gridMask[x/tileSize][y/tileSize] = fileNo;
                    }
                    img = new Image(resFolder + GameHandler.fileName[(btnVal/10)-12] + GameHandler.gridMask[x/tileSize][y/tileSize] + ".jpg");
                    g.drawImage(img, x, y);
                }
                else if(btnVal < 0){
                    img = new Image(resFolder + "ent.png");
                    g.drawImage(img, x, y);
                }
                if(x / tileSize == crossHairX && y / tileSize == crossHairY){
                    img = new Image(resFolder + "xhair.png");
                    g.drawImage(img, x, y);
                }
            }
        }
        g.drawString(GameHandler.pct_complete + "% Completed", (int)(0.59f * (float)gc.getWidth()), 20);
        g.drawString("Seeds: " + GameHandler.seeds, (int)(0.59f * (float)gc.getWidth()), 40);
        g.drawString("Upgrade Points: " + GameHandler.upgradePoints, (int)(0.59f * (float)gc.getWidth()), 60);
        g.drawString("Level: " + GameHandler.level, (int)(0.59f * (float)gc.getWidth()), 80);
        
        g.drawString("Spread: " + GameHandler.spread, (int)(0.59f * (float)gc.getWidth()), 120);
        g.drawString("Death: " + GameHandler.df2.format(GameHandler.death), (int)(0.59f * (float)gc.getWidth()), 160);
        g.drawString("Success: " + GameHandler.mining_success + "%", (int)(0.59f * (float)gc.getWidth()), 200);
        g.drawString("Iterations: " + GameHandler.mining_iterations, (int)(0.59f * (float)gc.getWidth()), 240);
        
        g.drawString("Universe Mass: " + GameHandler.df.format(GameHandler.mass) + " kg", (int)(0.59f * (float)gc.getWidth()), 330);
        g.drawString("Equivalent Mass: " + GameHandler.equivMass(), (int)(0.59f * (float)gc.getWidth()), 350);
        g.drawString("% of Universe: " + GameHandler.df.format(GameHandler.massPct) + "%", (int)(0.59f * (float)gc.getWidth()), 370);
        g.drawString("Next order of magnitude:", (int)(0.59f * (float)gc.getWidth()), 390);
        checkButtons(xpos, ypos, g);
        drawButtons(g);
        
        if(GameHandler.pct_complete == 100){
            try {
                GameHandler.levelUp();
                sbg.enterState(2);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        double pct = GameHandler.mass;
        double exp = (int)Math.log10(pct);
        if(exp < 0){
            --exp;
        }
        pct /= Math.pow(10, exp);
        pct /= 10;
        g.setColor(Color.darkGray);
        g.fillRect((int)(0.59f * (float)gc.getWidth()), 415, (int)(300.0d * pct), 30);
        g.setColor(Color.white);
        g.drawRect((int)(0.59f * (float)gc.getWidth()), 415, 300, 30);
        
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        xpos = Mouse.getX();
        ypos = gc.getHeight() - Mouse.getY(); //I'm used to 0,0 being the top left corner, not bottom left
        input = gc.getInput();
        int tileSize = 76;
        if(gc.getWidth() == 1440){
            tileSize = 80;
        }
        if(Mouse.isButtonDown(0) && clickDown == false){
            try {
                if(buttons[0].isMouseOver(xpos, ypos)){
                    sbg.enterState(1);
                }
                if(buttons[1].isMouseOver(xpos, ypos)){
                    GameHandler.loadGame();
                }
                if(buttons[5].isMouseOver(xpos, ypos)){
                    GameHandler.upgradeSpread();
                }
                if(buttons[4].isMouseOver(xpos, ypos)){
                    GameHandler.upgradeDeath();
                }
                if(buttons[3].isMouseOver(xpos, ypos)){
                    GameHandler.upgradeSuccess();
                }
                if(buttons[2].isMouseOver(xpos, ypos)){
                    GameHandler.upgradeIterations();
                }
                if(xpos < tileSize * 10 && ypos < tileSize * 10){
                    do {
                        GameHandler.click(xpos, ypos, 0, buttons, tileSize);
                    } while (gc.getInput().isKeyDown(Input.KEY_LCONTROL) && Mouse.isButtonDown(0) && GameHandler.grid[xpos/tileSize][ypos/tileSize] > 0 && GameHandler.grid[xpos/tileSize][ypos/tileSize] <= 100);
                }
            } 
            catch (IOException ex) {
                Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
            }
            clickDown = true;
        }
        if(Mouse.isButtonDown(0) == false && clickDown == true){
            clickDown = false;
        }
        
        if(Mouse.isButtonDown(1) && rightClickDown == false){
            try {
                GameHandler.click(xpos, ypos, 1, buttons, tileSize);
            } catch (IOException ex) {
                Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
            }
            rightClickDown = true;
        }
        if(Mouse.isButtonDown(1) == false && rightClickDown == true){
            rightClickDown = false;
        }
        if(gc.getInput().isKeyPressed(Keyboard.KEY_RIGHT) && crossHairX < 9){ 
            ++crossHairX;
        }
        if(gc.getInput().isKeyPressed(Keyboard.KEY_LEFT) && crossHairX > 0){
            --crossHairX;
        }
        if(gc.getInput().isKeyPressed(Keyboard.KEY_UP) && crossHairY > 0){
            --crossHairY;
        }
        if(gc.getInput().isKeyPressed(Keyboard.KEY_DOWN) && crossHairY < 9){
            ++crossHairY;
        }
        if(gc.getInput().isKeyPressed(Keyboard.KEY_SPACE)){
            try {
                GameHandler.click(crossHairX * tileSize, crossHairY * tileSize, 0, buttons, tileSize);
            } catch (IOException ex) {
                Logger.getLogger(Create.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void checkButtons(int xpos, int ypos, Graphics g){
        for(int i = 0; i < 6; ++i){
            if(buttons[i].isMouseOver(xpos, ypos)){
                g.setColor(Color.lightGray);
                g.fillRect(buttons[i].x, buttons[i].y, buttons[i].width, buttons[i].height);
                g.setColor(Color.white);
            }
        }
    }
    
    private void drawButtons(Graphics g){
        for(int i = 0; i < buttons.length; ++i){
            g.drawRect(buttons[i].x, buttons[i].y, buttons[i].width, buttons[i].height);
        }
        
        if(GameHandler.spreadCostPtr < GameHandler.spreadCost.length){
            g.drawString("Cost: " + GameHandler.spreadCost[GameHandler.spreadCostPtr], buttons[5].x + buttons[5].xTextOffset, buttons[5].y + buttons[5].yTextOffset);
        }
        else {
            g.drawString("Cost: --", buttons[5].x + buttons[5].xTextOffset, buttons[5].y + buttons[5].yTextOffset);
        }
        
        
        if(GameHandler.deathCostPtr < GameHandler.deathCost.length){
            g.drawString("Cost: " + GameHandler.deathCost[GameHandler.deathCostPtr], buttons[4].x + buttons[4].xTextOffset, buttons[4].y + buttons[4].yTextOffset);
        }
        else {
            g.drawString("Cost: --", buttons[4].x + buttons[4].xTextOffset, buttons[4].y + buttons[4].yTextOffset);
        }
        
        
        if(GameHandler.successCostPtr < GameHandler.successCost.length){
            g.drawString("Cost: " + GameHandler.successCost[GameHandler.successCostPtr], buttons[3].x + buttons[3].xTextOffset, buttons[3].y + buttons[3].yTextOffset);
        }
        else {
            g.drawString("Cost: --", buttons[3].x + buttons[3].xTextOffset, buttons[3].y + buttons[3].yTextOffset);
        }
        
        
        if(GameHandler.iterationsCostPtr < GameHandler.iterationsCost.length){
            g.drawString("Cost: " + GameHandler.iterationsCost[GameHandler.iterationsCostPtr], buttons[2].x + buttons[2].xTextOffset, buttons[2].y + buttons[2].yTextOffset);
        }
        else {
            g.drawString("Cost: --", buttons[2].x + buttons[2].xTextOffset, buttons[2].y + buttons[2].yTextOffset);
        }
        
        
        for(int i = 0; i < 2; ++i){
            g.drawString(buttons[i].text, buttons[i].x + buttons[i].xTextOffset, buttons[i].y + buttons[i].yTextOffset);
        }
    }
    
    
}
