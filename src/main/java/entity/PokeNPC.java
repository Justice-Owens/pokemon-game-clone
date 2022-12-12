package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class PokeNPC extends Entity{
    public String name;

    public PokeNPC(GamePanel gp, String pokeName, int worldX, int worldY) {
        super(gp, pokeName);

        this.worldX = worldX * gp.tileSize;
        this.worldY = worldY * gp.tileSize;
        this.direction = "down";
        this.speed = 1;
        this.name = pokeName;

        getImage(pokeName);
        getSprite(pokeName);

        solidArea = new Rectangle(8 ,16,32,32);
    }
    public void getImage(String pokeName){

        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/npc/" + pokeName + ".png")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void getSprite(String pokeName){
        try{
            sprite = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprite/" + pokeName + ".png")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setAction(){

        actionLockCounter++;

        if(actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }
    public void update(){
        setAction();

        isSolid = false;
        gp.collisionDetector.checkTile(this);
        gp.collisionDetector.checkObject(this, false);
        gp.collisionDetector.checkPlayer(this);

        if(!isSolid){
            switch (direction) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }
    }
    public String getName() {
        return name;
    }
}
