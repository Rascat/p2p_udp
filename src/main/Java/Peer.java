package main.Java;
import java.io.*;

public class Peer {

    public static void main(String[] args) throws IOException {
        new PeerThread(args[0],Integer.parseInt(args[1]), args[2], args[3]).start();
    }
}
