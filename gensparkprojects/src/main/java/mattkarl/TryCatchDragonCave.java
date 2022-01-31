package mattkarl;

import java.util.Random;
import java.util.Scanner;

//By Matthew Karloski
//1/26/2022
//Dragon Cave

public class TryCatchDragonCave {
    public static void main(String[] args) throws Exception {
        /*This is the same as Dragon cave, but try catch is used for user inputs
         */
        dragonCave();
    }

    public static void dragonCave() throws InterruptedException{
    	//We will give some story
        //next we'll need to get input from the user
        //take the input, and decide whether they get eaten or not
    	Scanner sc = new Scanner(System.in);

        System.out.println("You are in a land full of dragons. In front of you,");
        Thread.sleep(2000);
        System.out.println("you see two caves. In one cave, the dragon is friendly");
        Thread.sleep(2000);
        System.out.println("and will share his treasure with you. The other dragon");
        Thread.sleep(2000);
        System.out.println("is greedy and hungry and will eat you on sight");
        Thread.sleep(2000);
        
        //Get the input from the user, ensure that they are giving either a 1 or a 2, otherwise tell them to input again
        int dec = 0;


        /*
        do {
            System.out.println("Which cave will you go into? (1 or 2)");
			while (!sc.hasNextInt()) {
				System.out.println("Invalid input, please enter '1' or '2'.");
				sc.next();
			}
			dec = sc.nextInt();
		} while (dec == 1 && dec == 2);
        System.out.println();
        */

        //replacing above code with this to include the try catch statement
        do {
            System.out.println("Which cave will you go into? (1 or 2)");
            try {
                dec = sc.nextInt();
                if (dec != 1 && dec !=2){ // if dec is not 1 or 2, do this
                    System.out.println("Invalid input, please enter '1' or '2'.");
                }
            }catch(Exception ex){  //if exception in the try
                sc.next();
                System.out.println("Invalid input, please enter '1' or '2'.");
            }
        }while(dec != 1 && dec != 2); //do this until a valid answer is given
        
        
        //make the hungry dragon random number between 1 and 2, so the user can never guess the 
        //friendly dragon dragon even after multiple attempts
        Random rand = new Random();
        int hunDragon = rand.nextInt(2)+1;
        if (hunDragon == dec) {
        	//eats player
        	System.out.println("You approach the cave...");
        	Thread.sleep(2000);
        	System.out.println("It is dark and spooky...");
        	Thread.sleep(2000);
        	System.out.println("A large dragon jumps out in front of you! He opens his jaws and...");
        	Thread.sleep(2000);
        	System.out.println("Gobbles you down in one bite");
        }else {
        	//doesn't eat player
        	System.out.println("You approach the cave...");
        	Thread.sleep(2000);
        	System.out.println("It is dark and spooky...");
        	Thread.sleep(2000);
        	System.out.println("A large dragon jumps out in front of you! He opens his jaws and...");
        	Thread.sleep(2000);
        	System.out.println("says 'Hello there old friend!' and gives you a piece of gold from his treasure!");
        	}
        //ask if the user wishes to play again
        Scanner scanner = new Scanner(System.in);
        String answer = "";
		

        /*
		while (!scanner.hasNext("[yn]")) { //This line ensures that the user inputed y or n, if not, it'll continue to ask for the right input
			System.out.println("Not valid input, please input 'y' or 'n'");
			scanner.next();
		}
        String answer = scanner.next();
		*/

        //replacement for lines of code from above in Week 1, ensuring user enters y or n
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


		if (answer.equals("y")) dragonCave();
		else System.out.println("Game Over");
		sc.close();
		scanner.close();
    }
}
