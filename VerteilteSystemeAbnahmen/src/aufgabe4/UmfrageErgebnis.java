package aufgabe4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class UmfrageErgebnis implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final transient String speicherort = "src/aufgabe4/umfrageErgebnis.save";
    private static final transient Object fileLock = new Object();

    private int anzahlJa = 0;
    private int anzahlNein = 0;
    private int anzahlEnthaltung = 0;
    private String frage;

    /**
     * Versucht ein UmfrageErgebnis-Objekt aus der /umfrageErgebnis.save zu laden
     * 
     * @return geladenes UmfrageErgebnis Objekt oder null falls nicht vorhanden
     */
    public static UmfrageErgebnis laden() {
        UmfrageErgebnis result = null;
        File file = new File(speicherort);
        System.out.println("Save file at: " + file.getAbsolutePath());
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                result = (UmfrageErgebnis) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public UmfrageErgebnis(String frage) {
        super();
        this.frage = frage;
    }

    public String abstimmen(ClientNachricht.Votum votum) {
        switch (votum) {
            case JA:
                this.abstimmenJa();
                break;
            case NEIN:
                this.abstimmenNein();
                break;
            case ENTHALTUNG:
                this.abstimmenEnthaltung();
                break;
        }
        return "Sie haben folgendermaßen abgestimmt: " + votum.toString();
    }

    private synchronized void abstimmenJa() {
        this.anzahlJa++;
    }

    private synchronized void abstimmenNein() {
        this.anzahlNein++;
    }

    private synchronized void abstimmenEnthaltung() {
        this.anzahlEnthaltung++;
    }

    public int getAnzahlJa() {
        return anzahlJa;
    }

    public int getAnzahlNein() {
        return anzahlNein;
    }

    public int getAnzahlEnthaltung() {
        return anzahlEnthaltung;
    }

    public String getFrage() {
        return frage;
    }

    @Override
    public String toString() {
        return "----------\nUmfrage: " + this.getFrage() + "\nJA: " + this.getAnzahlJa() + "\nNEIN: "
                + this.getAnzahlNein() + "\nENTHALTUNG: " + this.getAnzahlEnthaltung() + "\n----------";
    }
    
    public static void speichern(UmfrageErgebnis umfrageErgebnis) {
        synchronized(fileLock) {
            File file = new File(speicherort);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false))) {
                oos.writeObject(umfrageErgebnis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
