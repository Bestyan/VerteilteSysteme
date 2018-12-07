package aufgabe6;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote{
    public void anmelden(IClient client) throws RemoteException;
    public void abmelden(IClient client) throws RemoteException;
}
