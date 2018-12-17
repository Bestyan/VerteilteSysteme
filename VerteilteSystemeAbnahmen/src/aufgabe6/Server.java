package aufgabe6;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server extends UnicastRemoteObject implements IServer {

	/**
	 * für Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Registry Port
	 */
	public static final int PORT = 16_000;

	/**
	 * aktueller Aktienkurs
	 */
	private double kurs;
	/**
	 * Liste aller angemeldeten Clienten, denen Kursänderungen mitgeteilt werden
	 */
	private List<IClient> clients = new ArrayList<>();

	protected Server(double kurs) throws RemoteException {
		super();
		this.kurs = kurs;
	}

	@Override
	public void anmelden(IClient client) throws RemoteException {
		/*
		 * Zugriff auf die Clientliste muss exklusiv sein, sonst potentiell komisches Verhalten. ArrayList ist nicht threadsafe
		 */
		synchronized (clients) {
			
			/*
			 * Client anmelden, sofern er noch nicht angemeldet ist
			 */
			if (!clients.contains(client)) {
				clients.add(client);
				System.out.println("-----");
				System.out.println("Ein Client hat sich angemeldet. Clients: " + clients.size());
			}
		}
	}

	@Override
	public void abmelden(IClient client) throws RemoteException {
		/*
		 * Zugriff auf die Clientliste muss exklusiv sein, sonst potentiell komisches Verhalten. ArrayList ist nicht threadsafe
		 */
		synchronized (clients) {
			
			/*
			 * Client abmelden, sofern er angemeldet ist
			 */
			if (clients.contains(client)) {
				clients.remove(client);
				System.out.println("-----");
				System.out.println("Ein Client hat sich abgemeldet. Clients: " + clients.size());
			}
		}
	}

	/**
	 * verändert den Kurs zufällig um -5 bis +5 und gibt den neuen Kurs auf der Konsole aus
	 */
	public void kursAnpassen() {
		Random random = new Random();
		double veraenderung = (random.nextDouble() * 2 - 1) * 5;
		System.out.println("-----");
//		System.out.println(String.format("Alter Kurs: %.2f", this.kurs));

		/*
		 * falls der Kurs unter 0 fallen würde, Vorzeichen der Kursänderung umdrehen
		 */
		if (this.kurs + veraenderung < 0) {
			veraenderung *= -1;
		}
		this.kurs += veraenderung;

		/*
		 * neuen Kurs mit Veränderung ausgeben
		 */
		System.out.println(String.format("Neuer Kurs: %.2f (", this.kurs) + (veraenderung > 0 ? "+" : "")
				+ String.format("%.2f)", veraenderung));
	}

	/**
	 * gibt den aktuellen Kurs bei allen Clients aus
	 */
	public void kursAnsagen() {
		/*
		 * Zugriff auf die Clientliste muss exklusiv sein, sonst potentiell komisches Verhalten. ArrayList ist nicht threadsafe
		 */
		synchronized (clients) {
			
			/*
			 * alle Stubs durchgehen und kursAusgeben aufrufen mit dem aktuellen Kurs
			 */
			for (IClient client : clients) {
				try {
					client.kursAusgeben(String.format("%.2f", this.kurs));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		Registry registry = LocateRegistry.createRegistry(PORT);

		/*
		 * neuen Server mit Anfangskurs von 10 erzeugen
		 */
		Server server = new Server(10.0);
		/*
		 * Server für Clients zugänglich machen
		 */
		registry.rebind("server", server);

		while (true) {
			server.kursAnpassen();
			server.kursAnsagen();
			
			try {
				/*
				 * 2-4 Sekunden warten, dann nächste Kursanpassung
				 */
				Thread.sleep(new Random().nextInt(3) * 1000 + 2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
