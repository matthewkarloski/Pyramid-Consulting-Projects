//By Matthew Karloski

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    /*
    Make a "Hangman picture" class for the visual
    make a list of random words. Program will choose one from there
    while hangman hasn't hanged, get user input, if right, show the right letter,
    else add a piece to hangman

    To do:
    -asks if you want to play again at the end
    -ensures user doesn't input the same letter twice
    -ensures the user input is lowercase
    -ensures the user input is 1 character long and a letter
    -Make a JUnit test for this
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
        //While not hung
        boolean usercompleted;

        //printout thepic
        hangmanPic(misses);
        for (Character i : usersees) {
            System.out.print(i);
        }
        System.out.println();

        //Now start the loop
        do { //need to get out when they are right
            //ask for input (make it lowercase)
            char userguess = '-';
            try {
                System.out.println("Guess a letter.");
                userguess = scanner.next().charAt(0);
            }catch (Exception e){
                System.out.println("Invalid input");
            }
            //check to see if input can be added in solution
            ArrayList<Integer> indexes = new ArrayList<>();
            indexes = checkletter(userguess, solution);
            if (!indexes.isEmpty()){
                //if can, show it
                usersees = changeCorrectLetters(usersees, indexes, userguess);
            }else {
                //if can't add 1 to misses
                misses++;
            }

            //print out the pic
            hangmanPic(misses);
            for (Character i : usersees) {
                System.out.print(i);
            }
            System.out.println();
            System.out.println();
            //checks if user correctly guessed the word
            usercompleted = true;
            for (char i : usersees){
                if (i == '_'){
                    usercompleted = false;
                }
            }
        }while (misses != 7 && !usercompleted);
        //repeat
        //if the misses reached 7, they failed. If not, they passed
        if (misses<7){
            //they passed
            System.out.printf("Yes! The secret word is \"%s\"! You have won!", solution);
        }else{
            System.out.printf("Sorry, you lost! The secret word was \"%s\"!", solution);

        }

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