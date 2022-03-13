import java.util.concurrent.atomic.AtomicBoolean;

public class SlowCalculator implements Runnable {

    private final long N;
	private Thread thread;
	private int result;
	private AtomicBoolean running = new AtomicBoolean( false );

    public SlowCalculator(final long N) {
        this.N = N;
		thread = new Thread( this );
    }
	/**
	 * Thread control methods
	 */
	public void start() {
		running.set( true );
		thread.start();
// System.out.println( N + " started" );
	}

	public void run() {
		result = calculate(N, running);
		running.set( false );
// System.out.println( N + " finished" );
	}

	public boolean running() {
		return running.get();
	}

	public void join() {
		try {
			thread.join();
		} catch( InterruptedException e ) { e.printStackTrace(); }
	}

	public void kill() {
		running.set( false );
// System.out.println( N + " terminated" );
	}

	/**
	 * Calculation methods
	 */
	public int getResult() {
		return result;
	}

    private static int calculate(final long N, AtomicBoolean running) {
        // This (very inefficiently) finds and returns the number of unique prime factors of |N|
        // You don't need to think about the mathematical details; what's important is that it does some slow calculation taking N as input
        // You should not modify the calculation performed by this class, but you may want to add support for interruption
        int count = 0;
        for (long candidate = 2; candidate < N && running.get(); ++candidate) {
            if (isPrime(candidate)) {
                if (Math.abs(N) % candidate == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean isPrime(final long n) {
        // This (very inefficiently) checks whether n is prime
        // You should not modify this method
        for (long candidate = 2; candidate < Math.sqrt(n); ++candidate) {
            if (n % candidate == 0) {
                return false;
            }
        }
        return true;
    }
}