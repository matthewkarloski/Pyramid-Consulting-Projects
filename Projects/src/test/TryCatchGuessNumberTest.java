import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TryCatchGuessNumberTest {
    TryCatchGuessNumber t;
    Scanner s;
    @BeforeEach
    void setUp() {
        s = new Scanner(System.in);
        t = new TryCatchGuessNumber();
    }

    @Test
    void name() {
        //Type "Matt" when prompted
        assertEquals("Matt", t.name(s), "name not working");
    }

    @Test
    void userGuess() {
        //Type "1" when prompted
        assertEquals(1, t.userGuess(s), "UserGuess not working");
    }

    @Test
    void check() {
        assertTrue(t.check(1, 1), "check1 not working");
        assertFalse(t.check(1, 20), "check2 not working");
    }

    @AfterEach
    void tearDown() {
    }


}