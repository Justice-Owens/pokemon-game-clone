package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX, screenY;

    public Player(GamePanel gp, KeyHandler keyH){

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize);
        screenY = gp.screenHeight/2 - (gp.tileSize);

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

        worldX = gp.tileSize * 20;
        worldY = gp.tileSize * 20;
        speed = 4;
        direction = "down";
    }
    public void update(){

        if(keyH.rightPressed == false && keyH.leftPressed == false && keyH.downPressed == false && keyH.upPressed == false){
            spriteNum = 1;
        }

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){
            if (keyH.upPressed == true){
                direction = "up";
                worldY -= speed;
            }
            else if (keyH.downPressed == true){
                direction = "down";
                worldY += speed;
            }
            else if (keyH.leftPressed == true){
                direction = "left";
                worldX -= speed;
            }
            else if (keyH.rightPressed == true){
                direction = "right";
                worldX += speed;
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
    public void draw(Graphics2D g2){

        BufferedImage image = null;
        switch (direction){
            case "up":
                if(spriteNum == 1) {
                    image = up1;
                }
                if(spriteNum == 2){
                    image = up2;
                }
                if(spriteNum == 3){
                    image = up1;
                }
                if(spriteNum == 4){
                    image = up3;
                }
                break;
            case "down":
                if(spriteNum == 1) {
                    image = down1;
                }
                if(spriteNum == 2){
                    image = down2;
                }
                if(spriteNum == 3){
                    image = down1;
                }
                if(spriteNum == 4){
                    image = down3;
                }
                break;
            case "left":
                if(spriteNum == 1) {
                    image = left1;
                }
                if(spriteNum == 2){
                    image = left2;
                }
                if(spriteNum == 3){
                    image = left1;
                }
                if(spriteNum == 4){
                    image = left3;
                }
                break;
            case "right":
                if(spriteNum == 1) {
                    image = right1;
                }
                if(spriteNum == 2){
                    image = right2;
                }
                if(spriteNum == 3){
                    image = right1;
                }
                if(spriteNum == 4){
                    image = right3;
                }
                break;
        }
        g2.drawImage(image,screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
