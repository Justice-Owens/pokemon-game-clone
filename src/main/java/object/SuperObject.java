package object;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + (gp.tileSize * 2) > gp.player.worldX - gp.player.screenX &&
            worldX - (gp.tileSize * 2) < gp.player.worldX + gp.player.screenX &&
            worldY + (gp.tileSize * 2) > gp.player.worldY - gp.player.screenY &&
            worldY - (gp.tileSize * 2) < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(image, screenX + gp.tileSize/4, screenY + gp.tileSize/4, gp.tileSize/2, gp.tileSize/2, null);
        }
    }
}
