package main.Java;

public class P2Protocol {

    static String stop() {
        return "P2Protocol STOP";
    }

    static boolean isSTOP(String data) {
        return data.equals("P2Protocol STOP");
    }

}
