/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createv1p2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 *
 * @author Elder
 */
public class Graph extends BasicGameState {
    int[] progress;
    public Graph(int state){}

    @Override
    public int getID() {
        return 2;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        int inc = 700 / progress.length;
        g.drawLine(200, 20, 200, 720);
        g.drawLine(200, 720, 900, 720);
        g.drawString("Mass (kg)", 20, 362);
        g.drawString("Level", 525, 730);
        g.drawString("Press enter to continue", 1000, 362);
        
        int axis = 60;
        for(int y = 20; y <= 720; y += 70){
            g.setColor(Color.darkGray);
            if(y != 720){
                g.drawLine(201, y, 900, y);
            }
            g.setColor(Color.white);
            g.drawString("10^" + axis, 140, y-7);
            axis -= 10;
        }
        
        g.drawLine(200, 720, 200 + inc, 720 - ((progress[0] + 40) * 7));
        g.drawLine(200 + inc, 718, 200 + inc, 722);
        
        for(int i = 1; i < progress.length; ++i){
            g.drawLine(200 + (inc * i), 720 - ((progress[i-1] + 40) * 7), 200 + (inc * (i+1)), 720 - ((progress[i] + 40) * 7));
            g.drawLine(200 + (inc * (i+1)), 718, 200 + (inc * (i+1)), 722);
        }
        if(container.getInput().isKeyDown(Keyboard.KEY_RETURN)){
            game.enterState(0);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        try {
            progress = GameHandler.getGameProgress();
        } catch (IOException ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
