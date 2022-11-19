package main;

import entity.Entity;
import entity.PokeNPC;
import object.SuperObject;

import java.util.ArrayList;

public class CollisionDetector {

    GamePanel gp;

    public CollisionDetector(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity){

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entityLeftWorldX + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entityTopWorldY + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile.get(tileNum1).collision || gp.tileM.tile.get(tileNum2).collision) {
                    entity.isSolid = true;
                }
            }
            case "down" -> {
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile.get(tileNum1).collision || gp.tileM.tile.get(tileNum2).collision) {
                    entity.isSolid = true;
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile.get(tileNum1).collision || gp.tileM.tile.get(tileNum2).collision) {
                    entity.isSolid = true;
                }
            }
            case "right" -> {
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile.get(tileNum1).collision || gp.tileM.tile.get(tileNum2).collision) {
                    entity.isSolid = true;
                }
            }
        }
    }
    public int checkObject(Entity entity, boolean player){
        int index = 999;

        for(SuperObject obj : gp.obj){
            if(obj != null){
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                obj.solidArea.x = obj.worldX + obj.solidArea.x;
                obj.solidArea.y = obj.worldY + obj. solidArea.y;

                switch (entity.direction) {
                    case "up" -> {
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(obj.solidArea)) {
                            if (obj.collision) {
                                entity.isSolid = true;
                            }
                            if (player) {
                                index = gp.obj.indexOf(obj);
                            }
                        }
                    }
                    case "down" -> {
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(obj.solidArea)) {
                            if (obj.collision) {
                                entity.isSolid = true;
                            }
                            if (player) {
                                index = gp.obj.indexOf(obj);
                            }
                        }
                    }
                    case "left" -> {
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(obj.solidArea)) {
                            if (obj.collision) {
                                entity.isSolid = true;
                            }
                            if (player) {
                                index = gp.obj.indexOf(obj);
                            }
                        }
                    }
                    case "right" -> {
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(obj.solidArea)) {
                            if (obj.collision) {
                                entity.isSolid = true;
                            }
                            if (player) {
                                index = gp.obj.indexOf(obj);
                            }
                        }
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                obj.solidArea.x = obj.solidAreaDefaultX;
                obj.solidArea.y = obj.solidAreaDefaultY;
            }
        }

        return index;
    }

    // PLAYER TO NPC Collision
    public int checkEntity(Entity entity, ArrayList<Entity> target){
        int index = 999;

        for(Entity npc : target){
            if(npc != null){
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                npc.solidArea.x = npc.worldX + npc.solidArea.x;
                npc.solidArea.y = npc.worldY + npc. solidArea.y;

                switch (entity.direction) {
                    case "up" -> {
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(npc.solidArea)) {
                                entity.isSolid = true;
                                index = gp.pokemon.indexOf(npc);
                        }
                    }
                    case "down" -> {
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(npc.solidArea)) {
                                entity.isSolid = true;
                                index = gp.pokemon.indexOf(npc);

                        }
                    }
                    case "left" -> {
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(npc.solidArea)) {
                                entity.isSolid = true;
                                index = gp.pokemon.indexOf(npc);

                        }
                    }
                    case "right" -> {
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(npc.solidArea)) {
                                entity.isSolid = true;
                                index = gp.pokemon.indexOf(npc);

                        }
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                npc.solidArea.x = npc.solidAreaDefaultX;
                npc.solidArea.y = npc.solidAreaDefaultY;
            }
        }

        return index;
    }

    public void checkPlayer(Entity entity){

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player. solidArea.y;

        switch (entity.direction) {
            case "up" -> {
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.isSolid = true;
                }
            }
            case "down" -> {
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.isSolid = true;

                }
            }
            case "left" -> {
                entity.solidArea.x -= entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.isSolid = true;

                }
            }
            case "right" -> {
                entity.solidArea.x += entity.speed;
                if (entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.isSolid = true;
                }
            }
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }
}
