package main.Java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Used for User - Application interactions
 */
public class InputService {

    private Scanner inputScanner;

    /**
     * Constructor
     */
    InputService() {
        this.inputScanner = new Scanner(System.in);
    }

    /**
     * Scans a peer's initial data from command line
     * @return User input
     */
    public ArrayList<HashMap<String, String>> scanInitialData() {
        ArrayList<HashMap<String, String>> initialData = new ArrayList<>();
        while (true) {
            System.out.println(
                    "\n Enter (n) to proceed with transmission of artist/title information\n" +
                            "Enter (q) to quit data transmission");
            String userInput = inputScanner.next();
            if (!(userInput.equals("n")) && !(userInput.equals("q"))) {
                System.out.println("Invalid Input");
            } else if (userInput.equals("n")) {
                HashMap<String, String> inputMap = new HashMap<>();

                System.out.println("Artist:");
                String artist = inputScanner.next();
                inputMap.put("artist", artist);
                System.out.println("Title:");
                String title = inputScanner.next();
                inputMap.put("title", title);

                initialData.add(inputMap);
            } else if (userInput.equals("q")) {
                break;
            }
        }
        return initialData;
    }

    /**
     * Input routine that scans control flow commands
     * @return User Input
     */
    public String scanStatusControl() {
        while (true) {
            System.out.println("Type (p) to print all available data or (x) to exit program");
            String userInput = inputScanner.next();

            if (!(userInput.equals("p")) && !(userInput.equals("x"))) {
                System.out.println("Invalid Input");
            } else {
                return userInput;
            }
        }
    }
}
