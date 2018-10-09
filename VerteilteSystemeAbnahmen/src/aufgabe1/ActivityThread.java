package aufgabe1;

import java.util.concurrent.Semaphore;

public class ActivityThread extends Thread {

	private int number;
	private Semaphore semaphoreIn[], semaphoreOut;

	public ActivityThread(int number) {
		super();
		this.number = number;
	}
	
	@Override
	public void run() {
		try {
			for(Semaphore in : this.semaphoreIn) {
				in.acquire();
			}
			System.out.println("Activity " + this.number + " done");
			semaphoreOut.release();
			semaphoreOut.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setSemaphoreIn(Semaphore... semaphoreIn) {
		this.semaphoreIn = semaphoreIn;
	}

	public Semaphore getSemaphoreOut() {
		return semaphoreOut;
	}

	public void setSemaphoreOut(Semaphore semaphoreOut) {
		this.semaphoreOut = semaphoreOut;
	}
}
