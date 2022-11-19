package main;

import entity.Entity;
import object.KeyObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B;
    BufferedImage image;
    BufferedImage displayBox;

    public boolean messageOn = false;
    public String message = "";
    int messageTimer = 0;
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");
    public boolean gameOver = false;




    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        KeyObject key = new KeyObject();
        image = key.image;

        try {
            displayBox = ImageIO.read(getClass().getResourceAsStream("/ui/display_box.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void showMessage (String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){

        g2.drawImage(displayBox, 15 * gp.tileSize - 2, 0, 50, 196, null);

        if(gp.caughtPokemon.size() > 0) {
            for (int i = 0; i < gp.caughtPokemon.size(); i++) {
                g2.drawImage(gp.caughtPokemon.get(i).image, 15 * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
            }
        }

        // GAME OVER SCREEN
        if (gameOver){
            g2.setFont(arial_40);
            g2.setColor(Color.white);

            String text;
            int textLength, x, y;

            text = "You caught 'em all!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 - (gp.tileSize * 3);

            g2.drawString(text, x, y);

            g2.setFont(arial_80B);
            g2.setColor(Color.yellow);
            text = "Game Finished!";
            textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

            x = gp.screenWidth/2 - textLength/2;
            y = gp.screenHeight/2 + (gp.tileSize * 2);

            g2.drawString(text, x, y);

            gp.gameThread = null;

        } else {
            // HUD
            this.g2 = g2;

            g2.setFont(arial_40);
            g2.setColor(Color.white);
            g2.drawImage(image, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);
            g2.drawString(" x " + gp.player.hasKey, 74, 65);


            // PLAY TIME
            playTime += (double)1/60;

            // MESSAGE
            if(messageOn){

                g2.setFont(g2.getFont().deriveFont(30F));
                g2.drawString(message, gp.tileSize/2, gp.tileSize*5);
                messageTimer++;

                if(messageTimer > 120){
                    messageTimer = 0;
                    messageOn = false;
                }
            }
        }
    }
}
