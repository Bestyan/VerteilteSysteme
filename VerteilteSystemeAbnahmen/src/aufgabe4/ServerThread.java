package aufgabe4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread{
    private Socket connection;

    public ServerThread(Socket connection) {
        super();
        this.connection = connection;
    }
    
    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            ClientNachricht nachricht = (ClientNachricht) in.readObject();
            
            switch(nachricht.getTyp()) {
                case ABFRAGE:
                    out.writeObject(Server.umfrageErgebnis.toString());
                    break;
                case ABSTIMMEN:
                    out.writeObject(Server.umfrageErgebnis.abstimmen(nachricht.getVotum()));
                    UmfrageErgebnis.speichern(Server.umfrageErgebnis);
                    break;
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("received trash mail from " + connection.getInetAddress().getCanonicalHostName());
            e.printStackTrace();
        }
    }
}
