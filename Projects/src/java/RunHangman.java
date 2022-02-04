import java.util.Scanner;

public class RunHangman {
    public static void main(String[] args){
        Hangman hang = new Hangman();
        Scanner scanner = new Scanner(System.in);

        hang.hangman(scanner);
        scanner.close();



    }
}
