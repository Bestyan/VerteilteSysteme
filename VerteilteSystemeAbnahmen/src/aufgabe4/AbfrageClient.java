package aufgabe4;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import aufgabe4.ClientNachricht.NachrichtenTyp;

public class AbfrageClient {

    private static final int SERVER_PORT = 10_000;

    Socket connection;

    public AbfrageClient(String host) {
        super();
        try {
            System.out.println("Connecting ...");
            
            connection = new Socket(host, SERVER_PORT);
            
            System.out.println("Connection established");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abfragen() {
        try{
            System.out.println("Requesting poll results ...");
            
            ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream()); 
            
            ClientNachricht request = new ClientNachricht(NachrichtenTyp.ABFRAGE);
            oos.writeObject(request);
            
            String response = (String) ois.readObject();
            System.out.println("Poll results: ");
            System.out.println(response);
            
            connection.close();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        AbfrageClient client = new AbfrageClient("127.0.0.1");
        client.abfragen();
    }
}
