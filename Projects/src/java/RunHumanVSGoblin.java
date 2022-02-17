import java.util.Scanner;

public class RunHumanVSGoblin {
    public static void main(String[] args){
        HumansVSGoblins test = new HumansVSGoblins();
        Scanner s = new Scanner(System.in);
        test.playGame(s);
        s.close();
    }
}
