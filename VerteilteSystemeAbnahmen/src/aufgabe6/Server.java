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

	private static final long serialVersionUID = 1L;

	public static final int PORT = 16_000;

	private double kurs;
	private List<IClient> clients = new ArrayList<>();

	protected Server(double kurs) throws RemoteException {
		super();
		this.kurs = kurs;
	}

	@Override
	public void anmelden(IClient client) throws RemoteException {
		synchronized (clients) {
			if (!clients.contains(client)) {
				clients.add(client);
				System.out.println("-----");
				System.out.println("Ein Client hat sich angemeldet. Clients: " + clients.size());
			}
		}
	}

	@Override
	public void abmelden(IClient client) throws RemoteException {
		synchronized (clients) {
			if (clients.contains(client)) {
				clients.remove(client);
				System.out.println("-----");
				System.out.println("Ein Client hat sich abgemeldet. Clients: " + clients.size());
			}
		}
	}

	public void kursAnpassen() {
		Random random = new Random();
		double veraenderung = (random.nextDouble() * 2 - 1) * 5;
		System.out.println("-----");
//		System.out.println(String.format("Alter Kurs: %.2f", this.kurs));

		if (this.kurs + veraenderung < 0) {
			veraenderung *= -1;
		}
		this.kurs += veraenderung;

		System.out.println(String.format("Neuer Kurs: %.2f (", this.kurs) + (veraenderung > 0 ? "+" : "")
				+ String.format("%.2f)", veraenderung));
	}

	public void kursAnsagen() {
		synchronized (clients) {
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

		Server server = new Server(10.0);
		registry.rebind("server", server);

		while (true) {
			server.kursAnpassen();
			server.kursAnsagen();
			try {
				Thread.sleep(new Random().nextInt(3) * 1000 + 2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
