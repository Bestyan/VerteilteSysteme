package aufgabe3;

public class RechenThread extends Thread {
    private BinomialKoeffizient koeffizient;
    private int result;

    public RechenThread(BinomialKoeffizient koeffizient) {
        super();
        this.koeffizient = koeffizient;
    }

    public BinomialKoeffizient getKoeffizient() {
        return koeffizient;
    }

    public void setKoeffizient(BinomialKoeffizient koeffizient) {
        this.koeffizient = koeffizient;
    }

    @Override
    public void run() {
        int n = koeffizient.getN();
        int k = koeffizient.getK();
        if(k > n - k) {
            k = n - k;
            System.out.println("solving " + koeffizient.toString() + " = " + "(" + n + "|" + k + ")" + "\t\t{optimized (n|k) to (n|n-k)}");
            koeffizient.setK(k);
        }
        
        if (n == k || k == 0) {

            // (n|n) oder (n|0) = 1
            System.out.println("solving " + koeffizient.toString() + " = 1 \t\t{(n|n) = 1 or (n|0) = 1 detected}");
            result = 1;
            return;

        } else if (k == 1) {

            // (n|1) = n
            System.out.println("solving " + koeffizient.toString() + " = " + n + " \t\t{(n|1) = n detected}");
            result = koeffizient.getN();
            return;

        } else {
            
            // (n|k) = (n-1|k-1) + (n-1|k)
            BinomialKoeffizient koeffizient1 = new BinomialKoeffizient(n - 1, k - 1);
            BinomialKoeffizient koeffizient2 = new BinomialKoeffizient(n - 1, k);
            System.out.println("solving " + koeffizient.toString() + " = " + koeffizient1.toString() + " + " + koeffizient2.toString());
            RechenThread thread1 = new RechenThread(koeffizient1);
            RechenThread thread2 = new RechenThread(koeffizient2);
            thread1.start();
            thread2.start();
            
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            result = thread1.getResult() + thread2.getResult();
        }

    }

    public int getResult() {
        return result;
    }
}
