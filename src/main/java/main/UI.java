package main;

import entity.Entity;
import entity.Player;
import item.PokeballItem;
import object.GroundObject;
import util.CatchTool;
import item.PokeballType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class UI {

    enum PauseMenuOptions{
        POKEDEX,
        POKEMON,
        BAG,
        POKEGEAR,
        NAME,
        SAVE,
        OPTION,
        EXIT,
        INNER_MENU
    }

    enum OptionMenuOptions{
        AUDIO,
        VIDEO
    }

    enum BagScreenWindows{
        POKEBALLS
    }

    //Variables
    GamePanel gp;
    Graphics2D g2;
    Font arial_20, arial_20B, arial_30, arial_40, arial_80B;
    BufferedImage image;
    BufferedImage displayBox;
    BufferedImage shadow;
    BagScreenWindows currentWindow = BagScreenWindows.POKEBALLS;

    public boolean messageOn = false;
    public String message = "";
    int messageTimer = 0;
    double playTime;
    public boolean gameOver = false;
    private Entity pokemon;
    private Entity player;
    int battlePhase = 0;
    String menuSelect = "CATCH";
    int index = 999;
    int catchPhase = 0;
    int throwX;
    int throwY;
    int animationCounter = 0;
    int spriteCounter = 0;
    int partyIndex = 0;
    int pageNum = 0;
    int pauseSelectY;
    int optionSelectY;
    CatchTool catchTool;
    private int itemIndex = 0;
    private int inventoryIndex = itemIndex;
    private PauseMenuOptions menuSelection = PauseMenuOptions.POKEDEX;
    private OptionMenuOptions optionMenuSelection = OptionMenuOptions.AUDIO;
    private boolean displayPokedexScreen = false;
    private boolean displayPartyScreen = false;
    private boolean displayBagScreen = false;
    private boolean displayPokegearScreen = false;
    private boolean displayCharacterStatScreen = false;
    private boolean displaySaveScreen = false;
    private boolean displayOptionMenu = false;
    private boolean displayPokemonStatScreen = false;
    private boolean displayVolumeScreen = false;
    private final Color partyBackground = new Color(180, 237, 180),
            bagBackground = new Color(218, 219, 147);


    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        arial_30 = new Font("Arial", Font.PLAIN, 30);
        arial_20 = new Font("Arial", Font.PLAIN, 20);
        arial_20B = new Font("Arial", Font.BOLD, 20);
        GroundObject key = new GroundObject();
        image = key.image;
        catchTool  = new CatchTool(0.9);


        try {
            displayBox = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/ui/display_box.png")));
            shadow = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprite/shadow.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throwX = gp.tileSize * 4;
        throwY = gp.tileSize * 5;
        pauseSelectY = gp.tileSize - 10;
        optionSelectY = gp.tileSize - 10;

    }

    public void showMessage (String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        if(gp.keyH.autoCatchSuccess){
            showMessage("Auto Catch ON");
        }


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

        if(gp.isPaused && !gp.isBattle){
            drawPauseMenu();
            if(displayPokedexScreen){
                //TODO: drawPokedexScreen();
            }
            if(displayPartyScreen){
                drawPartyScreen();
                if(displayPokemonStatScreen){
                    drawPokemonStatScreen(gp.caughtPokemon.get(partyIndex));
                }
            }
            if(displayPokegearScreen){
                //TODO: drawPokegearScreen();
            }
            if(displayBagScreen){
                drawBagScreen();
            }
            if(displayCharacterStatScreen){
                //TODO: drawCharacterStatScreen();
            }
            if(displaySaveScreen){
                //TODO: drawSaveScreen();
            }
            if(displayOptionMenu){
                drawOptionMenu();
            }
        }

        if(gp.isBattle){
            gp.isPaused = true;
//            gp.playMusic("");
            drawBattleScreen();
        }
    }

    public void drawPauseMenu() {

        int pauseMenuX = gp.tileSize * 12;
        int pauseSelectX = pauseMenuX - gp.tileSize / 2;

        drawSubWindow(gp.tileSize * 11, gp.tileSize / 2 - 30, gp.tileSize * 5, gp.tileSize * 9);

        drawText(PauseMenuOptions.POKEDEX.toString(), pauseMenuX, gp.tileSize, arial_30, Color.BLACK);
        drawText(PauseMenuOptions.POKEMON.toString(), pauseMenuX, gp.tileSize * 2, arial_30, Color.BLACK);
        drawText(PauseMenuOptions.POKEGEAR.toString(), pauseMenuX, gp.tileSize * 3, arial_30, Color.BLACK);
        drawText(PauseMenuOptions.BAG.toString(), pauseMenuX, gp.tileSize * 4, arial_30, Color.BLACK);
        drawText(PauseMenuOptions.NAME.toString(), pauseMenuX, gp.tileSize * 5, arial_30, Color.BLACK);
        drawText(PauseMenuOptions.SAVE.toString(), pauseMenuX, gp.tileSize * 6, arial_30, Color.BLACK);
        drawText(PauseMenuOptions.OPTION.toString(), pauseMenuX, gp.tileSize * 7, arial_30, Color.BLACK);
        drawText(PauseMenuOptions.EXIT.toString(), pauseMenuX, gp.tileSize * 8, arial_30, Color.BLACK);

        switch (menuSelection) {
            case POKEDEX -> {
                drawSelectTriangle(pauseSelectX, pauseSelectY);
                if (gp.keyH.downPressed) {
                    pauseSelectY += gp.tileSize;
                    menuSelection = PauseMenuOptions.POKEMON;
                    gp.keyH.downPressed = false;
                }
                if (gp.keyH.upPressed) {
                    pauseSelectY = gp.tileSize * 8 - 10;
                    menuSelection = PauseMenuOptions.EXIT;
                    gp.keyH.upPressed = false;
                }
                if (gp.keyH.enterPressed) {
                    displayPokedexScreen = true;
                    //TODO: drawPokeDexScreen();
                    gp.keyH.enterPressed = false;
                }
            }
            case POKEMON -> {
                drawSelectTriangle(pauseSelectX, pauseSelectY);
                if (gp.keyH.downPressed) {
                    pauseSelectY += gp.tileSize;
                    menuSelection = PauseMenuOptions.POKEGEAR;
                    gp.keyH.downPressed = false;
                }
                if (gp.keyH.upPressed) {
                    pauseSelectY -= gp.tileSize;
                    menuSelection = PauseMenuOptions.POKEDEX;
                    gp.keyH.upPressed = false;
                }
                if (gp.keyH.enterPressed) {
                    displayPartyScreen = true;
                    gp.keyH.enterPressed = false;
                    menuSelection = PauseMenuOptions.INNER_MENU;
                }
            }
            case POKEGEAR -> {
                drawSelectTriangle(pauseSelectX, pauseSelectY);
                if (gp.keyH.downPressed) {
                    pauseSelectY += gp.tileSize;
                    menuSelection = PauseMenuOptions.BAG;
                    gp.keyH.downPressed = false;
                }
                if (gp.keyH.upPressed) {
                    pauseSelectY -= gp.tileSize;
                    menuSelection = PauseMenuOptions.POKEMON;
                    gp.keyH.upPressed = false;
                }
                if (gp.keyH.enterPressed) {
                    displayPokegearScreen = true;
                    //TODO: drawPokeGearScreen();
                    gp.keyH.enterPressed = false;
                }
            }
            case BAG -> {
                drawSelectTriangle(pauseSelectX, pauseSelectY);
                if (gp.keyH.downPressed) {
                    pauseSelectY += gp.tileSize;
                    menuSelection = PauseMenuOptions.NAME;
                    gp.keyH.downPressed = false;
                }
                if (gp.keyH.upPressed) {
                    pauseSelectY -= gp.tileSize;
                    menuSelection = PauseMenuOptions.POKEGEAR;
                    gp.keyH.upPressed = false;
                }
                if (gp.keyH.enterPressed) {
                    displayBagScreen = true;
                    menuSelection = PauseMenuOptions.INNER_MENU;
                    gp.keyH.enterPressed = false;
                }
            }
            case NAME -> {
                drawSelectTriangle(pauseSelectX, pauseSelectY);
                if (gp.keyH.downPressed) {
                    pauseSelectY += gp.tileSize;
                    menuSelection = PauseMenuOptions.SAVE;
                    gp.keyH.downPressed = false;
                }
                if (gp.keyH.upPressed) {
                    pauseSelectY -= gp.tileSize;
                    menuSelection = PauseMenuOptions.BAG;
                    gp.keyH.upPressed = false;
                }
                if (gp.keyH.enterPressed) {
                    pauseSelectY += gp.tileSize;
                    displayCharacterStatScreen = true;
                    //TODO: drawCharacterStatScreen();
                    gp.keyH.enterPressed = false;
                }
            }
            case SAVE -> {
                drawSelectTriangle(pauseSelectX, pauseSelectY);
                if (gp.keyH.downPressed) {
                    pauseSelectY += gp.tileSize;
                    menuSelection = PauseMenuOptions.OPTION;
                    gp.keyH.downPressed = false;
                }
                if (gp.keyH.upPressed) {
                    pauseSelectY -= gp.tileSize;
                    menuSelection = PauseMenuOptions.NAME;
                    gp.keyH.upPressed = false;
                }
                if (gp.keyH.enterPressed) {
                    displaySaveScreen = true;
                    //TODO: saveGame();
                    gp.keyH.enterPressed = false;
                }
            }
            case OPTION -> {
                drawSelectTriangle(pauseSelectX, pauseSelectY);
                if (gp.keyH.downPressed) {
                    pauseSelectY += gp.tileSize;
                    menuSelection = PauseMenuOptions.EXIT;
                    gp.keyH.downPressed = false;
                }
                if (gp.keyH.upPressed) {
                    pauseSelectY -= gp.tileSize;
                    menuSelection = PauseMenuOptions.SAVE;
                    gp.keyH.upPressed = false;
                }
                if (gp.keyH.enterPressed) {
                    displayOptionMenu = true;
                    gp.keyH.enterPressed = false;
                    menuSelection = PauseMenuOptions.INNER_MENU;

                }
            }
            case EXIT -> {
                drawSelectTriangle(pauseSelectX, pauseSelectY);
                if (gp.keyH.downPressed) {
                    pauseSelectY = gp.tileSize - 10;
                    menuSelection = PauseMenuOptions.POKEDEX;
                    gp.keyH.downPressed = false;
                }
                if (gp.keyH.upPressed) {
                    pauseSelectY -= gp.tileSize;
                    menuSelection = PauseMenuOptions.OPTION;
                    gp.keyH.upPressed = false;
                }
                if (gp.keyH.enterPressed) {
                    gp.isPaused = false;
                    gp.keyH.enterPressed = false;
                    menuSelection = PauseMenuOptions.POKEDEX;
                }
            }
            case INNER_MENU -> {}
        }
    }

    public void startBattle(Entity pokemon, Player player, int index){
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

        //BATTLE HUD
        drawEnemyPokemonBox();

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
                case "CATCH" -> drawSelectTriangle(gp.tileSize*9 +25, gp.screenHeight-130);
                case "RUN" -> drawSelectTriangle(gp.tileSize*13, gp.screenHeight-130);
            }
            if(gp.keyH.enterPressed) {
                if ((menuSelect.equals("CATCH") && gp.player.iManager.getPokeball(PokeballType.STANDARD) > 0) || menuSelect.equals("RUN")) {
                    battlePhase = 2;
                } else {
                    battlePhase = 7;
                }
                gp.keyH.enterPressed = false;
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
                        gp.player.iManager.throwPokeball(PokeballType.STANDARD);
                        battlePhase = 4;
                    }
                }
                case "RUN" -> battlePhase = 3;
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
                case 0 -> g2.drawImage(image, pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                case 1 -> g2.drawImage(catchTool.getShake1(), pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                case 2 -> g2.drawImage(catchTool.getShake2(), pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                case 3 -> g2.drawImage(catchTool.getShake1(), pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                case 4 -> g2.drawImage(image, pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
                case 5 -> g2.drawImage(catchTool.getShake3(), pokeBallSpriteX, pokeBallSpriteY, gp.tileSize/2, gp.tileSize/2, null);
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

                if (gp.pokemon.size() == 0){
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
    public void drawSubWindow(int x, int y, int width, int height, Color background){

        g2.setColor(new Color(0, 0, 0));
        g2.fillRoundRect(x+5, y+5, width-10, height-10, 35, 35);

        g2.setColor(new Color(255, 255, 255));
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+10, y+10, width-20, height-20, 25, 25);

        g2.setColor(background);
        g2.fillRoundRect(x+15, y+15, width-30, height-30, 15, 15);

    }
    public void drawText(String text, int x, int y,Font fontType, Color color){
        g2.setFont(fontType);
        g2.setColor(color);
        g2.drawString(text, x, y);
    }

    public void drawText(String text, int x, int y, int lineLength, Font fontType, Color color){
        StringBuilder stringToDraw = new StringBuilder();
        StringBuilder trimmedLine = new StringBuilder();
        String[] words = text.split(" ");

        g2.setFont(fontType);
        g2.setColor(color);

        for(String s: words){
            if(trimmedLine.length() + 1 + s.length() <= lineLength){
                trimmedLine.append(s).append(" ");
            } else {
                g2.drawString(trimmedLine.toString(), x, y);
                y += fontType.getSize();
                trimmedLine = new StringBuilder();
                trimmedLine.append(s).append(" ");
            }
        }
        if(trimmedLine.length() > 0){
            g2.drawString(trimmedLine.toString(), x, y);
        }
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

    public void drawShadow(int x, int y, int width, int height){
        g2.drawImage(shadow, x, y, width, height, null);
    }

    public void drawHealthBar(int currentHealth, int totalHealth, int x, int y){
        if(currentHealth > totalHealth * 0.5){
            g2.setColor(Color.GREEN);
        } else if (currentHealth > totalHealth * 0.25){
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(Color.RED);
        }

        g2.fillRect(x, y-15, (gp.tileSize * 4) * (currentHealth/totalHealth), 15);
    }

    public void drawEnemyPokemonBox(){
        drawSubWindow(20,20, gp.tileSize * 8, gp.tileSize * 2);

        drawText(pokemon.name.toUpperCase(),50, gp.tileSize + 15, arial_20, Color.BLACK);
        drawText("Lvl: " + pokemon.getCurrentLevel(), 50, gp.tileSize + 40, arial_20, Color.BLACK);

        drawText("HP: ", gp.tileSize * 3, gp.tileSize + 40, arial_20, Color.BLACK);

        drawHealthBar(pokemon.getCurrentHP(), pokemon.getTotalHP(), gp.tileSize * 3 + 40, gp.tileSize + 40);
    }

    public void drawPartyScreen(){
        int selectX = gp.tileSize;
        int selectY = gp.tileSize + 15;

        g2.fillRect(0, 0, gp.tileSize * 16, gp.tileSize * 12);
        drawSubWindow(0, 0, gp.tileSize * 16, gp.tileSize * 12, partyBackground);

        int pokemonWindowX = 15;
        int pokemonWindowY = 12;
        int pokemonWindowWidth = gp.tileSize * 16 - 30;
        int pokemonWindowHeight = gp.tileSize * 2;
        int pokemonWindowYDelta = pokemonWindowHeight - 5;

        for(int i = 0; i < 6; i++){
            drawSubWindow(pokemonWindowX, pokemonWindowY, pokemonWindowWidth, pokemonWindowHeight);
            pokemonWindowY += pokemonWindowYDelta;
        }

        int pokemonIconX = gp.tileSize + 25;
        int pokemonIconY = gp.tileSize - 10;
        int pokemonSpriteSize = gp.tileSize;

        int pokemonNameX = gp.tileSize * 3;
        int pokemonNameY = gp.tileSize * 2 - 35;

        int pokemonHealthBarX = gp.tileSize * 5;
        int pokemonHealthBarY = gp.tileSize * 2 - 15;

        int pokemonLevelX = gp.tileSize * 3;
        int pokemonLevelY = gp.tileSize * 2 - 15;

        if(gp.caughtPokemon.size() > 0) {
            for (Entity e : gp.caughtPokemon) {
                g2.drawImage(e.image, pokemonIconX, pokemonIconY, pokemonSpriteSize, pokemonSpriteSize, null);
                drawText(e.name.toUpperCase(), pokemonNameX, pokemonNameY, arial_20, Color.BLACK);
                drawHealthBar(e.getCurrentHP(),e.getTotalHP(), pokemonHealthBarX, pokemonHealthBarY);
                drawText("Lvl: " + String.valueOf(e.getCurrentLevel()), pokemonLevelX, pokemonLevelY, arial_20, Color.BLACK);
                drawText("HP: " + e.getCurrentHP() + "/" + e.getTotalHP(), pokemonHealthBarX + gp.tileSize * 4 + 20,
                        pokemonHealthBarY, arial_20, Color.BLACK);

                pokemonIconY += pokemonWindowYDelta;
                pokemonLevelY += pokemonWindowYDelta;
                pokemonHealthBarY += pokemonWindowYDelta;
                pokemonNameY += pokemonWindowYDelta;
            }
        }

        switch(partyIndex){
            case 0 -> drawSelectTriangle(selectX, selectY);
            case 1 -> drawSelectTriangle(selectX, selectY + pokemonWindowYDelta);
            case 2 -> drawSelectTriangle(selectX, selectY + pokemonWindowYDelta * 2);
            case 3 -> drawSelectTriangle(selectX, selectY + pokemonWindowYDelta * 3);
            case 4 -> drawSelectTriangle(selectX, selectY + pokemonWindowYDelta * 4);
            case 5 -> drawSelectTriangle(selectX, selectY + pokemonWindowYDelta * 5);
        }

        if(!gp.caughtPokemon.isEmpty()) {
            if (gp.keyH.upPressed) {
                if (partyIndex == 0) {
                    partyIndex = gp.caughtPokemon.size() - 1;
                } else {
                    partyIndex--;
                }
                gp.keyH.upPressed = false;
            }
            if (gp.keyH.downPressed) {
                if (partyIndex == gp.caughtPokemon.size() - 1) {
                    partyIndex = 0;
                } else {
                    partyIndex++;
                }
                gp.keyH.downPressed = false;
            }
        } else {
            partyIndex = 0;
        }

        if(gp.keyH.escapePressed){
            displayPartyScreen = false;
            displayPokemonStatScreen = false;
            gp.keyH.escapePressed = false;
            menuSelection = PauseMenuOptions.POKEMON;
        }

        if(gp.keyH.enterPressed && partyIndex + 1 <= gp.caughtPokemon.size()){
            displayPokemonStatScreen = true;
            gp.keyH.enterPressed = false;
        }
    }

    public void drawPokemonStatScreen(Entity pokemon){
        //Variables
        int statX = gp.tileSize * 9 + 25;
        int statY = gp.tileSize + 20;
        int statNumX = gp.tileSize * 14;


        //Subwindows
        g2.fillRect(0, 0, gp.tileSize * 16, gp.tileSize * 12);
        drawSubWindow(0, 0, gp.tileSize * 16, gp.tileSize * 12, pokemon.getBackgroundStatColor());
        drawSubWindow(gp.tileSize/2, gp.tileSize/2, gp.tileSize*8, gp.tileSize*6);                          //Sprite Window
        drawSubWindow(gp.tileSize * 9, gp.tileSize/2, gp.tileSize * 6 + gp.tileSize/2, gp.tileSize * 11);   //Right Long Window
        drawSubWindow(gp.tileSize/2, gp.tileSize * 7, gp.tileSize * 8, gp.tileSize * 4 + gp.tileSize/2);    //Bottom Left Small Window

        //Pokemon Sprite and Persistent Info
        if(!Objects.equals(pokemon.getType1(), "GHOST") && !Objects.equals(pokemon.getType2(), "GHOST")) {
            drawShadow(gp.tileSize * 3, gp.tileSize * 2, gp.tileSize * 3, gp.tileSize * 3);
        }
        g2.drawImage(pokemon.sprite, gp.tileSize*3, gp.tileSize*2, gp.tileSize * 3, gp.tileSize * 3, null);
        drawText(pokemon.name.toUpperCase(), gp.tileSize * 3 + 40, gp.tileSize + 20, arial_20, Color.BLACK);
        drawText("LVL: " + pokemon.getCurrentLevel(), gp.tileSize + 20, gp.tileSize + 20, arial_20, Color.BLACK);
        drawText(pokemon.getDescription(), gp.tileSize, gp.tileSize * 8, 36, arial_20, Color.BLACK);


        //Logic For Navigating Pages
        if(gp.keyH.leftPressed && pageNum > 0){
            pageNum--;
            gp.keyH.leftPressed = false;
        }
        if(gp.keyH.rightPressed){
            pageNum++;
            gp.keyH.rightPressed = false;
        }

        if(pageNum == 0){

            // Stats
            drawText("HP: ", gp.tileSize + 20, gp.tileSize * 6, arial_20, Color.BLACK);
            drawHealthBar(pokemon.getCurrentHP(), pokemon.getTotalHP(), gp.tileSize * 2 + 20, gp.tileSize * 6);
            drawText(pokemon.getCurrentHP() + "/" + pokemon.getTotalHP(), gp.tileSize * 6 + 30, gp.tileSize * 6, arial_20, Color.BLACK);

            drawText("ATTACK: ", statX, statY, arial_30, Color.BLACK);
            drawText("DEFENSE: ", statX, statY * 2, arial_30, Color.BLACK);
            drawText("SP. ATK: ", statX, statY * 3, arial_30, Color.BLACK);
            drawText("SP. DEF: ", statX, statY * 4, arial_30, Color.BLACK);
            drawText("SPEED: ", statX, statY * 5, arial_30, Color.BLACK);
            drawText("TYPE: ", statX, statY * 6, arial_30, Color.BLACK);

            drawText(String.valueOf(pokemon.getAttack()), statNumX, statY, arial_30, Color.BLACK);
            drawText(String.valueOf(pokemon.getDefense()), statNumX, statY * 2, arial_30, Color.BLACK);
            drawText(String.valueOf(pokemon.getSpecialAttack()), statNumX, statY * 3, arial_30, Color.BLACK);
            drawText(String.valueOf(pokemon.getSpecialDefense()), statNumX, statY * 4, arial_30, Color.BLACK);
            drawText(String.valueOf(pokemon.getSpeedStat()), statNumX, statY * 5, arial_30, Color.BLACK);

            if(pokemon.doesHasTwoTypes()){
                drawText(pokemon.getType1() + "/" + pokemon.getType2(), statNumX - gp.tileSize * 2 - 20, statY * 6, arial_20, Color.BLACK);
            } else {
                drawText(pokemon.getType1(), statNumX - gp.tileSize * 2, statY * 6, arial_30, Color.BLACK);
            }

        }
        if(pageNum == 1){
            statNumX -= (gp.tileSize + gp.tileSize/2);
            drawText("MOVE 1: ", statX, statY, arial_30, Color.BLACK);
            drawText("MOVE 2: ", statX, statY * 2, arial_30, Color.BLACK);
            drawText("MOVE 3: ", statX, statY * 3, arial_30, Color.BLACK);
            drawText("MOVE 4: ", statX, statY * 4, arial_30, Color.BLACK);

            drawText("PP: 25/25", statNumX, statY + 30, arial_20B, Color.BLACK);
            drawText("PP: 25/25", statNumX, statY * 2 + 30, arial_20B, Color.BLACK);
            drawText("PP: 25/25", statNumX, statY * 3 + 30, arial_20B, Color.BLACK);
            drawText("PP: 25/25", statNumX, statY * 4 + 30, arial_20B, Color.BLACK);
        }

        if(pageNum == 2){
            displayPokemonStatScreen = false;
            pageNum = 0;
        }
    }

    public void drawBagScreen(){
        //VARIABLES
        int descriptionX = gp.tileSize/2;
        int descriptionY = gp.tileSize * 7;
        int descriptionWidth = gp.tileSize * 8;
        int descriptionHeight = gp.tileSize * 4 + gp.tileSize/2;

        int descriptionTextX = gp.tileSize;
        int descriptionTextY = gp.tileSize * 8;

        int inventoryX = gp.tileSize * 9;
        int inventoryY = gp.tileSize/2;
        int inventoryWidth = gp.tileSize * 6 + gp.tileSize/2;
        int inventoryHeight = gp.tileSize * 11;

        int inventoryTextX = gp.tileSize * 10;
        int inventoryTextY = gp.tileSize + 30;

        int inventorySelectTriX = inventoryTextX - 25;
        int inventorySelectTriY = inventoryTextY - 5;
        int inventorySelectTriYDelta = 30;

        int imageX = gp.tileSize * 3;
        int imageY = gp.tileSize * 2;
        int imageWidth = gp.tileSize * 2;
        int imageHeight = gp.tileSize * 2;

        int subBagX = gp.tileSize/2;
        int subBagY = gp.tileSize * 5 + gp.tileSize/2;
        int subBagWidth = gp.tileSize * 8;
        int subBagHeight = gp.tileSize + gp.tileSize/2;

        int subBagTextX = subBagX + gp.tileSize * 2 + 30;
        int subBagTextY = subBagY + gp.tileSize - 5;


        //SUBWINDOWS
        g2.fillRect(0, 0, gp.tileSize * 16, gp.tileSize * 12);
        drawSubWindow(0, 0, gp.tileSize * 16, gp.tileSize * 12, bagBackground);
        drawSubWindow(descriptionX, descriptionY, descriptionWidth, descriptionHeight);
        drawSubWindow(inventoryX, inventoryY, inventoryWidth, inventoryHeight);
        drawSubWindow(subBagX, subBagY, subBagWidth, subBagHeight);

        //PERSISTENT METHOD CALLS
        drawSelectTriangle(inventorySelectTriX, inventorySelectTriY + inventoryIndex * inventorySelectTriYDelta);

        //ITEM WINDOW
        drawText(currentWindow.toString(), subBagTextX - currentWindow.toString().length()/2, subBagTextY, arial_20B, Color.BLACK);
        switch (currentWindow) {
            case POKEBALLS -> {
                PokeballItem selectedItem;
                List<String[]> pokeballList = new ArrayList<>();
                for(Map.Entry<PokeballItem, Integer> entry: gp.player.iManager.getPokeballs().entrySet()) {
                    pokeballList.add(new String[]{entry.getKey().getTypeName(), String.valueOf(entry.getValue())});
                }
                drawPokeballList(pokeballList);
                switch (pokeballList.get(inventoryIndex + itemIndex)[0]){
                    case "standard" -> {
                        selectedItem = new PokeballItem(PokeballType.STANDARD);
                        drawShadow(imageX - gp.tileSize/2, imageY - gp.tileSize - gp.tileSize/3, imageWidth + gp.tileSize, gp.tileSize * 4);
                        g2.drawImage(selectedItem.getImage(), imageX, imageY, imageWidth, imageHeight, null);
                        drawText(selectedItem.getDescription(), descriptionTextX, descriptionTextY, 39, arial_20, Color.BLACK);
                    }
                    case "Great" -> {
                        selectedItem = new PokeballItem(PokeballType.GREAT_BALL);
                        drawShadow(imageX - gp.tileSize/2, imageY - gp.tileSize - gp.tileSize/3, imageWidth + gp.tileSize, gp.tileSize * 4);
                        g2.drawImage(selectedItem.getImage(), imageX, imageY, imageWidth, imageHeight, null);
                        drawText(selectedItem.getDescription(), descriptionTextX, descriptionTextY, 39, arial_20, Color.BLACK);
                    }
                    case "Ultra" -> {
                        selectedItem = new PokeballItem(PokeballType.ULTRA_BALL);
                        drawShadow(imageX - gp.tileSize/2, imageY - gp.tileSize - gp.tileSize/3, imageWidth + gp.tileSize, gp.tileSize * 4);
                        g2.drawImage(selectedItem.getImage(), imageX, imageY, imageWidth, imageHeight, null);
                        drawText(selectedItem.getDescription(), descriptionTextX, descriptionTextY, 39, arial_20, Color.BLACK);
                    }
                    case "Master" -> {
                        selectedItem = new PokeballItem(PokeballType.MASTER_BALL);
                        drawShadow(imageX - gp.tileSize/2, imageY - gp.tileSize - gp.tileSize/3, imageWidth + gp.tileSize, gp.tileSize * 4);
                        g2.drawImage(selectedItem.getImage(), imageX, imageY, imageWidth, imageHeight, null);
                        drawText(selectedItem.getDescription(), descriptionTextX, descriptionTextY, 39, arial_20, Color.BLACK);
                    }
                }


                //LOGIC FOR NAVIGATING MENU
                if (gp.keyH.downPressed && inventoryIndex < pokeballList.size() - 1) {
                    inventoryIndex++;
                } else if(gp.keyH.downPressed){
                    itemIndex = 0;
                    inventoryIndex = 0;
                }
                gp.keyH.downPressed = false;
                if (gp.keyH.upPressed && inventoryIndex > 0){
                    inventoryIndex--;
                } else if (gp.keyH.upPressed){
                    inventoryIndex = pokeballList.size() - 1;
                }
                gp.keyH.upPressed = false;
            }
        }

        //DESCRIPTION WINDOW
        //drawText(, descriptionTextX, descriptionTextY, arial_30, Color.BLACK);

        //EXIT
        if(gp.keyH.escapePressed){
            menuSelection = PauseMenuOptions.BAG;
            displayBagScreen = false;
            gp.keyH.escapePressed = false;
        }
    }

    public void drawPokeballList(List<String[]> pokeballList){
        int inventoryX = gp.tileSize * 9;
        int inventoryTextX = gp.tileSize * 10;
        int inventoryTextY = gp.tileSize + 30;
        int inventoryTextXDelta = gp.tileSize * 5;

        if(pokeballList.size() <= 10){
            for (String[] strings : pokeballList) {
                if (Integer.parseInt(strings[1]) > 0) {
                    if (!strings[0].equalsIgnoreCase("STANDARD")) {
                        drawText(strings[0] + " Ball", inventoryTextX, inventoryTextY, arial_20, Color.BLACK);
                        drawText(strings[1], inventoryX + inventoryTextXDelta, inventoryTextY, arial_20B, Color.BLACK);
                    } else {
                        drawText("Poke Ball", inventoryTextX, inventoryTextY, arial_20, Color.BLACK);
                        drawText(strings[1], inventoryX + inventoryTextXDelta, inventoryTextY, arial_20B, Color.BLACK);
                    }
                    inventoryTextY += 30;
                }
            }
        } else {
            for (int i = itemIndex; i < pokeballList.size(); i++) {
                if (Integer.parseInt(pokeballList.get(i)[1]) > 0) {
                    if (!pokeballList.get(i)[0].equals("STANDARD")) {
                        drawText(pokeballList.get(i)[0] + " Ball", inventoryTextX, inventoryTextY, arial_20, Color.BLACK);
                        drawText(pokeballList.get(i)[1], inventoryX + inventoryTextXDelta, inventoryTextY, arial_20B, Color.BLACK);
                    } else {
                        drawText("Poke Ball", inventoryTextX, inventoryTextY, arial_20, Color.BLACK);
                        drawText(pokeballList.get(i)[1], inventoryX + inventoryTextXDelta, inventoryTextY, arial_20B, Color.BLACK);
                    }
                    inventoryTextY += 30;
                }
                if (i >= inventoryIndex + 10) {
                    break;
                }
            }
        }
    }

    public void drawOptionMenu(){
        //VARIABLES
        int optionMenuX = 12 * gp.tileSize;
        int optionMenuSelectX = optionMenuX - gp.tileSize / 2;

        //WINDOW AND TEXT
        drawSubWindow(gp.tileSize * 11, gp.tileSize / 2 - 30, gp.tileSize * 5, gp.tileSize * 9);
        drawText(OptionMenuOptions.AUDIO.toString(), optionMenuX, gp.tileSize, arial_30, Color.BLACK);
        drawText(OptionMenuOptions.VIDEO.toString(), optionMenuX, gp.tileSize * 2, arial_30, Color.BLACK);

        //OPTION MENU SELECTION
        switch (optionMenuSelection){
            case AUDIO -> {
                drawSelectTriangle(optionMenuSelectX, optionSelectY);
                if (gp.keyH.downPressed) {
                    optionMenuSelection = OptionMenuOptions.VIDEO;
                    gp.keyH.downPressed = false;
                    optionSelectY += gp.tileSize;
                }
                if (gp.keyH.upPressed) {
                    optionMenuSelection = OptionMenuOptions.VIDEO;
                    gp.keyH.upPressed = false;
                    optionSelectY = gp.tileSize * 2 - 10;
                }
                if (gp.keyH.enterPressed) {
                    displayVolumeScreen = true;
                    //TODO: drawVolumeScreen();
                    gp.keyH.enterPressed = false;
                }
            }
            case VIDEO -> {
                drawSelectTriangle(optionMenuSelectX, optionSelectY);
                if (gp.keyH.downPressed) {
                    optionMenuSelection = OptionMenuOptions.AUDIO;
                    gp.keyH.downPressed = false;
                    optionSelectY = gp.tileSize - 10;
                }
                if (gp.keyH.upPressed) {
                    optionMenuSelection = OptionMenuOptions.AUDIO;
                    gp.keyH.upPressed = false;
                    optionSelectY -= gp.tileSize;
                }
                if (gp.keyH.enterPressed) {
                    displayVolumeScreen = true;
                    //TODO: drawVolumeScreen();
                    gp.keyH.enterPressed = false;
                }
            }
        }

        //EXIT
        if(gp.keyH.escapePressed){
            gp.keyH.escapePressed = false;
            displayOptionMenu = false;
            menuSelection = PauseMenuOptions.OPTION;
        }
    }

    public boolean isDisplayPokedexScreen() {
        return displayPokedexScreen;
    }

    public void setDisplayPokedexScreen(boolean displayPokedexScreen) {
        this.displayPokedexScreen = displayPokedexScreen;
    }

    public boolean isDisplayPartyScreen() {
        return displayPartyScreen;
    }

    public void setDisplayPartyScreen(boolean displayPartyScreen) {
        this.displayPartyScreen = displayPartyScreen;
    }

    public boolean isDisplayBagScreen() {
        return displayBagScreen;
    }

    public void setDisplayBagScreen(boolean displayBagScreen) {
        this.displayBagScreen = displayBagScreen;
    }

    public boolean isDisplayPokegearScreen() {
        return displayPokegearScreen;
    }

    public void setDisplayPokegearScreen(boolean displayPokegearScreen) {
        this.displayPokegearScreen = displayPokegearScreen;
    }

    public boolean isDisplayCharacterStatScreen() {
        return displayCharacterStatScreen;
    }

    public void setDisplayCharacterStatScreen(boolean displayCharacterStatScreen) {
        this.displayCharacterStatScreen = displayCharacterStatScreen;
    }

    public boolean isDisplaySaveScreen() {
        return displaySaveScreen;
    }

    public void setDisplaySaveScreen(boolean displaySaveScreen) {
        this.displaySaveScreen = displaySaveScreen;
    }

    public boolean isDisplayOptionMenu() {
        return displayOptionMenu;
    }

    public void setDisplayOptionMenu(boolean displayOptionMenu) {
        this.displayOptionMenu = displayOptionMenu;
    }

    public boolean isDisplayPokemonStatScreen() {
        return displayPokemonStatScreen;
    }

    public void setDisplayPokemonStatScreen(boolean displayPokemonStatScreen) {
        this.displayPokemonStatScreen = displayPokemonStatScreen;
    }
}
