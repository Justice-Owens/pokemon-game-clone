package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class GroundObject extends SuperObject{


    public GroundObject(int worldX, int worldY, String name) {
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.collision = true;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ground_item.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public GroundObject(){
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ground_item.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
