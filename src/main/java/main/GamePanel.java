package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.TileObserver;

public class GamePanel extends JPanel implements Runnable {

    //Screen Settings
    final int originalTileSize = 24; //16x16 tile
    final int scale = 2;

   public final int tileSize = originalTileSize * scale; //makes tile 48x48

    public final int maxScreenCol = 18;
    public final int maxScreenRow = 14;
    public final int screenWidth = tileSize * maxScreenCol; //768px
    public final int screenHeight = tileSize * maxScreenRow; //576px

    public final int maxWorldCol = 40;
    public final int maxWorldRow = 40;
    public final int worldWidth = tileSize * maxScreenCol;
    public final int worldHeight = tileSize * maxScreenRow;

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public Player player = new Player(this,keyH);
    TileManager tileM = new TileManager(this);


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
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
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){

        player.update();

    }
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2);

        player.draw(g2);

        g2.dispose();


    }
}
