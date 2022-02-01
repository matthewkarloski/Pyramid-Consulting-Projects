import java.util.Scanner;

/* Guess the number
 * By Matthew Karloski
 * 1/27/2022
 */
public class TCGuessNumber {
    static TryCatchGuessNumber t;
    public static void main(String[] args) {
        //Run this program for TryCatchGuessNumber if not doing the JUnit test
        Scanner s = new Scanner(System.in);
        t = new TryCatchGuessNumber();
        String tname = t.name(s);
        t.numberGuess(tname, s);
        s.close();

    }
}
