package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Entity {

    GamePanel gp;
    public BufferedImage image;
    public BufferedImage sprite;
    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public String direction;
    public int spriteCounter = 0;
    public int battleSpriteCounter = 0;
    public int battleSpriteNum = 1;
    public int spriteNum = 1;
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean isSolid = false;
    public int actionLockCounter = 0;
    public String name;

    public Entity(GamePanel gp, String name) {
        this.gp = gp;
        this.name = name;
    }
    public Entity(GamePanel gp){
        this.gp = gp;
    }


    public void setAction (){}

    public void update(){}

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + (gp.tileSize * 2) > gp.player.worldX - gp.player.screenX &&
                worldX - (gp.tileSize * 2) < gp.player.worldX + gp.player.screenX &&
                worldY + (gp.tileSize * 2) > gp.player.worldY - gp.player.screenY &&
                worldY - (gp.tileSize * 2) < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public void setDefaultValue(){}

}
