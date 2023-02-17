package entity;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import org.json.simple.parser.*;
import org.json.simple.*;

public class PokeNPC extends Entity{
    public String name;
    private boolean hasEvolveRequirement = false;
    private boolean hasTwoTypes = false;
    private String evolutionRequirement;
    private String type1, type2, description;

    public PokeNPC(GamePanel gp, String pokeName, int worldX, int worldY) {
        super(gp, pokeName);

        this.worldX = worldX * gp.tileSize;
        this.worldY = worldY * gp.tileSize;
        this.direction = "down";
        this.speed = 1;
        this.name = pokeName;

        getImage(pokeName);
        getSprite(pokeName);

        statSetup(pokeName);

        solidArea = new Rectangle(8 ,16,32,32);

        setCurrentLevel(5);
        setTotalHP((int) Math.floor(0.01 * (2 * super.getHp() * getCurrentLevel()) + getCurrentLevel() + 10));
        setCurrentHP(getTotalHP());

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

    public void statSetup(String name){
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("src/main/resources/pokemon-base-stats/" + name + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject statObject = (JSONObject)jsonObject.get("stats");
            JSONObject evolutionObject = (JSONObject)jsonObject.get("evolution");

            setHp(Integer.parseInt((String) statObject.get("hp")));
            setAttack(Integer.parseInt((String) statObject.get("attack")));
            setDefense(Integer.parseInt((String) statObject.get("defense")));
            setSpecialAttack(Integer.parseInt((String) statObject.get("specialAttack")));
            setSpecialDefense(Integer.parseInt((String) statObject.get("specialDefense")));
            setSpeedStat(Integer.parseInt((String) statObject.get("speed")));
            setType1((String) statObject.get("type1"));
            setType2((String) statObject.get("type2"));

            setDescription((String) statObject.get("description"));

            if(type2 != null){
                this.hasTwoTypes = true;
            }

            setEvolutionLevel(Integer.parseInt((String) evolutionObject.get("level")));
            evolutionRequirement = (String) evolutionObject.get("requirements");

            if(!evolutionRequirement.equalsIgnoreCase("none")){
                hasEvolveRequirement = true;
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("IOException");
        } catch (ParseException e) {
            System.err.println("Parse Exception");
        }

        switch (type1){
            case "GRASS" -> setBackgroundStatColor(new Color(0, 204, 102));
            case "GHOST" -> setBackgroundStatColor(new Color(96, 70, 107));
            case "WATER" -> setBackgroundStatColor(new Color(119, 156, 255));
            case "ELECTRIC" -> setBackgroundStatColor(new Color(255, 255, 51));
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

    //Getters and Setters
    public String getName() {
        return name;
    }
    public boolean doesHasEvolveRequirement() {
        return hasEvolveRequirement;
    }
    public void setHasEvolveRequirement(boolean hasEvolveRequirement) {
        this.hasEvolveRequirement = hasEvolveRequirement;
    }
    public String getEvolutionRequirement() {
        return evolutionRequirement;
    }
    public void setEvolutionRequirement(String evolutionRequirement) {
        this.evolutionRequirement = evolutionRequirement;
    }

    public boolean isHasTwoTypes() {
        return hasTwoTypes;
    }

    @Override
    public void setHasTwoTypes(boolean hasTwoTypes) {
        this.hasTwoTypes = hasTwoTypes;
    }

    @Override
    public String getType1() {
        return type1;
    }

    @Override
    public void setType1(String type1) {
        this.type1 = type1;
    }

    @Override
    public String getType2() {
        return type2;
    }

    @Override
    public void setType2(String type2) {
        this.type2 = type2;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
