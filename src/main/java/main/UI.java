package main;

import entity.Entity;
import entity.PokeNPC;
import object.KeyObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_30, arial_40, arial_80B;
    BufferedImage image;
    BufferedImage displayBox;

    public boolean messageOn = false;
    public String message = "";
    int messageTimer = 0;
    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");
    public boolean gameOver = false;
    private Entity pokemon;
    private Entity player;
    int battlePhase = 0;
    String menuSelect = "CATCH";
    int index = 999;
    int catchPhase = 0;
    int throwX = 0;
    int throwY = 0;
    int animationCounter = 0;
    int spriteCounter = 0;
    CatchTool catchTool = new CatchTool(0.8);





    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        arial_30 = new Font("Arial", Font.PLAIN, 30);
        KeyObject key = new KeyObject();
        image = key.image;

        try {
            displayBox = ImageIO.read(getClass().getResourceAsStream("/ui/display_box.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throwX = gp.tileSize * 4;
        throwY = gp.tileSize * 5;

    }

    public void showMessage (String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;



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

        g2.drawImage(displayBox, 15 * gp.tileSize - 2, 0, 50, 293, null);

        if(gp.caughtPokemon.size() > 0) {
            for (int i = 0; i < gp.caughtPokemon.size(); i++) {
                g2.drawImage(gp.caughtPokemon.get(i).image, 15 * gp.tileSize, i * gp.tileSize, gp.tileSize, gp.tileSize, null);
            }
        }
        if(gp.isPaused && !gp.isBattle){
            drawPauseScreen();
        } else {}

        if(gp.isBattle){
            gp.isPaused = true;
//            gp.playMusic("");
            drawBattleScreen();
        }
    }

    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        g2.setColor(Color.WHITE);
        String text = "PAUSED";
        int x = getXCenterScreen(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public int getXCenterScreen(String text){
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - textLength/2;

        return x;
    }
    public void startBattle(Entity pokemon, Entity player, int index){
        gp.isBattle = true;
        this.pokemon = pokemon;
        this.player = player;
        this.index = index;
    }
    public void drawBattleScreen(){


        //BATTLE WINDOW
        int x = 0;
        int y = 0;
        int playerSpriteX = gp.tileSize ;
        int playerSpriteY = gp.tileSize * 5;


        drawSubWindow(x, y, gp.screenWidth, gp.screenHeight);

        drawSubWindow(0, gp.screenHeight-200, gp.screenWidth, 200);

        int dialogueX = gp.tileSize;
        int dialogueY = gp.screenHeight-100;

        int pokeSpriteX = gp.tileSize * 12;
        int pokeSpriteY = gp.tileSize ;

        int pokeBallSpriteX = pokeSpriteX + gp.tileSize;
        int pokeBallSpriteY = pokeSpriteY + gp.tileSize *2;


        //POKEMON APPEARS
        if(battlePhase == 0){
            drawSprite(pokemon.sprite, pokeSpriteX, pokeSpriteY, gp.tileSize *3, gp.tileSize*3);
            drawSprite(player.sprite, playerSpriteX, playerSpriteY, gp.tileSize * 3, gp.tileSize * 3);
            drawText("A Wild " + pokemon.name.toUpperCase() + " Appeared!", dialogueX, dialogueY, arial_30, Color.BLACK);
            if(gp.keyH.enterPressed){
                battlePhase = 1;
                gp.keyH.enterPressed = false;
            }
        }

        //CHOICE WINDOW
        if(battlePhase == 1){
            drawSprite(pokemon.sprite, pokeSpriteX, pokeSpriteY, gp.tileSize *3, gp.tileSize*3);
            drawSprite(player.sprite, playerSpriteX, playerSpriteY, gp.tileSize * 3, gp.tileSize * 3);
            drawText("WHAT WILL YOU DO?", dialogueX, dialogueY, arial_30, Color.BLACK);
            drawSubWindow(gp.tileSize * 9, gp.screenHeight-200, gp.screenWidth-gp.tileSize*9, 200);
            drawText("CATCH", gp.tileSize*9+50, gp.screenHeight-125, arial_30, Color.BLACK);
            drawText("RUN", gp.tileSize*14-20, gp.screenHeight-125, arial_30, Color.BLACK);

            if (gp.keyH.rightPressed && menuSelect.equals("CATCH")){
                menuSelect = "RUN";
            }
            if(gp.keyH.leftPressed && menuSelect.equals("RUN")){
                menuSelect = "CATCH";
            }

            switch (menuSelect){
                case "CATCH" -> {
                    drawSelectTriangle(gp.tileSize*9 +25, gp.screenHeight-130);
                }
                case "RUN" -> {
                    drawSelectTriangle(gp.tileSize*13, gp.screenHeight-130);
                }
            }
            if(gp.keyH.enterPressed) {
                if ((menuSelect.equals("CATCH") && gp.player.hasKey > 0) || menuSelect.equals("RUN")) {
                    battlePhase = 2;
                    gp.keyH.enterPressed = false;
                } else {
                    battlePhase = 7;
                    gp.keyH.enterPressed = false;
                }
            }
        }

        //ANIMATIONS FOR THROW
        if(battlePhase == 2){

            switch (menuSelect){
                case "CATCH" -> {
                    drawSprite(player.sprite, playerSpriteX, playerSpriteY, gp.tileSize * 3, gp.tileSize * 3);
                    drawSprite(pokemon.sprite, pokeSpriteX, pokeSpriteY, gp.tileSize *3, gp.tileSize*3);
                    g2.drawImage(image, throwX, throwY, gp.tileSize / 2, gp.tileSize / 2, null);

                    throwY-=2;
                    throwX+=8;

                    if(throwX >= pokeSpriteX) {
                        throwX = gp.tileSize * 4;
                        throwY = gp.tileSize * 5;
                        gp.player.hasKey--;
                        battlePhase = 4;
                    }
                }
                case "RUN" -> {
                    battlePhase = 3;
                }
            }
        }

        //ESCAPE DIALOGUE
        if (battlePhase == 3){
            drawSprite(pokemon.sprite, pokeSpriteX, pokeSpriteY, gp.tileSize *3, gp.tileSize*3);
            drawSprite(player.sprite, playerSpriteX, playerSpriteY, gp.tileSize * 3, gp.tileSize * 3);
            drawText("You ran away!", dialogueX, dialogueY, arial_40, Color.BLACK);
            if (gp.keyH.enterPressed){
                gp.keyH.enterPressed = false;
                gp.isBattle = false;
                gp.isPaused = false;
                battlePhase = 0;
            }
        }

        //CATCH PHASE
        if(battlePhase == 4){
            boolean checkCatch = false;

            drawSprite(player.sprite, playerSpriteX, playerSpriteY, gp.tileSize * 3, gp.tileSize * 3);

            switch(spriteCounter){
                case 0 -> {
                    g2.drawImage(image, pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                }
                case 1 -> {
                    g2.drawImage(catchTool.shake1, pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                }
                case 2 -> {
                    g2.drawImage(catchTool.shake2, pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                }
                case 3 -> {
                    g2.drawImage(catchTool.shake1, pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                }
                case 4 -> {
                    g2.drawImage(image, pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                }
                case 5 -> {
                    g2.drawImage(catchTool.shake3, pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                }
            }

            //POKEBALL SHAKE ANIMATION
            animationCounter++;
            if (animationCounter > 12) {
                if (spriteCounter == 0) {
                    spriteCounter = 1;
                    animationCounter = 0;
                } else if (spriteCounter == 1) {
                    spriteCounter = 2;
                    animationCounter = 0;
                } else if (spriteCounter == 2) {
                    spriteCounter = 3;
                    animationCounter = 0;
                } else if (spriteCounter == 3) {
                    spriteCounter = 4;
                    animationCounter = 0;
                } else if (spriteCounter == 4) {
                    spriteCounter = 5;
                    animationCounter = 0;
                } else if (spriteCounter == 5) {
                    spriteCounter = 0;
                    animationCounter = 0;
                    checkCatch = true;
                }
            }

            //CATCH SUCCESS CHECK
            if(checkCatch){
                if(catchTool.pokeCatch()){
                    catchPhase++;
                }
                else {
                    battlePhase = 6;
                }
            }
            if (catchPhase == 3){
                catchPhase = 0;
                battlePhase = 5;
            }
        }

        //POKEMON CAUGHT
        if(battlePhase == 5){
            drawSprite(player.sprite, playerSpriteX, playerSpriteY, gp.tileSize * 3, gp.tileSize * 3);
            drawText("Wild " + pokemon.name.toUpperCase() + " was caught!", dialogueX, dialogueY, arial_30, Color.BLACK);
            g2.drawImage(image, pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);


            if(gp.keyH.enterPressed){
                gp.caughtPokemon.add(gp.pokemon.get(index));
                gp.pokemon.remove(index);
                gp.keyH.enterPressed = false;
                gp.isBattle = false;
                gp.isPaused = false;
                battlePhase = 0;

                if (gp.pokemon.size() <= 0){
                    gp.ui.gameOver = true;
                    gp.stopMusic();
                    gp.playMusic("game-over");
                }
            }
        }

        //POKEMON ESCAPES POKEBALL
        if(battlePhase == 6){
            drawSprite(player.sprite, playerSpriteX, playerSpriteY, gp.tileSize * 3, gp.tileSize * 3);
            drawSprite(pokemon.sprite, pokeSpriteX, pokeSpriteY, gp.tileSize *3, gp.tileSize*3);
            drawText(pokemon.name.toUpperCase() + " broke free!", dialogueX, dialogueY, arial_30, Color.BLACK);
            if(gp.keyH.enterPressed){
                gp.keyH.enterPressed = false;
                battlePhase = 1;
            }
        }

        //OUT OF POKEBALLS
        if(battlePhase == 7){
            drawText("You're out of pokeballs!", dialogueX, dialogueY, arial_30, Color.BLACK);
            drawSprite(pokemon.sprite, pokeSpriteX, pokeSpriteY, gp.tileSize *3, gp.tileSize*3);
            drawSprite(player.sprite, playerSpriteX, playerSpriteY, gp.tileSize * 3, gp.tileSize * 3);

            if(gp.keyH.enterPressed){
                gp.keyH.enterPressed = false;
                battlePhase = 1;
            }
        }
    }

    public void drawSubWindow(int x, int y, int width, int height){

        g2.setColor(new Color(0, 0, 0));
        g2.fillRoundRect(x+5, y+5, width-10, height-10, 35, 35);

        g2.setColor(new Color(255, 255, 255));
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+10, y+10, width-20, height-20, 25, 25);

        g2.setColor(new Color(255, 255, 255));
        g2.fillRoundRect(x+15, y+15, width-30, height-30, 15, 15);

    }
    public void drawText(String text, int x, int y,Font fontType, Color color){
        g2.setFont(fontType);
        g2.setColor(color);
        g2.drawString(text, x, y);
    }

    public void drawSelectTriangle(int x, int y){
        g2.setColor(Color.BLACK);
        Polygon triangle = new Polygon();
        triangle.npoints = 3;
        triangle.xpoints = new int[]{x, x, x+5};
        triangle.ypoints = new int[]{y, y-5, y-3};

        g2.drawPolygon(triangle);
    }

    public void drawSprite(BufferedImage image, int x, int y, int width, int height){
        g2.drawImage(image, x, y, width, height, null);
    }
}
