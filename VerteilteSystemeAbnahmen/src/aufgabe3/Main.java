package aufgabe3;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter a binomial coefficient (n|k)");
        BinomialKoeffizient eingabe = readFromConsole();
        System.out.println("Valid input. Processing...");
        RechenThread thread = new RechenThread(eingabe);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println(eingabe.toString() + " = " + thread.getResult());
    }
    
    public static BinomialKoeffizient readFromConsole() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String input = scanner.next();
            try {
                BinomialKoeffizient result = BinomialKoeffizient.parse(input);
                scanner.close();
                return result;
            } catch(Exception e) {
                System.out.println("Invalid input: " + e.getMessage() + "\nTry again");
            }
        }
    }
}
