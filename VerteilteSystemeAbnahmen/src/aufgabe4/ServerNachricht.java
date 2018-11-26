package aufgabe4;

import java.io.Serializable;

public class ServerNachricht implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String antwort;

    public ServerNachricht(String antwort) {
        super();
        this.antwort = antwort;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }
    
}
