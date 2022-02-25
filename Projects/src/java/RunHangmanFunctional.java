import java.io.IOException;
import java.util.Scanner;

public class RunHangmanFunctional {
    public static void main(String[] args) throws IOException {
        HangmanFunctional hang = new HangmanFunctional();
        Scanner scanner = new Scanner(System.in);

        hang.hangman(scanner);
        scanner.close();

    }

}
