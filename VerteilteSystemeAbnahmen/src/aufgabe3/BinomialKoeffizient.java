package aufgabe3;

public class BinomialKoeffizient {
    private int n, k;

    public BinomialKoeffizient(int n, int k) {
        super();
        if(k > n) {
            throw new RuntimeException("k must not be greater than n");
        }
        this.n = n;
        this.k = k;
    }

    /**
     * parses a String of the form (n|k)
     * 
     * @param input
     * @return
     */
    public static BinomialKoeffizient parse(String input) throws Exception {
        if (!input.matches("\\([0-9]+\\|[0-9]+\\)")) {
            throw new Exception("input doesn't match (n|k) format. given input was: " + input);
        }
        String shavedInput = input.substring(1, input.length() - 1);
        String parts[] = shavedInput.split("\\|");
        int n = Integer.parseInt(parts[0]);
        int k = Integer.parseInt(parts[1]);

        return new BinomialKoeffizient(n, k);
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    @Override
    public String toString() {
        return "(" + this.getN() + "|" + this.getK() + ")";
    }
}
