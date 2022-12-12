package tile;

import main.GamePanel;
import main.UtilTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TileManager {

    GamePanel gp;
    public ArrayList<Tile> tile = new ArrayList<>();
    public int[][] mapTileNum;

    public TileManager(GamePanel gp){
        this.gp = gp;

        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/Map Files/map03.txt");
    }

    public void getTileImage(){

/* 0 */     setup(0,"Grass", "grass", false);
/* 1 */     setup(1,"Water", "water", true);
/* 2 */     setup(2, "Fence", "fence", true);
/* 3 */     setup(3,"Sand", "sand", false);
/* 4 */     setup(4,"Gravel", "gravel", false);
/* 5 */     setup(5,"Tree", "tree", true);
/* 6 */     setup(6,"Bottom Left Water", "water_bottom_left", true);
/* 7 */     setup(7,"Left Water", "water_left", true);
/* 8 */     setup(8,"Top Left Water", "water_top_left", true);
/* 9 */     setup(9, "Top Water Edge", "water_top", true);
/* 10 */    setup(10, "Top Right Water Edge", "water_top_right", true);
/* 11 */    setup(11,"Right Water Edge", "water_right", true);
/* 12 */    setup(12,"Bottom Right Water Edge", "water_bottom_right", true);
/* 13 */    setup(13,"Bottom Water Edge", "water_bottom_edge", true);
/* 14 */    setup(14,"Bottom Left Water Inside Edge", "water_inside_bottom_left", true);
/* 15 */    setup(15,"Left Water Inside Edge", "water_inside_left", true);
/* 16 */    setup(16,"Top Left Water Inside Edge", "water_inside_top_left", true);
/* 17 */    setup(17,"Top Water Inside Edge", "water_inside_top", true);
/* 18 */    setup(18,"Top Right Water Inside Edge", "water_inside_top_right", true);
/* 19 */    setup(19,"Right Water Inside Edge", "water_inside_right", true);
/* 20 */    setup(20,"Bottom Right Water Inside Edge", "water_inside_bottom_right", true);
/* 21 */    setup(21,"Bottom Water Inside Edge", "water_inside_bottom", true);
/* 22 */    setup(22, "Bottom Left Fence", "fence_bottom_left", true);
/* 23 */    setup(23, "Left Fence", "fence_left", true);
/* 24 */    setup(24, "Top Left Fence", "fence_top_left", true);
/* 25 */    setup(25, "Top Fence", "fence_top", true);
/* 26 */    setup(26, "Top Right Fence", "fence_top_right", true);
/* 27 */    setup(27, "Right Fence", "fence_right", true);
/* 28 */    setup(28, "Bottom Right Fence", "fence_bottom_right", true);
/* 29 */    setup(29, "Flowers", "flowers", false);
/* 30 */    setup(30, "Left Sand Path", "30_sand_path_left", false);
/* 31 */    setup(31, "Bottom Left Sand Path", "31_sand_path_bottom_left", false);
/* 32 */    setup(32, "Bottom Right Sand Path", "32_sand_path_bottom_right", false);
/* 33 */    setup(33, "Right Sand Path", "33_sand_path_right", false);
/* 34 */    setup(34, "Top Right Sand Path", "34_sand_path_top_right", false);
/* 35 */    setup(35, "Top Sand Path", "35_sand_path_top", false);
/* 36 */    setup(36, "Top Left Sand Path", "36_sand_path_top_left", false);
/* 37 */    setup(37, "Bottom Sand Path", "37_sand_path_bottom", false);
/* 38 */    setup(38, "Inside Top Left Sand Path", "38_sand_path_inside_top_left", false);
/* 39 */    setup(39, "Inside Top Right Sand Path", "39_sand_path_inside_top_right", false);
/* 40 */    setup(40, "Inside Bottom Left Sand Path", "40_sand_path_inside_bottom_left", false);
/* 41 */    setup(41, "Inside Bottom Right Sand Path", "41_sand_path_inside_bottom_right", false);
/* 42 */    setup(42, "Dirt", "42_dirt", false);
/* 43 */    setup(43, "Left Dirt Path", "43_dirt_path_left", false);
/* 44 */    setup(44, "Bottom Left Dirt Path", "44_dirt_path_bottom_left", false);
/* 45 */    setup(45, "Bottom Dirt Path", "45_dirt_path_bottom", false);
/* 46 */    setup(46, "Bottom Right Dirt Path", "46_dirt_path_bottom_right", false);
/* 47 */    setup(47, "Right Dirt Path", "47_dirt_path_right", false);
/* 48 */    setup(48, "Top Right Dirt Path", "48_dirt_path_top_right", false);
/* 49 */    setup(49, "Top Dirt Path", "49_dirt_path_top", false);
/* 50 */    setup(50, "Top Left Dirt Path", "50_dirt_path_top_left", false);
/* 51 */    setup(51, "Inside Top Left Dirt Path", "51_dirt_path_inside_top_left", false);
/* 52 */    setup(52, "Inside Top Right Dirt Path", "52_dirt_path_inside_top_right", false);
/* 53 */    setup(53, "Inside Bottom Right Dirt Path", "53_dirt_path_inside_bottom_right", false);
/* 54 */    setup(54, "Inside Bottom Left Dirt Path", "54_dirt_path_inside_bottom_left", false);
/* 55 */    setup(55, "Boulder", "55_boulder", true);
/* 56 */    setup(56, "Left Beach", "56_left_beach", false);
/* 57 */    setup(57, "Bottom left Beach", "57_bottom_left_beach", false);
/* 58 */    setup(58, "Bottom Beach", "58_bottom_beach", false);
/* 59 */    setup(59, "Bottom Right Beach", "59_bottom_right_beach", false);
/* 60 */    setup(60, "Right Beach", "60_right_beach", false);
/* 61 */    setup(61, "Top Right Beach", "61_top_right_beach", false);
/* 62 */    setup(62, "Top Beach", "62_top_beach", false);
/* 63 */    setup(63, "Top Left Beach", "63_top_left_beach", false);
/* 64 */    setup(64, "Inside Top Left Beach", "64_inside_top_left_beach", false);
/* 65 */    setup(65, "Inside Bottom Left Beach", "65_inside_bottom_left_beach", false);
/* 66 */    setup(66, "Inside Bottom Right Beach", "66_inside_bottom_right_beach", false);
/* 67 */    setup(67, "Inside Top Right Beach", "67_inside_top_right_beach", false);

    }

    public void setup(int index, String tileName, String fileName, boolean collision){
        UtilTool uTool = new UtilTool();

        try {
            tile.add(new Tile(tileName, ImageIO.read(getClass().getResourceAsStream("/tiles/" + fileName + ".png")), collision));
            tile.get(index).image = uTool.scaleImage(tile.get(index).image, gp.tileSize, gp.tileSize);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void loadMap(String filePath){

        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){

                String line = br.readLine();

                while(col < gp.maxWorldCol){
                    String[] numbers = line.split(",");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch(Exception e){

        }
    }

    public void draw(Graphics2D g2){

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + (gp.tileSize*2) > gp.player.worldX - gp.player.screenX &&
                worldX - (gp.tileSize*2) < gp.player.worldX + gp.player.screenX &&
                worldY + (gp.tileSize*2) > gp.player.worldY - gp.player.screenY &&
                worldY - (gp.tileSize*2) < gp.player.worldY + gp.player.screenY){
                g2.drawImage(tile.get(tileNum).image, screenX, screenY, null);

            }
            worldCol++;
            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }

    }

}
