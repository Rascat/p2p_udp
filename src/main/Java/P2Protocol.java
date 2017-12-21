package main.Java;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class P2Protocol {

    public static DatagramPacket connect(InetAddress address, int port)
    {
        String message = "P2Protocol CONNECT";
        return new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
    }

    public static DatagramPacket ok(InetAddress address, int port)
    {
        String message = "P2Protocol OK";
        return new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
    }

    public static DatagramPacket bye(InetAddress address, int port)
    {
        String message = "P2Protocol BYE";
        return new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
    }

    public static boolean isP2ProtocolPck(DatagramPacket packet)
    {
        String data = new String(packet.getData(), 0, packet.getLength());
        String[] dataElements = data.split(" ");
        return dataElements[0].equals("P2Protocol");
    }

    public static boolean isP2ProtocolOKPck(DatagramPacket packet)
    {
        String data = new String(packet.getData(), 0, packet.getLength());
        return data.equals("P2Protocol OK");
    }

    public static boolean isP2ProtocolCONNECTPck(DatagramPacket packet)
    {
        String data = new String(packet.getData(), 0, packet.getLength());
        return data.equals("P2Protocol CONNECT");
    }

    public static boolean isP2ProtocolBYEPck(DatagramPacket packet)
    {
        String data = new String(packet.getData(), 0, packet.getLength());
        return data.equals("P2Protocol BYE");
    }
}
