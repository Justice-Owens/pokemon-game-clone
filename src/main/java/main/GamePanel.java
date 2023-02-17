package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;
import util.AssetSetter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 24; //16x16 tile
    final int scale = 2;

   public final int tileSize = originalTileSize * scale; //makes tile 48x48

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768px
    public final int screenHeight = tileSize * maxScreenRow; //576px

    public final int maxWorldCol = 100;
    public final int maxWorldRow = 100;
    int FPS = 60;

    // SYSTEM
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    public Sound music = new Sound(true, false);
    public Sound soundFX = new Sound(false, true);
    public CollisionDetector collisionDetector = new CollisionDetector(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player player = new Player(this,keyH);
    public ArrayList<SuperObject> obj = new ArrayList<>();
    public ArrayList<Entity> pokemon = new ArrayList<>();
    public ArrayList<Entity> caughtPokemon = new ArrayList<>();

    // GAME STATE
    public boolean isPaused = false;
    public boolean isBattle = false;


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void gameSetup(){

        assetSetter.placeObject();
        assetSetter.setNPC();
        playMusic("main_theme");
    }

    public void startGameThread (){

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/FPS; // 0.016667 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                // Update character position
                update();
                // Draw screen with updated information
                repaint();
                delta--;
                drawCount++;

            }

            if(timer >= 1000000000){
//                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){

        //PLAY STATE
        if(!isPaused){
            //PLAYER
            player.update();
            //NPC
            for(Entity entity : pokemon){
                if (entity != null){
                    entity.update();
                }
            }
        //PAUSE STATE
        }
        if(isBattle){
//            ui.drawBattleScene();
        }
    }
    @Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long drawStart = 0;
        if (keyH.debugCheckDrawTime){
            drawStart = System.nanoTime();
        }

        // TILES
        tileM.draw(g2);

        // OBJECTS
        for(SuperObject obj : obj){
            if(obj != null){
                obj.draw(g2, this);
            }
        }
        // POKEMON
        for (Entity poke : pokemon){
            if (poke != null){
                poke.draw(g2);
            }
        }
        // PLAYER
        player.draw(g2);

        // UI
        ui.draw(g2);

        long drawEnd = 0;
        if (keyH.debugCheckDrawTime) {
            drawEnd = System.nanoTime();
            long timeToDraw = drawEnd-drawStart;

            g2.setColor(Color.WHITE);
            g2.drawString("Draw Time: " + timeToDraw, 10, 400);
            System.out.println("Draw Time: " + timeToDraw);
        }


        g2.dispose();

    }


    public void playMusic(String fileName){

        music.setFile(fileName);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSoundFX(String fileName){
        soundFX.setFile(fileName);
        soundFX.play();
    }
}
