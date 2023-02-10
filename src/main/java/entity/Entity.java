package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Entity {

    GamePanel gp;
    public BufferedImage image;
    public BufferedImage sprite;
    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public String direction;
    public int spriteCounter = 0;
    public int battleSpriteCounter = 0;
    public int battleSpriteNum = 1;
    public int spriteNum = 1;
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean isSolid = false;
    public int actionLockCounter = 0;
    public String name;
    private boolean hasTwoTypes = false;
    private String type1, type2, description;
    private int hp, attack, defense, specialAttack, specialDefense, speedStat, evolutionLevel, currentLevel, currentHP, totalHP;
    private Color backgroundStatColor;

    public Entity(GamePanel gp, String name) {
        this.gp = gp;
        this.name = name;
    }
    public Entity(GamePanel gp){
        this.gp = gp;
    }


    public void setAction (){}

    public void update(){}

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + (gp.tileSize * 2) > gp.player.worldX - gp.player.screenX &&
                worldX - (gp.tileSize * 2) < gp.player.worldX + gp.player.screenX &&
                worldY + (gp.tileSize * 2) > gp.player.worldY - gp.player.screenY &&
                worldY - (gp.tileSize * 2) < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public void setDefaultValue(){}


    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(int specialDefense) {
        this.specialDefense = specialDefense;
    }

    public int getSpeedStat() {
        return speedStat;
    }

    public void setSpeedStat(int speedStat) {
        this.speedStat = speedStat;
    }

    public int getEvolutionLevel() {
        return evolutionLevel;
    }

    public void setEvolutionLevel(int evolutionLevel) {
        this.evolutionLevel = evolutionLevel;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getTotalHP() {
        return totalHP;
    }

    public void setTotalHP(int totalHP) {
        this.totalHP = totalHP;
    }

    public boolean doesHasTwoTypes() {
        return hasTwoTypes;
    }

    public void setHasTwoTypes(boolean hasTwoTypes) {
        this.hasTwoTypes = hasTwoTypes;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getBackgroundStatColor() {
        return backgroundStatColor;
    }

    public void setBackgroundStatColor(Color backgroundStatColor) {
        this.backgroundStatColor = backgroundStatColor;
    }
}
