package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CatchTool {
    int catchPhase = 0;
    Double catchSuccessRate;
    BufferedImage shake1, shake2, shake3;

    public CatchTool(double catchSuccessRate){
        this.catchSuccessRate = catchSuccessRate;
        setShakeImages();
    }

    public boolean pokeCatch(){
        return Math.random() <= catchSuccessRate;
    }
    public void setShakeImages(){
        try{
            shake1 = ImageIO.read(getClass().getResourceAsStream("/objects/poke_shake_1.png"));
            shake2 = ImageIO.read(getClass().getResourceAsStream("/objects/poke_shake_2.png"));
            shake3 = ImageIO.read(getClass().getResourceAsStream("/objects/poke_shake_3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
