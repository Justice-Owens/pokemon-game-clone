package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    KeyHandler keyH;
    public final int screenX, screenY;
    public int hasKey = 0;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize);
        screenY = gp.screenHeight/2 - (gp.tileSize);

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValue();
        getPlayerImage();
    }

    public void getPlayerImage(){

        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/up_stand.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/up_left.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/player/up_right.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/down_stand.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/down_left.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/player/down_right.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left_stand.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/left_left.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/left_right.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right_stand.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/right_left.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/right_right.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void setDefaultValue(){

        worldX = gp.tileSize * 44;
        worldY = gp.tileSize * 44;
        speed = 4;
        direction = "down";
    }
    public void update(){

        if(!keyH.rightPressed && !keyH.leftPressed && !keyH.downPressed && !keyH.upPressed){
            spriteNum = 1;
        }

        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            if (keyH.upPressed){
                direction = "up";
            }
            else if (keyH.downPressed){
                direction = "down";
            }
            else if (keyH.leftPressed){
                direction = "left";
            }
            else if (keyH.rightPressed){
                direction = "right";
            }

            // TILE COLLISION
            isSolid = false;
            gp.collisionDetector.checkTile(this);

            // OBJECT COLLISION
            int objectIndex = gp.collisionDetector.checkObject(this, true);
            pickUpObject(objectIndex);

            // ENTITY COLLISION
            int pokeIndex = gp.collisionDetector.checkEntity(this, gp.pokemon);
            interactPoke(pokeIndex);

            if(!isSolid){
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12){
                if(spriteNum == 1){
                    spriteNum = 2;
                } else if (spriteNum == 2){
                    spriteNum = 3;
                } else if (spriteNum == 3){
                    spriteNum = 4;
                } else if(spriteNum == 4){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void pickUpObject(int index){
        if(index != 999){
            String objectName = gp.obj.get(index).name;

            switch (objectName){
                case "key":
                    hasKey++;
                    gp.obj.remove(index);
                    // lower volume or pause music before playing SFX and then returning to original volume

                    gp.playSoundFX("receive-item");
                    gp.ui.showMessage("I found a pokeball!");

                    break;
                case "door":
                    if(hasKey > 0){
                        gp.obj.remove(index);
                        hasKey--;
                        gp.playSoundFX("door_enter");
                        gp.ui.showMessage("Door unlocked!");
                    }
                    break;
            }
        }
    }

    public void interactPoke(int index){
        if (index != 999) {
            String pokeName = gp.pokemon.get(index).name;

            if (hasKey > 0) {
                switch (pokeName) {
                    case "gastly" -> {
                        gp.caughtPokemon.add(gp.pokemon.get(index));
                        gp.pokemon.remove(index);
                        hasKey--;
                        gp.ui.showMessage("I caught a Gastly!");
                    }
                    case "treecko" -> {
                        gp.caughtPokemon.add(gp.pokemon.get(index));
                        gp.pokemon.remove(index);
                        hasKey--;
                        gp.ui.showMessage("I caught a Treecko!");
                    }
                    case "pikachu" -> {
                        gp.caughtPokemon.add(gp.pokemon.get(index));
                        gp.pokemon.remove(index);
                        hasKey--;
                        gp.ui.showMessage("I caught a Pikachu!");
                    }
                    case "voltorb" -> {
                        gp.caughtPokemon.add(gp.pokemon.get(index));
                        gp.pokemon.remove(index);
                        hasKey--;
                        gp.ui.showMessage("I caught a Voltorb!");
                    }
                }
            }
            if (gp.pokemon.size() <= 0){
                gp.ui.gameOver = true;
                gp.stopMusic();
                gp.playMusic("game-over");
            }
        }
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;
        switch (direction) {
            case "up" -> {
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                if (spriteNum == 3) {
                    image = up1;
                }
                if (spriteNum == 4) {
                    image = up3;
                }
            }
            case "down" -> {
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                if (spriteNum == 3) {
                    image = down1;
                }
                if (spriteNum == 4) {
                    image = down3;
                }
            }
            case "left" -> {
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                if (spriteNum == 3) {
                    image = left1;
                }
                if (spriteNum == 4) {
                    image = left3;
                }
            }
            case "right" -> {
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                if (spriteNum == 3) {
                    image = right1;
                }
                if (spriteNum == 4) {
                    image = right3;
                }
            }
        }
        g2.drawImage(image,screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
