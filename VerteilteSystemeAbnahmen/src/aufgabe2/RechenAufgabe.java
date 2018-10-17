package aufgabe2;

import java.util.Arrays;

public class RechenAufgabe {
    /**
     * Zeile und Spalte der zu multiplizierenden Matrizen
     */
    private int[] zeile, spalte;

    /**
     * Position des Ergebnisses in der Ergebnismatrix C
     */
    private int x, y;

    public RechenAufgabe(int[] zeile, int[] spalte, int x, int y) {
        super();
        this.zeile = zeile;
        this.spalte = spalte;
        this.x = x;
        this.y = y;
    }

    public int loesen() {
        int result = 0;
        for (int i = 0; i < zeile.length; i++) {
            result += zeile[i] * spalte[i];
        }
        return result;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return Arrays.toString(zeile) + " * " + Arrays.toString(spalte);
    }
}
