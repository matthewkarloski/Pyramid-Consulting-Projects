import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HangmanFunctional {
    /*
    Todo:
        -Add JUnit testing
     */


    //Prints out the hangman picture. misses are how many times it has missed so far
    public void hangmanPic(int misses) throws IOException {
        String file;
        if (misses == 0)
            file = "C:\\Users\\matthew\\Documents\\GenSpark\\Pyramid-Consulting-Projects\\Projects\\HangmanFiles\\0misses.txt";
        else if (misses == 1)
            file = "C:\\Users\\matthew\\Documents\\GenSpark\\Pyramid-Consulting-Projects\\Projects\\HangmanFiles\\1miss.txt";
        else if (misses == 2)
            file = "C:\\Users\\matthew\\Documents\\GenSpark\\Pyramid-Consulting-Projects\\Projects\\HangmanFiles\\2misses.txt";
        else if (misses == 3)
            file = "C:\\Users\\matthew\\Documents\\GenSpark\\Pyramid-Consulting-Projects\\Projects\\HangmanFiles\\3misses.txt";
        else if (misses == 4)
            file = "C:\\Users\\matthew\\Documents\\GenSpark\\Pyramid-Consulting-Projects\\Projects\\HangmanFiles\\4misses.txt";
        else if (misses == 5)
            file = "C:\\Users\\matthew\\Documents\\GenSpark\\Pyramid-Consulting-Projects\\Projects\\HangmanFiles\\5misses.txt";
        else if (misses == 6)
            file = "C:\\Users\\matthew\\Documents\\GenSpark\\Pyramid-Consulting-Projects\\Projects\\HangmanFiles\\6misses.txt";
        else
            file = "C:\\Users\\matthew\\Documents\\GenSpark\\Pyramid-Consulting-Projects\\Projects\\HangmanFiles\\7misses.txt";

        Files.lines(Paths.get(file))
                .forEach(System.out :: println);
    }

    //Runs the game
    public void hangman(Scanner scanner) throws IOException {
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
        ArrayList<Character> usersees = new ArrayList<>();
        ArrayList<Character> finalUsersees = usersees;
        Stream.of(solution.split(""))
                .forEach(x -> finalUsersees.add('_'));
        usersees = finalUsersees;

        String filename = "C:\\Users\\matthew\\Documents\\GenSpark\\Pyramid-Consulting-Projects\\Projects\\HangmanFiles\\HighScores.txt";
        int misses = 0;
        boolean usercompleted; //While not hung
        ArrayList<Character> pastguesses = new ArrayList<>(); // creates list of all past letters user gave
        ArrayList<Character> missedletters = new ArrayList<>();//creates list of all past missed letters user gave

        //printout the pic to start
        hangmanPic(misses);
        System.out.print("Missed Letters: ");
        System.out.println();

        usersees
                .forEach(System.out :: print);
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
            missedletters.forEach(x -> System.out.print(x + " "));
            System.out.println();
            usersees
                    .forEach(System.out :: print);
            System.out.println();
            System.out.println();

            //checks if user correctly guessed the word
            usercompleted = true;
            usercompleted = usersees.stream().noneMatch(x -> x.equals('_'));
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

        //Get name and say whether they got a high score
        String name = "";
        System.out.println("What is your name?");
        Scanner s = new Scanner(System.in);
        name = s.nextLine();

        if (addCheckScores(filename, name, misses))
            System.out.println("You have a high score!");
        //figure out if they want to play again
        Scanner sc = new Scanner(System.in);
        if(playAgain(sc)){
            hangman(scanner);
        }else{
            System.out.println("Game Over");
        }
        sc.close();
        s.close();
    }

    public boolean addCheckScores(String filepath, String name, int score){
        String tempfile = "temp.txt";
        File oldFile = new File(filepath);
        File newFile = new File(tempfile);
        boolean highscore = false;
        boolean nameinfile = false;

        try{
            FileWriter fw = new FileWriter(tempfile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner x = new Scanner(new File(filepath));
            x.useDelimiter("[,\n]");
            while(x.hasNext()){
                String filename = x.next();
                String filescore = x.next();
                if (filename.equals(name)) {
                    //if had less misses than previous times. If highscore, should always return true
                    nameinfile = true;
                    highscore = Integer.parseInt(filescore) > score;
                }
                pw.print(filename +"," + filescore + "\n");
            }
            if (!nameinfile) highscore = true; //if new name, it's a high score
            pw.print(name + "," + score + "\n");
            x.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(filepath);
            newFile.renameTo(dump);
        }catch (Exception e){
            System.out.println("Something went wrong when editing file");
            highscore = false;
        }
        return highscore;
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
        IntStream.range(0, solution.length())
                .filter(index -> solution.charAt(index) == c)
                .forEach(indexesfound::add);

        return indexesfound;
    }

    //Changes to correct word
    public ArrayList<Character> changeCorrectLetters(ArrayList<Character> solution, ArrayList<Integer> indexes, char c) {
        indexes.forEach(x-> solution.set(x, c));

        return solution;
    }
}
