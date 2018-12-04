package aufgabe5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class RingPuffer extends UnicastRemoteObject implements IRingPuffer{
    private static final long serialVersionUID = 1;
    private Object[] puffer;
    /**
     * array-index für das nächste einzufügende Objekt
     */
    private int in = 0;
    /**
     * array-index für das nächste auszulesende Objekt
     */
    private int out = 0;
    
    /**
     * lock für schreibvorgänge
     */
    private static final Object inLock = new Object();
    /**
     * lock für lesevorgänge
     */
    private static final Object outLock = new Object();
    
    
    public RingPuffer(int size) throws RemoteException {
        super();
        puffer = new Object[size];
    }
    
    @Override
    public Object read() {
        Object result;
        synchronized(outLock) {     
            
            //prüfen, ob mindestens ein Objekt im Puffer vorhanden ist, falls nein --> warten
            while(out == in) {
                try {
                    outLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            result = puffer[out];
            puffer[out] = null;
            out = (out + 1) % puffer.length;
            synchronized(inLock) {
                inLock.notifyAll();
            }
        }
        return result;
    }
    
    @Override
    public void write(Object input) {
        synchronized(inLock) {
            
            //prüfen ob Puffer nur noch einen Platz frei hat, falls ja --> warten
            while( (in+1) % puffer.length == out) {
                try {
                    inLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            puffer[in] = input;
            in = (in + 1) % puffer.length;
            synchronized(outLock) {
                outLock.notifyAll();
            }
        }
    }
    
    @Override
    public String getZustand() {
        String result = "Groesse: " + puffer.length +"\r\n";
        result += "in = " + in + "\r\n";
        result += "out = " + out + "\r\n";
        result += "Puffer: " + Arrays.toString(puffer);
        return result;
    }
    
}
