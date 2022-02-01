
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TryCatchDragonCaveTest {
    TryCatchDragonCave t;
    @BeforeEach
    void setUp() {
        t = new TryCatchDragonCave();
    }

    @Test
    void dragonCave() throws InterruptedException {
        assertTrue(t.dragonCave(), "I guess it didn't work");
    }

    @AfterEach
    void tearDown() {

    }
}