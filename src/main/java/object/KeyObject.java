package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class KeyObject extends SuperObject{


    public KeyObject(int worldX, int worldY) {
        name = "key";
        this.worldX = worldX;
        this.worldY = worldY;
        this.collision = true;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ground_item.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public KeyObject(){
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ground_item.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
