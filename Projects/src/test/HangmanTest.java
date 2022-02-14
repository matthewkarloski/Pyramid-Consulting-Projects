import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class HangmanTest {
    Hangman h;
    Scanner s;
    ArrayList<Integer> i;
    ArrayList<Character> c;
    ArrayList<Character> d;
    @BeforeEach
    void setUp() {
        s = new Scanner(System.in);
        h = new Hangman();
        i = new ArrayList<>(){{
            add(0);
            add(5);
        }}; //indexes
        c = new ArrayList<>(){{
            add('c');
            add('_');
            add('_');
            add('_');
            add('_');
            add('c');
            add('_');
            add('_');
            add('_');
        }}; //c's in character
        d = new ArrayList<>(){{
            add('_');
            add('_');
            add('_');
            add('_');
            add('_');
            add('_');
            add('_');
            add('_');
            add('_');
        }};//Just ______s
    }


    @Test
    void playAgain() {
        //input y when prompted
        assertTrue(h.playAgain(s), "play again not registering y as to play again");

        //input n when prompted
        assertFalse(h.playAgain(s), "play again not registering n");

    }

    @Test
    void checkletter() {
        assertEquals(i, h.checkletter('c', "character"), "checkLetter is not working" );
    }

    @Test
    void changeCorrectLetters() {
        assertEquals(c, h.changeCorrectLetters(d,i, 'c'), "change correct Letters is not working");
    }

    @AfterEach
    void tearDown() {
    }
}