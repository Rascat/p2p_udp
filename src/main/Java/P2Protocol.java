package main.Java;

public class P2Protocol {

    static String connect() {
        return "P2Protocol CONNECT";
    }

    static String ok() {
        return "P2Protocol OK";
    }

    static String stop() {
        return "P2Protocol STOP";
    }

    static boolean isSTOP(String data) {
        return data.equals("P2Protocol STOP");
    }


    static String bye() {
        return "P2Protocol BYE";
    }

    public static boolean isP2ProtocolPck(String data) {
        String[] dataElements = data.split(" ");
        return dataElements[0].equals("P2Protocol");
    }

    static boolean isOK(String data) {
        return data.equals("P2Protocol OK");
    }

    static boolean isCONNECT(String data) {
        return data.equals("P2Protocol CONNECT");
    }

    static boolean isBYE(String data) {
        return data.equals("P2Protocol BYE");
    }
}
