package tile;

import main.UtilTool;

import java.awt.image.BufferedImage;

public class Tile {

    String tileName;
    public BufferedImage image;
    public boolean collision = false;
    int side;


    public Tile (String name, BufferedImage image, boolean collision){
        this.tileName = name;
        this.image = image;
        this.collision = collision;

    }
}
