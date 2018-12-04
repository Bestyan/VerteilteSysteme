package aufgabe5;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Server {
    
    public static final int PORT = 15_000;

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        RingPuffer ringPuffer = new RingPuffer(5);
        Registry registry = LocateRegistry.createRegistry(PORT);
        
        registry.rebind("ringpuffer", ringPuffer);
        System.out.println(Arrays.toString(registry.list()));
        System.out.println("Server started");
    }
    
}
