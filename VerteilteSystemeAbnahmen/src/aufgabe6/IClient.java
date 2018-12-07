package aufgabe6;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote{
    public void kursAusgeben(String kurs) throws RemoteException;
}
