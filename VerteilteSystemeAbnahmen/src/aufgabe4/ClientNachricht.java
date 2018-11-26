package aufgabe4;

import java.io.Serializable;

public class ClientNachricht implements Serializable {

    enum NachrichtenTyp {
        ABSTIMMEN, ABFRAGE
    }
    
    public static enum Votum{
        JA, NEIN, ENTHALTUNG
    }

    private static final long serialVersionUID = 1L;

    private NachrichtenTyp typ;
    private Votum votum;

    public ClientNachricht(NachrichtenTyp typ, Votum votum) {
        super();
        this.typ = typ;
        this.votum = votum;
    }
    
    public ClientNachricht(NachrichtenTyp typ) {
        super();
        this.typ = typ;
    }

    public NachrichtenTyp getTyp() {
        return typ;
    }

    public void setTyp(NachrichtenTyp typ) {
        this.typ = typ;
    }

    public Votum getVotum() {
        return votum;
    }

    public void setVotum(Votum votum) {
        this.votum = votum;
    }


}
