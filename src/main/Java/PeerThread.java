package main.Java;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Takes care of communication in the p2p network.
 * When an instance of this class is created, a
 * DatagramSocket is bound to the provided port.
 * Also, the initial data in the form of an artist and
 * an title string is being handed over to an instance
 * of DataService.
 */
public class PeerThread extends Thread {

    private DatagramSocket socket;
    private DataService dataService;
    private String status;
    private InetAddress localHost;

    /**
     * Constructor
     * @param name Name of the thread. Alias for the name of the peer.
     * @param portNr Number of the port to which the DatagramSocket is bound.
     * @throws IOException Thrown if socket cannot be bound to the provided port.
     *                      Thrown if localhost cannot be found.
     */
    PeerThread(String name, int portNr, ArrayList<HashMap<String, String>> initialData) throws IOException {
        super(name);
        this.socket = new DatagramSocket(portNr);
        this.dataService = new DataService(initialData);
        this.status = "UNKNOWN";
        this.localHost = InetAddress.getLocalHost();
    }

    /**
     * Getter for dataService
     * @return dataService
     */
    public DataService getDataService()
    {
        return this.dataService;
    }

    /**
     * Main routine
     */
    public void run() {

        // Exit loop if thread is being interrupted from Peer main method.
        while (!this.isInterrupted()) {

            if (this.status.equals("UNKNOWN")) {
                multicastInitialData();
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
        }
    }

    /**
     * Used to send UDP-Datagrams with initial data to every port in range
     * 50001 to 50010. Introduces new data to the p2p network.
     */
    private void multicastInitialData() {
        ArrayList<String> initialData = this.dataService.getCommaSeparatedInitialData();

        for (int port = 50001; port < 50010; port += 1) {
            // Don't send traffic to own port
            if (port == this.socket.getLocalPort()) {
                continue;
            }
            sendDataSequence(this.localHost, port, initialData);
        }
        System.out.println(this.getName() + " finished broadcasting initial data");
        this.status = "WAITING";
    }

    /**
     * Used to receive data sequences from other peers.
     * A data sequence is sequence of UDP-Datagrams with data relevant
     * to the DataService in the payload followed by a datagram with a
     * P2Protocol stop signal in the payload.
     *
     * @param milliseconds Time in ms the socket awaits incoming datagrams
     * @return Port number of the sending peer.
     * @throws IOException Socket Error
     */
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

            // exit loop if stop signal has been send
            if (P2Protocol.isSTOP(receivedData)) {
                break;
            } else {
                this.dataService.addCommaSeparatedData(receivedData);
            }
        }
        return portOfSender;
    }

    /**
     * Sends a data sequence to a peer with the provided inet-address/ port-number
     * configuration.
     *
     * @param address Address of the targeted peer
     * @param port Port number of the targeted peer
     * @param data Data which should be placed in the datagrams payload.
     */
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

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
