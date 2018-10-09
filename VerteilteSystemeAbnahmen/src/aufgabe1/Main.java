package aufgabe1;

import java.util.concurrent.Semaphore;

public class Main {

	public static void main(String[] args) {
		ActivityThread[] threads = new ActivityThread[7];
//		for (int i = 6; i >= 0; i--) {
		for (int i = 0; i < 7; i++) {
			threads[i] = new ActivityThread(i + 1);
		}

		// 1er Semaphor, damit a1 startet
		Semaphore a1in = new Semaphore(1);

		// a1out = a2in = a3in
		Semaphore a1out = new Semaphore(0);

		// a2out = a4in = a5in1
		Semaphore a2out = new Semaphore(0);

		// a3out = a5in2 = a6in
		Semaphore a3out = new Semaphore(0);

		// a4out = a7in1
		Semaphore a4out = new Semaphore(0);

		// a5out = a7in2
		Semaphore a5out = new Semaphore(0);

		// a6out = a7in3
		Semaphore a6out = new Semaphore(0);

		// dummy-Semaphore für a7
		Semaphore a7out = new Semaphore(0);

		// Semaphore setzen
		// a1
		threads[0].setSemaphoreIn(a1in);
		threads[0].setSemaphoreOut(a1out);

		// a2
		threads[1].setSemaphoreIn(a1out);
		threads[1].setSemaphoreOut(a2out);

		// a3
		threads[2].setSemaphoreIn(a1out);
		threads[2].setSemaphoreOut(a3out);

		// a4
		threads[3].setSemaphoreIn(a2out);
		threads[3].setSemaphoreOut(a4out);

		// a5
		threads[4].setSemaphoreIn(a2out, a3out);
		threads[4].setSemaphoreOut(a5out);

		// a6
		threads[5].setSemaphoreIn(a3out);
		threads[5].setSemaphoreOut(a6out);

		// a7
		threads[6].setSemaphoreIn(a4out, a5out, a6out);
		threads[6].setSemaphoreOut(a7out);

		for (Thread thread : threads) {
			thread.start();
		}
	}

}
