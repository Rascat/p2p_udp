package main.Java;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class PeerThread extends Thread {

    private DatagramSocket socket;
    private DataService dataService;
    private String status;
    private InetAddress localHost;

    PeerThread(String name, int portNr, String artist, String title) throws IOException {
        super(name);
        this.socket = new DatagramSocket(portNr);
        this.dataService = new DataService(artist, title);
        this.status = "UNKNOWN";
        this.localHost = InetAddress.getLocalHost();
    }

    public DataService getDataService()
    {
        return this.dataService;
    }

    public void run() {

        while (!this.isInterrupted()) {

            if (this.status.equals("UNKNOWN")) {
                broadcastInitialData();
                // receive data sequences from n possible peers
                while (true) {
                    try {
                        receiveDataSequence(100);
                    } catch (IOException ioEx) {
                        break;
                    }
                }
            }

            if (this.status.equals("WAITING")) {
                try {
                    // System.out.println("Warte auf neue daten");
                    int portOfSender = receiveDataSequence(2000);
                    sendDataSequence(this.localHost, portOfSender, this.dataService.getCommaSeparatedInitialData());
                } catch (IOException ioEx) {
                   // System.out.println("...");
                }
            }
            // this.dataService.printAllData();
        }
    }

    private void broadcastInitialData() {
        ArrayList<String> initialData = this.dataService.getCommaSeparatedInitialData();

        for (int port = 50001; port < 50010; port += 1) {
            // Don't send traffic to own port
            if (port == this.socket.getLocalPort()) {
                continue;
            }
            sendDataSequence(this.localHost, port, initialData);
        }
        System.out.println(this.getName() + "finished broadcasting initial data");
        this.status = "WAITING";
    }


    private int receiveDataSequence(int milliseconds) throws IOException {
        byte[] buf = new byte[512];
        int portOfSender;
        while (true) {

            DatagramPacket rp = new DatagramPacket(buf, buf.length);
            this.socket.setSoTimeout(milliseconds);
            this.socket.receive(rp);
            String receivedData = new String(rp.getData(),0, rp.getLength());
            System.out.println("received data: " + receivedData);
            portOfSender = rp.getPort();

            if (P2Protocol.isSTOP(receivedData)) {
                break;
            } else {
                this.dataService.addCommaSeparatedData(receivedData);
            }
        }
        return portOfSender;
    }

    private void sendDataSequence(InetAddress address, int port, ArrayList<String> data) {
        try {
            byte[] buf;

            for (String dataItem : data) {
                buf = dataItem.getBytes();
                DatagramPacket dataPacket = new DatagramPacket(buf, buf.length, address, port);
                this.socket.send(dataPacket);
            }

            byte[] byeBuf = P2Protocol.stop().getBytes();
            DatagramPacket stopPacket = new DatagramPacket(byeBuf, byeBuf.length, address, port);
            this.socket.send(stopPacket);

        } catch (UnknownHostException uhEx) {
            System.out.println("WTF");
            uhEx.printStackTrace();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }


    private void printPacketInfo(DatagramPacket packet) {
        System.out.println("\n Packet Meta Data:");
        System.out.println("Address: " + packet.getAddress());
        System.out.println("Port: " + packet.getPort());
        System.out.println("\n Packet Data:");
        System.out.println("Data: " + new String(packet.getData(), 0, packet.getLength()) + "\n");


    }
}
