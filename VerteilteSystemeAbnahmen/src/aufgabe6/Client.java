package aufgabe6;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements IClient {

    private static final long serialVersionUID = 1L;

    protected Client() throws RemoteException {
        super();
    }

    @Override
    public void kursAusgeben(String kurs) throws RemoteException {
        System.out.println("Der Kurs steht bei " + kurs);
    }

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Host?");
        String host = scanner.nextLine();

        System.out.println("Verbinden...");
        IServer server = (IServer) Naming.lookup("rmi://" + host + ":" + Server.PORT + "/server");
        System.out.println("Verbunden!");
        
        Client client = new Client();
        System.out.println("Enter drücken, zum anmelden");
        scanner.nextLine();
        
        System.out.println("Anmelden...");
        server.anmelden(client);
        System.out.println("Angemeldet!");
        
        System.out.println("Enter drücken, zum abmelden");
        scanner.nextLine();
        
        System.out.println("Abmelden...");
        server.abmelden(client);
        System.out.println("Abgemeldet!");
        
        scanner.close();
        System.exit(0);
    }

}
