// Jane Sobeck
// 2/6/2024
// Assignment 1: Word Search
// CS145 2958

//    The purpose of this java file is to allow the user to create a word search with the desired words
// and then the user can view the word search, view the solution, or print the word search
// to a file.

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class WordSearch {

    private static boolean WS_GENERATED = false;
    private static final int LENGTH = 15;
    private static final int HEIGHT = 15;

    // Main runs the intro funciton and then moves to the function manager
    public static void main(String[] args) throws FileNotFoundException {
        printIntro();
        methodSelector();
    }

    // Prints the introduction for the program to the console
    public static void printIntro() {
        System.out.println("Welcome to my word search generator!");
        System.out.println("This program will allow you to generate your own word search puzzle");
    }

    // Prints the program funcions to the console
    public static void printMenu() {
        System.out.println("Please select an option:");
        System.out.println("   Generate a new word search (g)");
        System.out.println("   Print out your word search (p)");
        System.out.println("   Show the solution to your word search (s)");
        System.out.println("   Prints generated word search to a file (f)");
        System.out.println("   Quit the program (q)");
    }

    /*
     * Prompts the user for amount of words, then tries to fit the words one by one
     * as the user
     * inputs the words. After each word is checked, it fits them in one by one on
     * the grid.
     * It would have been much easier to create a wordsearch object and manipulate
     * that, but
     * I realized that too late in development to easily transition it over.
     */
    public static char[][] generate(Scanner userInput) {
        char[][] answer = new char[HEIGHT][LENGTH];
        Random rand = new Random();
        System.out.println("How many words would you like to add to the word search?");
        int wordCount = userInput.nextInt();
        userInput.nextLine();
        String[] words = new String[wordCount];

        for (int i = 0; i < wordCount; i++) {
            System.out.println("Please enter the next word:");
            String word = (userInput.nextLine()).toUpperCase();
            boolean wordPlaced = false;
            for (int a = 0; a < 100; a++) {
                //
                if (wordPlaced) {
                    break;
                }
                int h = rand.nextInt(HEIGHT);
                int l = rand.nextInt(LENGTH);
                int tempH = h;
                int tempL = l;
                int direction = rand.nextInt(1, 4);
                boolean allValid = false;

                for (int j = 0; j < word.length(); j++) {
                    char tempChar;
                    char currentChar = word.charAt(j);

                    // tests if letter placement for a word goes out of bounds of the word search
                    try {
                        tempChar = answer[tempH][tempL];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        allValid = false;
                        break;
                    }

                    /*
                     * Allows placement of the letter if the space on the grid is either empty
                     * or if the space on the grid has the same letter as the one to be placed
                     * Allows crossover placement of words
                     */
                    if ((tempChar == currentChar) || (tempChar == 0)) {
                        allValid = true;
                    } else {
                        allValid = false;
                        break;
                    }

                    // Switch case moves the 'cursor' on the grid to the next space in the direction
                    switch (direction) {
                        case 1:
                            tempL++;
                            tempH--;
                            break;
                        case 2:
                            tempL++;
                            break;
                        case 3:
                            tempL++;
                            tempH++;
                            break;
                        case 4:
                            tempH++;
                            break;
                        default:
                            break;
                    }
                }

                // if the last(100th) attempt fails, adds the word to a list of omitted words
                if ((a == 99) && !allValid) {
                    words[i] = word;
                    break;
                }

                // Moves to next attempt to place word if current placement is invalid
                if (!allValid) {
                    continue;
                    // If placement is valid, goes ahead and edits the array, placing the letters
                    // down
                } else {
                    for (int j = 0; j < word.length(); j++) {
                        answer[h][l] = word.charAt(j);
                        switch (direction) {
                            case 1:
                                l++;
                                h--;
                                break;
                            case 2:
                                l++;
                                break;
                            case 3:
                                l++;
                                h++;
                                break;
                            case 4:
                                h++;
                                break;
                            default:
                                break;
                        }
                    }
                    wordPlaced = true;
                }
            }
        }

        // If any omitted words, displays them to the user
        for (int i = 0; i < wordCount; i++) {
            if (!(words[i] == null)) {
                System.out.println("The following words have been omitted from the word search:");
                System.out.println(words[i]);
            }
        }
        WS_GENERATED = true;
        return answer;
    }

    // print() takes the generated word search with filled letters and displays it
    // to the console
    public static void print(char[][] finalWS) {
        // tests if a word search has been generated
        if (!WS_GENERATED) {
            System.out.println("You need to generate a word search first!");
            return;
        }
        System.out.println();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < LENGTH; j++) {
                System.out.print(finalWS[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // showSolution() just shows the generated word search without the randomly
    // filled letters
    public static void showSolution(char[][] answer) {
        // tests if a word search has been generated
        if (!WS_GENERATED) {
            System.out.println("You need to generate a word search first!");
            return;
        }
        System.out.println();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < LENGTH; j++) {
                if (answer[i][j] == 0) {
                    System.out.print(".  ");
                } else {
                    System.out.print(answer[i][j] + "  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Takes a word search array with the placed words and fill the empty spaces
    // with random letters
    // Returns a finalized word search 2d array
    public static char[][] letterFiller(char[][] answer) {
        Random rand = new Random();
        char[][] finalWS = new char[HEIGHT][LENGTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < LENGTH; j++) {
                if (answer[i][j] == 0) {
                    finalWS[i][j] = (char) ('A' + rand.nextInt(0, 26));
                } else {
                    finalWS[i][j] = answer[i][j];
                }
            }
        }
        return finalWS;
    }

    // Prints the word search to a file named "wordSearch.txt"
    public static void filePrint(char[][] finalWS) throws FileNotFoundException {
        // tests if a word search has been generated
        if (!WS_GENERATED) {
            System.out.println("You need to generate a word search first!");
            return;
        }
        PrintStream output = new PrintStream(new File("wordSearch.txt"));
        System.out.println();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < LENGTH; j++) {
                output.print(finalWS[i][j] + "  ");
            }
            output.println();
        }
        System.out.println();
        output.close();
    }

    // Takes in user input and selects which method is necessary to execute the
    // listed function
    public static void methodSelector() throws FileNotFoundException {
        char selector = 0;
        Scanner userInput = new Scanner(System.in);
        char[][] answer = new char[HEIGHT][LENGTH];
        char[][] finalWS = new char[HEIGHT][LENGTH];

        while (selector != 'q') {
            printMenu();
            selector = (userInput.nextLine()).charAt(0);
            switch (selector) {
                case 'g':
                    answer = generate(userInput);
                    finalWS = letterFiller(answer);
                    break;
                case 'p':
                    print(finalWS);
                    break;
                case 's':
                    showSolution(answer);
                    break;
                case 'f':
                    filePrint(finalWS);
                    break;
                case 'q':
                    System.out.println("Thank you for using the Word Search Program. Bye Bye!");
                    break;
                default:
                    System.out.println("Please enter a valid option!!");
                    System.out.println();
                    break;
            }
        }

    }
}