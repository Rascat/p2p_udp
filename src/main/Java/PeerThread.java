package main.Java;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PeerThread extends Thread {

    private DatagramSocket socket = null;
    private ArrayList<String> data = new ArrayList<>();
    private Map<String, String> connPeer = null;
    private InetAddress connPeerAddress = null;
    private int connPeerPort = 0;
    private String status = null;

    PeerThread() throws IOException {
        this("PeerThread", 5000, "Date: " + (new Date()).toString());
    }

    PeerThread(String name, int portNr, String data) throws IOException {
        super(name);
        this.socket = new DatagramSocket(portNr);
        this.data.add(data);
        this.connPeer = new HashMap<>();
        this.status = "UNKNOWN";
    }

    public void run() {

        while (true) {

            if (this.status.equals("UNKNOWN")) {
                establishConnection();
            }

            if (this.status.equals("READY")) {
                waitOnConnectionRequest();
                receiveData(); // status now CONNECTED
                sendData();
                endConnection();
            }

            if (this.status.equals("CONNECTED")) {
                sendData();
                receiveData();
                endConnection();
            }

        }

    }

    private void establishConnection() {

        byte[] buf = new byte[512];
        DatagramPacket rp = new DatagramPacket(buf, buf.length);

        for (int port = 50001; port < 50010; port += 1) {
            // Don't send traffic connection request to own port
            if (port == this.socket.getLocalPort()) {
                continue;
            }
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                this.socket.send(P2Protocol.connect(localHost, port));
                System.out.println("Peer named " + this.getName() + " send connection request");

                this.socket.setSoTimeout(500);
                this.socket.receive(rp);

                if (P2Protocol.isP2ProtocolOKPck(rp)) {
                    this.connPeerAddress = rp.getAddress();
                    this.connPeerPort = rp.getPort();
                    System.out.println("Connected to: " + this.connPeerAddress + " on Port: " + this.connPeerPort);
                    this.status = "CONNECTED";
                    return;
                }

            } catch (SocketTimeoutException stEx) {
                System.out.println("Timeout reached");
                //this.socket.close();
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
                System.out.println("Something went horribly wrong");
            }
        }
        this.status = "READY";
        System.out.println("Could not connect to peer");
    }

    private void waitOnConnectionRequest()
    {
        byte[] buf = new byte[512];
        DatagramPacket rp = new DatagramPacket(buf, buf.length);

        try{
            this.socket.setSoTimeout(0);
            this.socket.receive(rp);

            if (P2Protocol.isP2ProtocolCONNECTPck(rp)) {
                this.connPeerAddress = rp.getAddress();
                this.connPeerPort = rp.getPort();

                this.socket.send(P2Protocol.ok(rp.getAddress(), rp.getPort()));
                this.status = "CONNECTED";
                System.out.println("Peer is connected with " + this.connPeerAddress + "on Port: " + this.connPeerPort);
                return;
            }

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
        System.out.println("Waited without success");
    }

    private void sendData()
    {
        try {
            byte[] buf = this.data.get(0).getBytes();
            InetAddress address =  this.connPeerAddress;
            int port = this.connPeerPort;

            DatagramPacket sp = new DatagramPacket(buf, buf.length, address, port);
            this.socket.send(sp);

        } catch (UnknownHostException uhEx) {
            System.out.println("WTF");
            uhEx.printStackTrace();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    private void receiveData()
    {
        try{
            byte[] buf = new byte[512];
            DatagramPacket rp = new DatagramPacket(buf, buf.length);

            socket.receive(rp);
            if (P2Protocol.isP2ProtocolPck(rp)) {
                System.out.println("That was a P2Protocol packet");
            } else {
                String data = new String(rp.getData(), 0, rp.getLength());
                System.out.println("Received: " + data);
                this.data.add(data);
            }
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    private void endConnection()
    {
        try{
            socket.send(P2Protocol.bye(this.connPeerAddress, this.connPeerPort));
            this.connPeerAddress = null;
            this.connPeerPort = 0;
            this.status = "READY";

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            System.out.println("Could not end connection");
        }
    }

    private void printPeerData() {
        System.out.println("Peer named " + this.getName() + " has the following data:");
        for (String dataItem : this.data) {
            System.out.println(dataItem);
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
