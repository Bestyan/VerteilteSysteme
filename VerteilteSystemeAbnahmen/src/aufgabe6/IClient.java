package aufgabe6;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Client Remote Stub
 * wird vom Server verwendet, um den Kurs bei den Clients auszugeben
 * @author Bastian
 *
 */
public interface IClient extends Remote{
	/**
	 * gibt den Kurs auf der Konsole aus
	 * @param kurs
	 * @throws RemoteException
	 */
    public void kursAusgeben(String kurs) throws RemoteException;
}
