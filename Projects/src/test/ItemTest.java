import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest {
    Item i;
    Item s;
    @BeforeEach
    void setUp() {
        i = new Item();
        s = new Item("Weapon", "Sword", 10);

    }
    @Test
    void getRow(){
        assertEquals(0, i.getRow(), "getRow not working");
        assertEquals(0, s.getRow(), "getRow not working");
    }

    @Test
    void getCol(){
        assertEquals(0, i.getRow(), "getRow not working");
        assertEquals(0, s.getRow(), "getRow not working");
    }

    @Test
    void getName(){
        assertEquals("", i.getName(), "getName not working");
        assertEquals("Sword", s.getName(), "getName not working");
    }

    @Test
    void getType(){
        assertEquals("empty", i.getType(), "getType not working");
        assertEquals("Weapon", s.getType(), "getType not working");
    }
    @Test
    void getStat(){
        assertEquals(0, i.getStat(), "getStat not working");
        assertEquals(10, s.getStat(), "getStat not working");
    }


    @AfterEach
    void tearDown() {
    }
}
