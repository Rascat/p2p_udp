package main.Java;

import java.io.*;
import java.util.Scanner;

public class Peer {

    public static void main(String[] args) throws IOException {
        PeerThread pt = new PeerThread(args[0], Integer.parseInt(args[1]), args[2], args[3]);
        pt.start();

        Scanner inputScanner = new Scanner(System.in);
        boolean runloop = true;
        while (runloop) {

            System.out.println("Type (P) to print all available data or (X) to exit program");
            String userInput = inputScanner.next();

            if (!(userInput.equals("p")) && !(userInput.equals("x"))) {
                System.out.println("Invalid Input");
            } else {
                switch (userInput) {
                    case "p":
                        pt.getDataService().printAllData();
                        break;
                    case "x":
                        runloop = false;
                        break;
                }
            }
        }
        pt.interrupt();
    }
}
