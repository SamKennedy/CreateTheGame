/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createv1p2;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.Sound;


/**
 *
 * @author Elder
 */
public class CreateV1p2 extends StateBasedGame {
    public static final String gamename = "Create v1.5";
    public static final int create = 0;
    public static final int exchange = 1;
    public static final int graph = 2;
    
    public static void main(String[] args) throws SlickException {
        Sound sound = new Sound("res/kolob.ogg");
        sound.play();
        AppGameContainer appgc;
        
        try{
            appgc = new AppGameContainer(new CreateV1p2(gamename));
            //supports 1440x900 and 1366x768
            //eg. run with command line arguments '1366 768 false' for a 1366x768 window
            appgc.setDisplayMode(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Boolean.valueOf(args[2])); 
            appgc.setShowFPS(false);
            appgc.setAlwaysRender(false);
            appgc.start();
        } catch(SlickException e){
            e.printStackTrace();
        }
    }

    public CreateV1p2(String name) {
        super(name);
        this.addState(new Create(create));
        this.addState(new Exchange(exchange));
        this.addState(new Graph(graph));
        GameHandler.setGridToZero();
        GameHandler.addEntropy();
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(create).init(gc, this);
        this.getState(exchange).init(gc, this);
        this.getState(graph).init(gc, this);
        this.enterState(create);
    }
    
}
