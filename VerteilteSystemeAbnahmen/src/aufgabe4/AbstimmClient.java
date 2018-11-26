package aufgabe4;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import aufgabe4.ClientNachricht.NachrichtenTyp;
import aufgabe4.ClientNachricht.Votum;

public class AbstimmClient {

    private static final int SERVER_PORT = 10_000;

    Socket connection;

    public AbstimmClient(String host) {
        super();
        try {
            System.out.println("Connecting ...");

            connection = new Socket(host, SERVER_PORT);

            System.out.println("Connection established");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abstimmen(ClientNachricht nachricht) {
        System.out.println("Voting...");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());

            oos.writeObject(nachricht);

            String response = (String) ois.readObject();
            System.out.println(response);
            

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AbstimmClient client = new AbstimmClient("127.0.0.1");

        Scanner scanner = new Scanner(System.in);
        ClientNachricht nachricht = stimmeErfassen(scanner);

        client.abstimmen(nachricht);
    }

    private static ClientNachricht stimmeErfassen(Scanner scanner) {
        System.out.println("Your vote? (J|N|E)");
        
        ClientNachricht nachricht = new ClientNachricht(NachrichtenTyp.ABSTIMMEN);

        for (boolean validInput = false; !validInput;) {
            validInput = true;
            String input = scanner.next();
            switch (input) {
                case "J":
                    nachricht.setVotum(Votum.JA);
                    break;
                case "N":
                    nachricht.setVotum(Votum.NEIN);
                    break;
                case "E":
                    nachricht.setVotum(Votum.ENTHALTUNG);
                    break;
                default:
                    validInput = false;
                    System.out.println("Invalid Input");
                    System.out.println("Your vote? (J|N|E)");
                    break;
            }
        }
        return nachricht;
    }
}
