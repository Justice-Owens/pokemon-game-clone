package entity;

import junit.framework.Assert;
import junit.framework.TestCase;
import main.GamePanel;

public class PokeNPCTest extends TestCase {
    GamePanel gpTest = new GamePanel();
    PokeNPC test = new PokeNPC(gpTest, "bulbasaur", 0,0);

    public void testStatSetup() {
        test.statSetup(test.getName());

        Assert.assertEquals("Bulbasaur stat test HP = 45", 45, test.getHp());
        Assert.assertEquals("Bulbasaur stat test attack = 49", 49, test.getAttack());
        Assert.assertEquals("Bulbasaur stat test defense = 49", 49, test.getDefense());
        Assert.assertEquals("Bulbasaur stat test special attack = 65", 65, test.getSpecialAttack());
        Assert.assertEquals("Bulbasaur stat test special defense = 65", 65, test.getSpecialDefense());
        Assert.assertEquals("Bulbasaur stat test speed stat = 45", 45, test.getSpeedStat());
        Assert.assertEquals("Bulbasaur has evolve requirement = false", false, test.isHasEvolveRequirement());
        Assert.assertEquals("Bulbasaur evolution requirement = none", "none", test.getEvolutionRequirement());
        Assert.assertEquals("Bulbasaur evolution level = 16", 16, test.getEvolutionLevel());
    }
}