package aufgabe5;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class AnfrageClient {

    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Host?");
        String host = scanner.nextLine();
        
        System.out.println("");
        System.out.println("Verbinden...");
        IRingPuffer ringPuffer = (IRingPuffer) Naming.lookup("rmi://" + host + ":" + Server.PORT + "/ringpuffer");
        System.out.println("Verbunden!");
        System.out.println("");
        
        System.out.println("Enter zum Abfragen, x zum Beenden");
        while(true) {
            String input = scanner.nextLine();
            if(input.equals("x")) {
                break;
            }
            System.out.println("");
            System.out.println(ringPuffer.getZustand());
            System.out.println("");
        }
        scanner.close();
    }
}
