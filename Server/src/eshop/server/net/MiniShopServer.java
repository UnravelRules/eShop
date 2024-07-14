package eshop.server.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import eShop.common.interfaces.eShopInterface;
import eshop.server.domain.eShop;

public class MiniShopServer {

    public final static int DEFAULT_PORT = 6789;

    protected int port;
    protected ServerSocket serverSocket;
    private eShopInterface eshop;

    public MiniShopServer(int port) throws IOException {

        eshop = new eShop("Kunden", "Mitarbeiter", "Artikel", "Ereignis");

        if (port == 0)
            port = DEFAULT_PORT;
        this.port = port;

        try {
            // Server-Socket anlegen
            serverSocket = new ServerSocket(port);

            // Serverdaten ausgeben
            InetAddress ia = InetAddress.getLocalHost();
//			Diese Anweisung liefert zu meiner Überraschung nicht das Gewünschte:
//			InetAddress ia = serverSocket.getInetAddress();
            System.out.println("Host: " + ia.getHostName());
            System.out.println("Server *" + ia.getHostAddress()	+ "* lauscht auf Port " + port);
        } catch (IOException e) {
            fail(e, "Eine Ausnahme trat beim Anlegen des Server-Sockets auf");
        }
    }

    /**
     * Methode zur Entgegennahme von Verbindungswünschen durch Clients.
     * Die Methode fragt wiederholt ab, ob Verbindungsanfragen vorliegen
     * und erzeugt dann jeweils ein ClientRequestProcessor-Objekt mit dem
     * fuer diese Verbindung erzeugten Client-Socket.
     */
    public void acceptClientConnectRequests() {

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientRequestProcessor c = new ClientRequestProcessor(clientSocket, eshop);
                Thread t = new Thread(c);
                t.start();
                System.out.println("Client verbunden");
            }
        } catch (IOException e) {
            fail(e, "Fehler während des Lauschens auf Verbindungen");
        }
    }


    /**
     * main()-Methode zum Starten des Servers
     *
     * @param args kann optional Portnummer enthalten, auf der Verbindungen entgegengenommen werden sollen
     */
    public static void main(String[] args) {
        int port = 0;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                port = 0;
            }
        }
        try {
            MiniShopServer server = new MiniShopServer(port);
            server.acceptClientConnectRequests();
        } catch (IOException e) {
            e.printStackTrace();
            fail(e, " - MiniBibServer-Erzeugung");
        }
    }

    // Standard-Exit im Fehlerfall:
    private static void fail(Exception e, String msg) {
        System.err.println(msg + ": " + e);
        System.exit(1);
    }
}
