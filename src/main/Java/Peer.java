package main.Java;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Peer class.
 * Checks for other peers in the port range 50001 - 50010.
 * If other peers are available, the initial set of data is being exchanged,
 * and the data from the other peers collected and saved.
 * Accepts user input in order to print out the current data or halt execution.
 */
public class Peer {

    /**
     * Starts a PeerThread in order to handle the communication with other peers.
     * Waits for user input in order to print out status or exit application.
     *
     * @param args [0]: Name of the peer
     *             [1]: Port number to which the peer should listen
     * @throws IOException IOex
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Peer has been initialized with the following configuration:\n" +
                "Peer Name: " + args[0] + "\n" +
                "Port Number: " + args[1]);
        InputService inputService = new InputService();

        ArrayList<HashMap<String, String>> initialData = inputService.scanInitialData();

        PeerThread pt = new PeerThread(args[0], Integer.parseInt(args[1]), initialData);
        pt.start();

        boolean runloop = true;
        while (runloop) {

            String userInput = inputService.scanStatusControl();
                switch (userInput) {
                    case "p":
                        pt.getDataService().printAllData();
                        break;
                    case "x":
                        runloop = false;
                        break;
                }
            }
        pt.interrupt();
    }
}
