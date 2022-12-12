package main;

import entity.PokeNPC;
import object.KeyObject;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void placeObject(){

        gp.obj.add(new KeyObject(65 * gp.tileSize, 74 * gp.tileSize));
        gp.obj.add(new KeyObject(24 * gp.tileSize, 72 * gp.tileSize));
        gp.obj.add(new KeyObject(14 * gp.tileSize, 37 * gp.tileSize));
        gp.obj.add(new KeyObject(12 * gp.tileSize, 12 * gp.tileSize));
        gp.obj.add(new KeyObject(75 * gp.tileSize, 15 * gp.tileSize));
        gp.obj.add(new KeyObject(77 * gp.tileSize, 32 * gp.tileSize));

    }

    public void setNPC(){

        gp.pokemon.add(new PokeNPC(gp, "gastly", 63,54));
        gp.pokemon.add(new PokeNPC(gp, "pikachu", 17, 17));
        gp.pokemon.add(new PokeNPC(gp, "treecko", 45,61));
        gp.pokemon.add(new PokeNPC(gp, "voltorb", 63,38));
        gp.pokemon.add(new PokeNPC(gp, "mudkip", 57, 16));
        gp.pokemon.add(new PokeNPC(gp, "bulbasaur", 25, 37));
    }
}
