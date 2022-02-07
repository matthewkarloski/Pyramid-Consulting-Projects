//By Matthew Karloski

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    /*
    Make a "Hangman picture" class for the visual
    make a list of random words. Program will choose one from there
    while hangman hasn't hanged, get user input, if right, show the right letter,
    else add a piece to hangman

     */

    //Prints out the hangman picture. misses are how many times it has missed so far
    public void hangmanPic(int misses){
        System.out.println(" +--------+");
        if (misses > 0)
        System.out.println(" O        |");

        //Second level
        if (misses == 2)
        System.out.println(" |        |");
        if (misses == 3)
        System.out.println("/|        |");
        if (misses >3)
        System.out.println("/|\\       |");
        //third level
        if (misses>4)
        System.out.println(" |        |");
        //fourth
        if (misses == 6)
        System.out.println("/         |");
        if (misses >6)
        System.out.println("/ \\       |");


        //printing out the empty levels
        if (misses == 0){ //all empty
            for (int i = 0; i<4; i++)
                System.out.println("          |");
        }else if(misses == 1){ //head there
            for (int i = 0; i<3; i++)
                System.out.println("          |");
        }else if(misses == 2 || misses == 3 || misses ==4) { //If arms are there
            for (int i = 0; i < 2; i++)
                System.out.println("          |");
        }else if (misses ==5) //if it reaches the torso
            System.out.println("          |");

        //fifth
        System.out.println("        =====");

    }

    //Runs the game
    public void hangman(Scanner scanner){
        System.out.println("H A N G M A N");
        //Create "dictionary"
        ArrayList<String> words = new ArrayList<String>() {
            {   add("dogs");
                add("cats");
                add("haphazard");
                add("beekeeper");
                add("abruptly");
                add("stronghold");
                add("nightclub");
                add("oxygen");
                add("wristwatch");
                add("microwave");
                add("jukebox");
            }};


        //get a random word
        Random rand = new Random();
        String solution = words.get(rand.nextInt(words.size()-1)+1);
        ArrayList<Character> usersees = new ArrayList<Character>();
        for (int i = 0; i <solution.length(); i++){
            usersees.add('_');
        }

        int misses = 0;
        boolean usercompleted; //While not hung
        ArrayList<Character> pastguesses = new ArrayList<>(); // creates list of all past letters user gave
        ArrayList<Character> missedletters = new ArrayList<>();//creates list of all past missed letters user gave

        //printout the pic to start
        hangmanPic(misses);
        System.out.print("Missed Letters: ");
        System.out.println();

        for (Character i : usersees) {
            System.out.print(i);
        }
        System.out.println();

        //Now start the loop
        do { //need to get out when they are right
            //ask for input (make it lowercase)
            char userguess = '-';
            String struserguess = "";
            do {
                try {
                    System.out.println("Guess a letter.");
                    struserguess = scanner.nextLine();
                    userguess = struserguess.charAt(0);
                } catch (Exception e) {
                    System.out.println("Invalid input");
                }
                //lowercase it
                userguess = Character.toLowerCase(userguess);
                //checks if already guessed that letter and that it is only 1 valid letter
                if (!struserguess.matches("[A-Za-z]{1}")){
                    System.out.println("Invalid input, please guess a single letter");
                }else if (pastguesses.contains(userguess)) {
                    System.out.println("You've already guessed that letter. Choose again.");
                }
            }while(pastguesses.contains(userguess) || !struserguess.matches("[A-Za-z]{1}"));
            pastguesses.add(userguess);


            //check to see if input can be added in solution
            ArrayList<Integer> indexes = new ArrayList<>();
            indexes = checkletter(userguess, solution);
            if (!indexes.isEmpty()){
                //if can, show it
                usersees = changeCorrectLetters(usersees, indexes, userguess);
            }else {
                //if can't add 1 to misses
                missedletters.add(userguess);
                misses++;
            }

            //print out the pic to show result
            hangmanPic(misses);
            System.out.print("Missed Letters: ");
            for (char i : missedletters){
                System.out.print(i + " ");
            }
            System.out.println();
            for (Character i : usersees) {
                System.out.print(i);
            }
            System.out.println();
            System.out.println();

            //checks if user correctly guessed the word
            usercompleted = true;
            for (char i : usersees){
                if (i == '_') {
                    usercompleted = false;
                    break;
                }
            }
        }while (misses != 7 && !usercompleted);


        //if the misses reached 7, they failed. If not, they passed
        if (misses<7){
            //they passed
            System.out.printf("Yes! The secret word is \"%s\"! You have won!", solution);
            System.out.println();
        }else{
            System.out.printf("Sorry, you lost! The secret word was \"%s\"!", solution);
            System.out.println();
        }

        //figure out if they want to play again
        Scanner sc = new Scanner(System.in);
        if(playAgain(sc)){
            hangman(scanner);
        }else{
            System.out.println("Game Over");
        }
        sc.close();
    }

    //Finds out whether the user wants to play again or not
    public boolean playAgain(Scanner scanner){
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

        return answer.equals("y");//If yes, return true
    }


    //will take a letter, and check and see if it is in the solution and returns all indexes where it was located
    public ArrayList<Integer> checkletter(char c, String solution){
        ArrayList<Integer> indexesfound = new ArrayList<>();
        for (int i = 0; i<solution.length(); i++){
            if (solution.charAt(i) == c)
                indexesfound.add(i);

        }
        return indexesfound;
    }

    //Changes to correct word
    public ArrayList<Character> changeCorrectLetters(ArrayList<Character> solution, ArrayList<Integer> indexes, char c){
        for (int i : indexes)
            solution.set(i, c);

        return solution;
    }
}