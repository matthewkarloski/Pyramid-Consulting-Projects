package Week1;
import java.util.Random;
import java.util.Scanner;

/* Guess the number
 * By Matthew Karloski
 * 1/27/2022
 */

public class GuesstheNumber {
	/* The computer will pick a random number between 1 to 20 and
	 * ask the user to guess it. After each guess, the computer will
	 * tell the user whether the number is too high or too low. User
	 * wins if they can guess the number within 6 tries
	 */

	public static void main(String[] args) {
		//Start the welcome, get their name
		System.out.println("Hello! What is your name?");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		numberGuess(name);
		sc.close();
		System.out.print("Game over");
		
	}
	
	public static void numberGuess(String name) {
		System.out.println(String.format("Well %s, I am thinking of a number between 1 and 20", name));
		//get a random number between 1 and 20
        Random rand = new Random();
        int solution = rand.nextInt(20)+1;
		boolean usercorrect = false;
		Scanner sc2 = new Scanner(System.in);
		
		//user gets 6 tries to guess the right number
		for (int i = 0; i<6; i++) {
			int userguess;
			
			do {
				System.out.println("Take a guess between 1 and 20.");
				while (!sc2.hasNextInt()) {
					System.out.println("Invalid input, please enter a number between 1 and 20");
					sc2.next();
				}
				userguess = sc2.nextInt();
			} while (userguess <1 || userguess >20);
			
			
			if (userguess == solution) {
				//User guessed correctly
				//# of tries is i+1
				System.out.println(String.format("Good job, %s! You guessed my number in %d", name, i+1));
				usercorrect = true; //put usercorrect as true
				break; //get out of the for loop
			}else {
				//user was wrong, needs to check whether it's to high or low
				if(userguess > solution) System.out.println("Your guess is too high.");
				else System.out.println("Your guess is too low.");
			}
		}
		//check to see if user passed, if not, then tell them that they failed
		if (!usercorrect) System.out.println(String.format("Sorry, you didn't guess the right number. The solution was %d", solution));
		
		
		//seeing if the user would like to start a new game
		Scanner scanner = new Scanner(System.in);
		System.out.println("Would you like to play again? (y or n)");
		
		while (!scanner.hasNext("[yn]")) { //This line ensures that the user inputed y or n, if not, it'll continue to ask for the right input
			System.out.println("Not valid input, please input 'y' or 'n'");
			scanner.next();
		}
		
		String answer = scanner.next();
		if (answer.equals("y")) numberGuess(name);
		sc2.close();
		scanner.close();
	}
}
