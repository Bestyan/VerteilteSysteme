package aufgabe2;

import java.util.Stack;

public class WorkerThread extends Thread {

    /**
     * zu lösende Rechenaufgaben
     */
    Stack<RechenAufgabe> aufgaben;
    /**
     * Ergebnismatrix
     */
    private int[][] ergebnis;
    /**
     * Name des Threads (fürs Log)
     */
    private String name;

    public WorkerThread(Stack<RechenAufgabe> aufgaben, int[][] ergebnis, String name) {
        super();
        this.aufgaben = aufgaben;
        this.ergebnis = ergebnis;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            //Startvorsprung von der Reihenfolge der Erzeugung ausgleichen
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        while(true) {
            
            RechenAufgabe aufgabe;
            
            synchronized (aufgaben) {
                //prüfen, ob noch Aufgaben vorhanden sind
                if(aufgaben.size() == 0) {
                    System.out.println(name + " has no tasks left");
                    return;
                }
                aufgabe = aufgaben.pop();
            }
            
            ergebnis[aufgabe.getY()][aufgabe.getX()] = aufgabe.loesen();
            System.out.println(name + " solved " + aufgabe.toString());
        }
    }

}
