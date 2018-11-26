package aufgabe2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {

        // @formatter:off
        int[][] a = new int[][] { 
            { 1, -2, 3, 4, -1 }, 
            { -2, 3, 0, 1, 2 }, 
            { 4, -1, 2, 1, -2 }, 
            { -2, 1, 3, -1, 3 },
            { 0, 2, -1, 2, 4 } };
//        int[][] a = new int[][] { 
//            { 1, -2, 3, 4}, 
//            { -2, 3, 0, 1}, 
//            { 4, -1, 2, 1}, 
//            { -2, 1, 3, -1},
//            { 0, 2, -1, 2} };

        int[][] b = new int[][] { 
            { 2, -4, -1, 1, -2 }, 
            { -1, 1, -2, 2, 1 }, 
            { 5, 0, 3, -2, -4 }, 
            { 1, -2, 1, 0, 2 },
            { 2, 3, -3, 0, 0 } };
//        int[][] b = new int[][] { 
//            { 2, -4, -1, 1, -2 }, 
//            { -1, 1, -2, 2, 1 }, 
//            { 5, 0, 3, -2, -4 }, 
//            { 1, -2, 1, 0, 2 }};
        // @formatter:on

        /*
         * Master Worker Paradigma: Threads konkurrieren um Rechenaufgaben
         * Resultats-Parallelismus-Paradigma: Threads bekommen balancierte Last
         */

        // Kompatibilität der Matrizen prüfen
        if (a[0].length != b.length) {
            throw new RuntimeException("inkompatible Matrizen");
        }

        // anzahl zu verwendender Threads
        int anzahlThreads = getAnzahlThreadsFromConsole();

        // Aufgabenliste erstellen
        Stack<RechenAufgabe> aufgaben = new Stack<>();
        for (int y = 0; y < a[0].length; y++) {
            for (int x = 0; x < b.length; x++) {
                int[] zeileA = a[y];
                int[] spalteB = getSpalte(b, x);
                aufgaben.push(new RechenAufgabe(zeileA, spalteB, x, y));
            }
        }

//        int[][] ergebnis = masterWorker(a, b, anzahlThreads, aufgaben);
        int[][] ergebnis = resultatsParallelismus(a, b, anzahlThreads, aufgaben);
        System.out.println("---------------------------------------------");
        printMatrix(ergebnis);
    }

    /**
     * fragt Benutzer nach Anzahl Threads. Verträgt auch Unsinn
     * @return Anzahl Threads
     */
    private static int getAnzahlThreadsFromConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Anzahl Threads: ");
        int result = -1;
        for (boolean valid = false; !valid;) {
            String input = scanner.next();
            try {
                result = Integer.parseInt(input);
                valid = true;
            } catch (Exception e) {
                System.out.println(String.format("'%s' is not a valid number. Try again", input));
            }
        }
        scanner.close();
        return result;
    }

    /**
     * Abarbeitung der Rechenaufgaben nach dem Master Worker Paradigma - Threads
     * konkurrieren um Aufgaben
     * 
     * @param a Matrix A
     * @param b Matrix B
     * @param anzahlThreads
     * @param aufgaben
     * @return Matrix C = A * B
     */
    public static int[][] masterWorker(int[][] a, int[][] b, int anzahlThreads, Stack<RechenAufgabe> aufgaben) {

        int[][] ergebnis = new int[a[0].length][b.length];

        Thread[] threads = new WorkerThread[anzahlThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new WorkerThread(aufgaben, ergebnis, "Thread " + (i + 1));
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return ergebnis;
    }

    /**
     * Abarbeitung der Rechenaufgaben nach dem Resultats Parallelismus Paradigma -
     * jeder Thread erhält gleich viele Aufgaben (soweit möglich)
     * 
     * @param a Matrix A
     * @param b Matrix B
     * @param anzahlThreads
     * @param aufgaben
     * @return Matrix C = A * B
     */
    public static int[][] resultatsParallelismus(int[][] a, int[][] b, int anzahlThreads,
            Stack<RechenAufgabe> aufgaben) {
        
        int[][] ergebnis = new int[a[0].length][b.length];

        List<Stack<RechenAufgabe>> aufgabenlisten = new ArrayList<Stack<RechenAufgabe>>();

        for (int i = 0; i < anzahlThreads; i++) {
            aufgabenlisten.add(new Stack<>());
        }

        // Aufgaben gleichmäßig verteilen
        int aufgabenIndex = 0;
        for (RechenAufgabe aufgabe = aufgaben.pop(); true; aufgabe = aufgaben.pop()) {
            aufgabenlisten.get(aufgabenIndex).add(aufgabe);
            aufgabenIndex = (aufgabenIndex + 1) % aufgabenlisten.size();
            if (aufgaben.size() == 0) {
                break;
            }
        }

//        printAufgabenVerteilung(aufgabenlisten);

        // Threads erzeugen
        Thread[] threads = new WorkerThread[anzahlThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new WorkerThread(aufgabenlisten.get(i), ergebnis, "Thread " + (i + 1));
            threads[i].start();
        }

        // auf Threadende warten
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return ergebnis;
    }

    /**
     * liefert die Spalte einer Matrix mit dem übergebenen Index als Array
     * @param matrix
     * @param spaltenIndex
     * @return 
     */
    public static int[] getSpalte(int[][] matrix, int spaltenIndex) {
        int[] result = new int[matrix[0].length];
        for (int y = 0; y < matrix.length; y++) {
            result[y] = matrix[y][spaltenIndex];
        }
        return result;
    }
    
    /**
     * gibt die übergebene Matrix schön formatiert aus
     * @param matrix
     */
    public static void printMatrix(int[][] matrix) {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                System.out.print(String.format("%5d", matrix[y][x]) + " ");
            }
            System.out.println("");
        }
    }

    public static void printAufgabenVerteilung(List<Stack<RechenAufgabe>> aufgabenlisten) {
        System.out.println(Arrays.toString(aufgabenlisten.toArray()));
    }
}
