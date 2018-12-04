package aufgabe5;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRingPuffer extends Remote {
    
    public Object read() throws RemoteException;
    public void write(Object input) throws RemoteException;
    public String getZustand() throws RemoteException;
}
