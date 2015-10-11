/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createv1p2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Random;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Elder
 */
public class GameHandler {
    public static DecimalFormat df = new DecimalFormat("0.000E00");
    public static DecimalFormat df2 = new DecimalFormat("0.0");
    private static final Random rnd = new Random();
    public static int[][] grid = new int[10][10];
    public static int[][] gridMask = new int[10][10];
    public static int pct_complete = 0;
    public static int seeds = 10;
    public static int level = 1;
    public static int upgradePoints = 0;
    public static boolean multiplierFound = false;
    public static int multiplierX;
    public static int multiplierY;
    public static double mass = 0.0d;
    public static double massPct = 0.0d;
    public static int multipliers = 0;
    
    
    //<exchange matrix>
    //there is no doubt a better way of implenting this, but I have the time and nothing better to do
    private static double[][] exchangeMatrix = {
        { 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Proton
        { 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Neutron
        { 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Hydrogen
        { 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Helium
        { 0, 0, 6, 6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Carbon
        { 0, 0, 7, 7, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Nitrogen
        { 0, 0, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Oxygen
        { 0, 0, 11, 11, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Sodium
        { 0, 0, 12, 12, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Magnesium
        { 0, 0, 14, 14, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Silicon
        { 0, 0, 15, 15, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Phosphorous
        { 0, 0, 16, 16, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Sulfur
        { 0, 0, 17, 17, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Chlorine
        { 0, 0, 26, 26, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Iron
        { 0, 0, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Water
        { 0, 0, 0, 0, 0, 60, 0, 72, 28, 32, 0, 0, 0, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //DNA
        { 0, 0, 0, 0, 0, 15, 0, 18, 7, 8, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Amino Acid
        { 0, 0, 0, 0, 0, 20, 0, 9, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Phospholipid
        { 0, 0, 0, 0, 0, 25, 0, 16, 0, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Polysaccharide
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //Protein
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3000000, 1000000, 1000000, 1000000, 1000000, 1000000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Bacteria
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3000, 1000, 1000, 1000, 1000, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Virus
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3e15, 1e15d, 1e15d, 1e15d, 1e15d, 1e15d, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Insect
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5e23d, 1e23d, 1e23d, 1e23d, 1e23d, 5e22d, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Plant
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7e24d, 1e24d, 1e24d, 1e24d, 1e24d, 1e24d, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Animal
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32e23d, 4e23d, 4e23d, 4e23d, 4e23d, 8e23d, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Human
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 330e41d, 77e41d, 0, 0, 37e41d, 550e41d, 0, 1e47d, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Ocean
        { 0, 0, 0, 0, 0, 0, 0, 0, 1e50d, 4e50d, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Atmosphere
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1e49d, 0, 1e47, 1e48, 0, 0, 0, 8e49d, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Crust
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0 }, //Planet
        { 0, 0, 0, 0, 0, 1e56d, 1e56d, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //Star
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100000000000d, 200000000000d, 0, 0, 0 }, //Galaxy
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 0, 0 }, //Cluster
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0 }, //Super Cluster
    };
    
    //<abilities>
    public static int spread = 20;
    public static float death = 1.0f;
    public static int mining_success = 20;
    public static int mining_iterations = 1;
    public static int[] spreadCost = { 10, 20, 30 };
    public static int[] deathCost = { 10, 15, 20, 30, 50 };
    public static int[] successCost = { 10, 15, 20, 25, 30, 35, 40, 50 };
    public static int[] iterationsCost = { 10, 15, 20, 25, 30, 40, 50, 75, 100, 150, 200, 250, 300, 500 };
    public static int spreadCostPtr = 0;
    public static int deathCostPtr = 0;
    public static int successCostPtr = 0;
    public static int iterationsCostPtr = 0;
    //</abilities>
    
    public static String[] particleTypes = { "Up Quark", "Down Quark", "Electron", 
        "Proton", "Neutron", 
        "Hydrogen", "Helium", "Carbon", "Nitrogen", "Oxygen", "Sodium", "Magnesium", "Silicon", "Phosphorous", "Sulfur", "Chlorine", "Iron",
        "Water", "DNA", "Amino Acid", "Phospholipid", "Polysaccharide",
        "Protein",
        "Bacteria", "Virus", "Insect", "Plant", "Animal", "Human",
        "Ocean", "Atmosphere", "Crust",
        "Planet", "Star", "Galaxy", "Cluster", "Super Cluster" };
    static boolean[] particleMask = { true, true, true, 
        false, false, 
        false, false, false, false, false, false, false, false, false, false, false, false,
        false, false, false, false, false,
        false,
        false, false, false, false, false, false,
        false, false, false,
        false, false, false, false, false };
    static double[] particleMultiplier = { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    static double[] particleBucket = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    static String[] fileName = { "uq", "dq", "e", "pplus", "n", "h", "he", "c", "ni", "o", "na", "mg", "si", "p", "s", "cl", "fe", "h2o", "dna", "aa", "pbl", "ply", "prot", "bac", "vi",
                       "ins", "plant", "an", "hum", "ocean", "atm", "crust", "planet", "Star", "gal", "clstr", "uni"};
    static int[] maxFileNo = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 2, 1, 1, 1, 1, 1, 3, 1, 1 };
    static int[] particleNo = { 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 400, 410,
                                420, 430, 440, 450, 460, 470, 480 };
    static double[] massArray = { 0, 0, 9.1e-31d, 1.67e-27d, 1.67e-27d, 1.67e-27d, 6.64e-27d, 1.99e-26d, 2.32e-26d, 2.65e-26d, 3.81e-26d, 4.03e-26d, 4.66e-26d, 5.14e-26d, 5.32e-26d, 5.88e-26d,
        9.27e-26d, 2.984e-26d, 3.875e-24d, 9.688e-25d, 2.639e-25d, 7.3115e-25d, 9.688e-23d, 1.028e-16d, 1.028e-19d, 1.028e-7d, 5.442d, 1.029e2d, 7.992e1d, 2.988e21d, 1.292e18d, 7.731e24d,
        7.733e24d, 8.31e29d, 1.662e41d, 1.662e43d, 8.31e43d
    };
    
    
    public static void setGridToZero(){
        for(int x = 0; x < 10; ++x){
            for(int y = 0; y < 10; ++y){
                grid[x][y] = 0;
                gridMask[x][y] = 0;
            }
        }
        multiplierX = rnd.nextInt(10);
        multiplierY = rnd.nextInt(10);
        multiplierFound = false;
    }
    
    public static void click(int x, int y, int btn, Button[] buttons, int tileSize) throws IOException, SlickException{
        x /= tileSize;
        y /= tileSize;
        
        if(x < 10 && y < 10){
            if(btn == 0){
                if(grid[x][y] > 0 && grid[x][y] <= 100){
                    //spin to see if cell grows left
                    if(x > 0 && rnd.nextInt(100) < grid[x][y] && grid[x-1][y] <= 100){
                        incGrid(x-1, y, spread);
                    }
                        //spin to see if cell grows right
                    if(x < 9 && rnd.nextInt(100) < grid[x][y] && grid[x+1][y] <= 100){
                        incGrid(x+1, y, spread);
                    }
                    //spin to see if cell grows up
                    if(y > 0 && rnd.nextInt(100) < grid[x][y] && grid[x][y-1] <= 100){ 
                        incGrid(x, y-1, spread);
                    }
                    //spin to see if cell grows down
                    if(y < 9 && rnd.nextInt(100) < grid[x][y] && grid[x][y+1] <= 100){
                        incGrid(x, y+1, spread);
                    }                   
                    //spin to see if cell dies
                    if(rnd.nextInt(100) < grid[x][y] * death){
                        grid[x][y] = 0;
                    }

                } //EXPLOSION CODE:
                else if (grid[x][y] == 110){
                    if(x == multiplierX && y == multiplierY && multiplierFound == false){
                        multiplierFound = true;
                        grid[x][y] = 480;
                        soundPlayer("res/sweep.wav");
                        multipliers += rnd.nextInt(5) + 1;
                    }
                    else {
                        if(x > 0 && grid[x-1][y] <= 100){
                            incGrid(x-1, y, 50);
                        }
                        if(x < 9 && grid[x+1][y] <= 100){
                            incGrid(x+1, y, 50);
                        }
                        if(y > 0 && grid[x][y-1] <= 100){
                            incGrid(x, y-1, 50);
                            }
                        if(y < 9 && grid[x][y+1] <= 100){
                            incGrid(x, y+1, 50);
                            }
                        
                        //select random particle to create
                        int randomParticleIdx = rnd.nextInt(37);
                            while(particleMask[randomParticleIdx] == false){
                                randomParticleIdx = rnd.nextInt(37);
                            }
                        grid[x][y] = particleNo[randomParticleIdx];
                        if(rnd.nextInt(100) < mining_success){
                            for(int i = 0; i < mining_iterations; ++i){
                                
                                particleBucket[randomParticleIdx] += particleMultiplier[randomParticleIdx];
                            }
                            soundPlayer("res/explosion.wav");
                        }
                        else {
                            grid[x][y] = 490;
                            soundPlayer("res/nobeans.wav");
                        }
                    }
                }
            } //RIGHT CLICK:
            else if(btn == 1){
                if(grid[x][y] <= 100 && seeds > 0){
                    incGrid(x, y, spread);
                    --seeds;
                }
            }
         calcPctComplete();
         updateMass();
        }
        
        
    }
    
    private static void incGrid(int x, int y, int inc){
        boolean negFlag = false;
        if(grid[x][y] < 0){
            negFlag = true;
        }
        if(grid[x][y] < 100){
            grid[x][y] += inc;
            if(grid[x][y] > 100){
                grid[x][y] = 100;
            }
        }
        else if(grid[x][y] >= 100){
            grid[x][y] = 110;
        }
        if(grid[x][y] >= 0 && negFlag == true){
            ++upgradePoints;
            ++seeds;
        }
    }
    
    public static void addEntropy(){
        for(int x = 0; x < 10; ++x){
            for(int y = 0; y < 10; ++y){
                if(rnd.nextInt(100) < level){
                    grid[x][y] = -100;
                }
            }
        }
    }
    
    private static void calcPctComplete(){
        pct_complete = 0;
        for(int x = 0; x < 10; ++x){
            for(int y = 0; y < 10; ++y){
                if(grid[x][y] > 0){
                    ++pct_complete;   
                }
            }
        }
    }
    
    public static void levelUp() throws FileNotFoundException, SlickException, IOException{
        setGridToZero();
        ++level;
        addEntropy();
        pct_complete = 0;
        
        String saveString = String.valueOf(spread) + "," + String.valueOf(death) + "," + String.valueOf(mining_success) + "," + String.valueOf(mining_iterations) + "," +
                        String.valueOf(spreadCostPtr) + "," + String.valueOf(deathCostPtr) + "," + String.valueOf(successCostPtr) + "," + String.valueOf(iterationsCostPtr) + ",";
                for(int i = 0; i < particleMask.length; ++i){
                    saveString += String.valueOf(particleMask[i]) + ",";
                }
                for(int i = 0; i < particleMultiplier.length; ++i){
                    saveString += String.valueOf(particleMultiplier[i] + ",");
                }
                for(int i = 0; i < particleBucket.length; ++i){
                    saveString += String.valueOf(particleBucket[i] + ",");
                }
                saveString += String.valueOf(upgradePoints) + "," + String.valueOf(seeds) + "," + String.valueOf(level) + "," + String.valueOf(multipliers);
                int massVal = (int)Math.log10(mass);
                if(massVal < 0){
                    --massVal;
                }
                if(level == 2){
                    saveString += "," + String.valueOf(massVal);
                }
                else if(level > 2){
                    int[] prev = getGameProgress();
                    for(int i = 0; i < prev.length; ++i){
                        saveString += "," + String.valueOf(prev[i]);
                    }
                    saveString += "," + String.valueOf(massVal);
                }
                PrintWriter out = new PrintWriter("state.dat");
                out.print(saveString);
                out.close();
                soundPlayer("res/finished.wav");
    }
    
    public static void upgradeSpread(){
        if(spreadCostPtr < spreadCost.length){
            if(upgradePoints >= spreadCost[spreadCostPtr]){
                upgradePoints -= spreadCost[spreadCostPtr];
                spread += 10;
                ++spreadCostPtr;
            }
        }
    }
    
    public static void upgradeDeath(){
        if(deathCostPtr < deathCost.length){
            if(upgradePoints >= deathCost[deathCostPtr]){
                upgradePoints -= deathCost[deathCostPtr];
                death -= 0.1f;
                ++deathCostPtr;
            }
        }
    }
    
    public static void upgradeSuccess(){
        if(successCostPtr < successCost.length){
            if(upgradePoints >= successCost[successCostPtr]){
                upgradePoints -= successCost[successCostPtr];
                mining_success += 10;
                ++successCostPtr;
            }
        }
    }
    
    public static void upgradeIterations(){
        if(iterationsCostPtr < iterationsCost.length){
            if(upgradePoints >= iterationsCost[iterationsCostPtr]){
                upgradePoints -= iterationsCost[iterationsCostPtr];
                mining_iterations *= 2;
                ++iterationsCostPtr;
            }
        }
    }
    
    public static void loadGame() throws FileNotFoundException, IOException{
       FileReader fr;
       fr = new FileReader("state.dat");
       BufferedReader br = new BufferedReader(fr);
       String stateString = br.readLine();
       String[] states = stateString.split(",");
       
       spread = Integer.valueOf(states[0]);
       death = Float.valueOf(states[1]);
       mining_success = Integer.valueOf(states[2]);
       mining_iterations = Integer.valueOf(states[3]);
       
       spreadCostPtr = Integer.valueOf(states[4]);
       deathCostPtr = Integer.valueOf(states[5]);
       successCostPtr = Integer.valueOf(states[6]);
       iterationsCostPtr = Integer.valueOf(states[7]);
       
       for(int i = 8; i < 45; ++i){
           particleMask[i-8] = Boolean.valueOf(states[i]);
       }
       for(int i = 45; i < 82; ++i){
           particleMultiplier[i-45] = Double.valueOf(states[i]);
       }
       for(int i = 82; i < 119; ++i){
           particleBucket[i-82] = Double.valueOf(states[i]);
       }
       upgradePoints = Integer.valueOf(states[119]);
       seeds = Integer.valueOf(states[120]);
       level = Integer.valueOf(states[121]);
       multipliers = Integer.valueOf(states[122]);
       setGridToZero();
       addEntropy();
       updateMass();
    }
    
    private static void updateMass(){
       double total = 0.0d;
       for(int i = 0; i < 37; ++i){
           total += particleBucket[i] * massArray[i];
       }
       mass = total;
       massPct = (total / 1.0e54d) * 100d;
   }
    
    public static String equivMass(){
        int e = (int)Math.log10(mass);
        if(e >= 52){ return "Universe"; }
        else if(e >= 42){ return "Galaxy"; }
        else if(e >= 30){ return "Sun"; }
        else if(e >= 24){ return "Earth"; }
        else if(e >= 22){ return "Moon"; }
        else if(e >= 15){ return "World's Coal Deposits"; }
        else if(e >= 11){ return "Total Human Population"; }
        else if(e >= 8){ return "Largest Ship"; }
        else if(e >= 6){ return "Space Shuttle"; }
        else if(e >= 3){ return "Car"; }
        else if(e >= 1){ return "Adult Human"; }
        else if(e >= 0){ return "Cat"; }
        else if(e >= -1){ return "Orange"; }
        else if(e >= -3){ return "Raisin"; }
        else if(e >= -7){ return "Fruit Fly"; }
        else if(e >= -15){ return "Bacterium"; }
        else if(e >= -21){ return "DNA Molecule"; }
        else if(e >= -25){ return "Caffeine Molecule"; }
        else if(e >= -27){ return "Hydrogen Atom"; }
        else { return "Electron"; }
    }
    
    public static String[] reqStrBldr(int i){
        
        if(i >= 3){
            String bld = "";
            for(int x = 0; x < exchangeMatrix[0].length; ++x){
                if(exchangeMatrix[i-3][x] > 0.0d){
                    bld += String.valueOf(exchangeMatrix[i-3][x]) + " " + String.valueOf(particleTypes[x]) + "\n";
                    }
            }
            return bld.split("\n");
        }
        String[] retStr = { "" };
        return retStr;
    }
    
    public static void exchange(int i){
        if(i >= 3){
            boolean sufficientFunds = true;
            for(int l = 0; l < 37; ++l){
                if(particleBucket[l] < exchangeMatrix[i-3][l]){
                    sufficientFunds = false;
                    break;
                }
            }
            if(sufficientFunds == true){
                particleMask[i] = true;
                for(int l = 0; l < 37; ++l){
                    particleBucket[l] -= exchangeMatrix[i-3][l];
                }
                particleBucket[i] += 1.0d;
                if(particleMultiplier[i] == 0.0d){
                    particleMultiplier[i] = 1.0d;
                }
                updateMass();
            }
        }
    }
    
    public static void soundPlayer(String loc) throws SlickException{
        Sound sound = new Sound(loc);
        sound.play();
    }
    
    public static void multiply(int i){
        if(particleMask[i] == true && multipliers > 0){
            particleMultiplier[i] = particleBucket[i];
            if(particleMultiplier[i] == 0.0d){
                particleMultiplier[i] = 1.0d;
            }
            --multipliers;
        }
    }
    
    public static int[] getGameProgress() throws FileNotFoundException, IOException{
       FileReader fr;
       fr = new FileReader("state.dat");
       BufferedReader br = new BufferedReader(fr);
       String stateString = br.readLine();
       String[] states = stateString.split(",");
       int[] retArr = new int[states.length - 123];
       for(int i = 123; i < states.length; ++i){
           retArr[i-123] = Integer.valueOf(states[i]);
       }
       
       return retArr;
    }
}
