package main.Java;

/**
 * Used to handle the communication between two peers.
 */
public class P2Protocol {

    /**
     * Used to create P2Protocol stop signal
     * @return P2Protocol stop signal
     */
    static String stop() {
        return "P2Protocol STOP";
    }

    /**
     * Used to check whether a given string is a P2Protocol stop signal
     * @param data String that is to be checked
     * @return True if provided string is P2Protocol stop signal
     */
    static boolean isSTOP(String data) {
        return data.equals("P2Protocol STOP");
    }

}
