package aufgabe4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 10_000;
    
    public static UmfrageErgebnis umfrageErgebnis;

    public static void main(String args[]) {
        umfrageErgebnis = UmfrageErgebnis.laden();
        if(umfrageErgebnis == null) {
            umfrageErgebnis = new UmfrageErgebnis("Antworten Sie mit JA, NEIN oder ENTHALTUNG");
        }
        
        try (ServerSocket socket = new ServerSocket(PORT)) {
            System.out.println("Server started ...");
            System.out.println(umfrageErgebnis.toString());
            while (true) {
                Socket connection = socket.accept();
                new ServerThread(connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
