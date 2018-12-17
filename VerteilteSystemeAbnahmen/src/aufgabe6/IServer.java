package aufgabe6;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Server Remote Stub
 * wird von den Clients zum an/abmelden verwendet
 * @author Bastian
 *
 */
public interface IServer extends Remote{
	/**
	 * meldet den Client an.
	 * Client erh�lt ab sofort Kurs-Updates �ber die kursAusgeben() Funktion von IClient
	 * @param client
	 * @throws RemoteException
	 */
    public void anmelden(IClient client) throws RemoteException;
    /**
     * meldet den Client ab.
     * Client erh�lt keine Kurs-Updates mehr
     * @param client
     * @throws RemoteException
     */
    public void abmelden(IClient client) throws RemoteException;
}
