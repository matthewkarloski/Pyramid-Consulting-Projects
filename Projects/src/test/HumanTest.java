import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class HumanTest {
    Human h1;
    Human h2;
    Item i1;
    Item i2;
    HumansVSGoblins hg;
    String[][] board;
    Scanner s;
    ArrayList<Item> items;
    @BeforeEach
    void setUp() {
        h1 = new Human(60, 5);
        h2 = new Human(22, 7);
        i1 = new Item("Weapon", "Bane of Java", 25);
        i2 = new Item("Potion", "Health Potion", 20);
        hg = new HumansVSGoblins();
        board= hg.board();
        s = new Scanner(System.in);
        items = new ArrayList<>();
    }
    @Test
    void getRow(){
        assertEquals(4, h1.getRow(), "getRow not working");
        assertEquals(4, h2.getRow(), "getRow not working");
    }

    @Test
    void getCol(){
        assertEquals(4, h1.getCol(), "getCol not working");
        assertEquals(4, h2.getCol(), "getCol not working");
    }
    @Test
    void getHealth(){
        assertEquals(60, h1.getHealth(), "getHealth not working");
        assertEquals(22, h2.getHealth(), "getHealth not working");
    }

    @Test
    void getStrength(){
        assertEquals(5, h1.getStrength(), "getStrength not working");
        assertEquals(7, h2.getStrength(), "getStrength not working");
    }
    @Test
    void getDefense(){
        assertEquals(0, h1.getDefense(), "getDefense not working");
        assertEquals(0, h2.getDefense(), "getDefense not working");
    }

    @Test
    void equip(){
        h1.addItemToInventory(i1); //both should be added to first slot in backpack (5)
        h2.addItemToInventory(i2);
        assertTrue(h1.equip(5), "equip not working");
        assertFalse(h2.equip(5), "equip not working");
    }
    @Test
    void unequip(){
        h1.addItemToInventory(i1); //both should be added to first slot in backpack (5)
        h2.addItemToInventory(i1);
        h1.equip(5);
        h2.equip(5);
        assertTrue(h1.unequip(0), "unequip not working");
        assertTrue(h2.unequip(0), "unequip not working");
    }

    @Test
    void addItemToInventory(){
        assertTrue(h1.addItemToInventory(i1), "addItemToInventory not working");
        assertTrue(h1.addItemToInventory(i2), "addItemToInventory not working");
    }

    @Test
    void notOutOfBounds(){
        h1.setRow(1);
        h1.setCol(1);
        String s1 = "s";
        String s2 = "a";
        assertTrue(h1.notOutOfBounds(s1, board), "notOutOfBounds not working");
        assertFalse(h1.notOutOfBounds(s2, board), "notOutOfBounds not working");
    }


    @Test
    void loot(){
        items.add(i1);
        items.add(i2);
        //Enter y here when prompted
        assertTrue(h1.loot(i1, s, items), "loot not working");

        //enter n here when prompted
        assertFalse(h1.loot(i2, s, items), "loot not working");
    }


    @Test
    void usePotion(){
        h1.addItemToInventory(i1);
        h1.addItemToInventory(i2);
        h2.addItemToInventory(i1);

        assertFalse(h2.usePotion(), "usePotion not working");
        assertTrue(h1.usePotion(), "usePotion not working");
    }


    @AfterEach
    void tearDown() {
    }
}
