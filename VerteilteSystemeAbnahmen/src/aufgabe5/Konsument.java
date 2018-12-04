package aufgabe5;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Konsument extends Thread {
    
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Host?");
        String host = scanner.nextLine();
        
        System.out.println("");
        System.out.println("Verbinden...");
        IRingPuffer ringPuffer = (IRingPuffer) Naming.lookup("rmi://" + host + ":" + Server.PORT + "/ringpuffer");
        System.out.println("Verbunden!");
        System.out.println("");
        
        System.out.println("Name des Konsumenten?");
        String name = scanner.nextLine();
        
        new Konsument(ringPuffer, name).start();
        scanner.close();
    }
    
    private IRingPuffer ringPuffer;
    private String name;
    
    public Konsument(IRingPuffer ringPuffer, String name) {
        super();
        this.ringPuffer = ringPuffer;
        this.name = name;
    }
    
    public void konsumieren() {
        try {
            Object produkt = ringPuffer.read();
            System.out.println(name + " hat \"" + produkt + "\" konsumiert.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            konsumieren();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
