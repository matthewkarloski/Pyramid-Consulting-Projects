import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoblinTest {
    Goblin g1;
    Goblin g2;
    @BeforeEach
    void setUp() {
        g1 = new Goblin(40, 1);
        g2 = new Goblin(55, 4);
    }
    @Test
    void getRow(){
        assertEquals(2, g1.getRow(), "getRow not working");
        assertEquals(2, g2.getRow(), "getRow not working");
    }

    @Test
    void getCol(){
        assertEquals(2, g1.getCol(), "getCol not working");
        assertEquals(2, g2.getCol(), "getCol not working");
    }
    @Test
    void getHealth(){
        assertEquals(40, g1.getHealth(), "getHealth not working");
        assertEquals(55, g2.getHealth(), "getHealth not working");
    }

    @Test
    void getStrength(){
        assertEquals(1, g1.getStrength(), "getStrength not working");
        assertEquals(4, g2.getStrength(), "getStrength not working");
    }


    @AfterEach
    void tearDown() {
    }
}
