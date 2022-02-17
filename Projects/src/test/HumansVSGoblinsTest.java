import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HumansVSGoblinsTest {

    HumansVSGoblins t;
    String[][] board;

    @BeforeEach
    void setUp() {
        t = new HumansVSGoblins();
        t.h = new Human(10, 5);
        t.g = new Goblin(2, 1);
    }
    @Test
    void combat(){
        assertEquals("goblin has died", t.combat(true), "combat not working");
        t.g.setHealth(10);
        t.g.setStrength(5);
        t.h.setHealth(1);
        assertEquals("human has died", t.combat(false), "combat not working");
        t.g.setHealth(100);
        t.h.setHealth(100);
        assertEquals("", t.combat(true), "combat not working");
    }

    @AfterEach
    void tearDown() {
    }
}