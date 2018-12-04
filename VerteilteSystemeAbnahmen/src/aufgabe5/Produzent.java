package aufgabe5;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Produzent extends Thread {
    
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Host?");
        String host = scanner.nextLine();
        
        System.out.println("");
        System.out.println("Verbinden...");
        IRingPuffer ringPuffer = (IRingPuffer) Naming.lookup("rmi://" + host + ":" + Server.PORT + "/ringpuffer");
        System.out.println("Verbunden!");
        System.out.println("");
        
        System.out.println("Produkt?");
        String produkt = scanner.nextLine();
        
        new Produzent(ringPuffer, produkt).start();
        scanner.close();
    }
    
    private IRingPuffer ringPuffer;
    private String produkt;
    
    public Produzent(IRingPuffer ringPuffer, String produkt) {
        super();
        this.ringPuffer = ringPuffer;
        this.produkt = produkt;
    }
    
    public void produzieren() {
        try {
            ringPuffer.write(produkt);
            System.out.println("Produzent hat \"" + produkt + "\" produziert.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            produzieren();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
