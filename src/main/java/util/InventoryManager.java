package util;

import entity.Entity;
import item.Item;
import item.PokeballItem;
import item.PokeballType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManager {
    private Entity player;
    private Map<PokeballItem, Integer> pokeballs;


    public InventoryManager(Entity player){
        this.player = player;
        pokeballs = new HashMap<>();
        pokeballs.put(new PokeballItem(PokeballType.STANDARD), 5);
        pokeballs.put(new PokeballItem(PokeballType.GREAT_BALL), 5);
        pokeballs.put(new PokeballItem(PokeballType.ULTRA_BALL), 5);
        pokeballs.put(new PokeballItem(PokeballType.MASTER_BALL), 1);
    }

    public void pickupPokeballs(PokeballType type, int increase){
        boolean added = false;

        for(Map.Entry<PokeballItem, Integer> entry: pokeballs.entrySet()) {
            if(type.equals(entry.getKey().getType())) {
                pokeballs.put(entry.getKey(), entry.getValue() + increase);
                added = true;
            }
        }
        if(!added){
            pokeballs.put(new PokeballItem(type), increase);
        }
    }
    public void throwPokeball(PokeballType type) {
        for (Map.Entry<PokeballItem, Integer> entry : pokeballs.entrySet()) {
            if (type.equals(entry.getKey().getType())) {
                pokeballs.put(entry.getKey(), entry.getValue() - 1);
            }
        }
    }
    public boolean pokeballsNotEmpty(){
        return pokeballs.size() > 0;
    }



    //GETTERS AND SETTERS
    public int getPokeball(PokeballType type) {
        for (Map.Entry<PokeballItem, Integer> entry : pokeballs.entrySet()) {
            if (type.equals(entry.getKey().getType())) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public Map<PokeballItem, Integer> getPokeballs() {
        return pokeballs;
    }

    public void setPokeballs(Map<PokeballItem, Integer> pokeballs) {
        this.pokeballs = pokeballs;
    }
}
