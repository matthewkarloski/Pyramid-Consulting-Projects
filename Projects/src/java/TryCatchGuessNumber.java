import java.util.Random;
import java.util.Scanner;

public class TryCatchGuessNumber {
    /* Does the same as Guess Number, but adds try catch statements to the user inputs

    USE TCGuessNumber TO RUN THIS CODE:

    public static void main(String[] args) {
        //Start the welcome, get their name
        System.out.println("Hello! What is your name?");
        Scanner sc = new Scanner(System.in);
        try {
            String name = sc.nextLine();
            numberGuess(name);
        } catch (Exception e) {
            System.out.println("Invalid input, please restart the program and enter your name.");
        }
        sc.close();
        System.out.print("Game over");

    }

     */
    public String name(Scanner sc){
        System.out.println("Hello! What is your name?");
        String name = "";
        try {
            name = sc.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input, please restart the program and enter your name.");
        }
        return name;
    }

    public void numberGuess(String name, Scanner sc) {
        System.out.printf("Well %s, I am thinking of a number between 1 and 20%n", name);
        //get a random number between 1 and 20
        Random rand = new Random();
        int solution = rand.nextInt(20)+1;
        boolean usercorrect = false;
        int guess;

        //user gets 6 tries to guess the right number
        for (int i = 0; i<6; i++) {
            guess = userGuess(sc);
            usercorrect = check(guess, solution);

            if (usercorrect){
                //User guessed correctly
                //# of tries is i+1
                System.out.printf("Good job, %s! You guessed my number in %d%n", name, i+1);
                break;
            }
        }

        //check to see if user passed, if not, then tell them that they failed
        if (!usercorrect) System.out.printf("Sorry, you didn't guess the right number. The solution was %d%n", solution);


        //seeing if the user would like to start a new game
        Scanner scanner = new Scanner(System.in);
        String answer = "";

        do{
            try {
                System.out.println("Would you like to play again? (y or n)");
                answer = scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please input 'y' or 'n'"); //honestly don't know how you'd get this outide of control-something since everything else would be a string
            }
            if (!answer.equals("y") && !answer.equals("n")){ //ensures user enters y or n
                System.out.println("Please input 'y' or 'n'");
            }
        }while(!answer.equals("y") && !answer.equals("n")); //do it until a valid answer is given

        if (answer.equals("y")) numberGuess(name, sc);
        else System.out.println("Game over");
        scanner.close();
    }

    public int userGuess(Scanner sc2){
        int userguess = -1;

        //ensure the user gives a number between 1 and 20
        do {
            System.out.println("Take a guess between 1 and 20.");
            try {
                userguess = sc2.nextInt();
                if (userguess <1 || userguess >20){ // if dec is not 1 or 2, do this
                    System.out.println("Invalid input, please enter a number between 1 and 20");
                }
            }catch(Exception ex){  //if exception in the try
                sc2.next();
                System.out.println("Invalid input, please enter a number between 1 and 20");
            }
        } while (userguess <1 || userguess >20);

        return userguess;
    }

    public boolean check(int userguess, int solution){
        if (userguess == solution) {
            return true; //returns true if correct
        }
        //user was wrong, needs to check whether it's to high or low
        if(userguess > solution) System.out.println("Your guess is too high.");
        else System.out.println("Your guess is too low.");
        return false;
    }
}
