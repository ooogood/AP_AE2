import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Solution {
	Scanner sc = new Scanner( System.in );
	boolean bContinue = true;

	// < N, corresponding threadObj >
	Map<Long, SlowCalculator> threads = new HashMap<>();

	// prompt user for a single commamd
	public void prompt() {
		// show prompt message
		System.out.println( "Enter a command" );
		String cmdline = sc.nextLine();
		// seperate cmd line for tokens
		if( !cmdDispatcher( cmdline.split( " " ) ) ) {
			System.out.println( "Invalid command" );
		}
	}

	// redirect the command to proper function
	// return false if the input is invalid
	private boolean cmdDispatcher( String[] cmds ) {
		switch( cmds[ 0 ] ) {
		case "start":
			try {
				long arg = Long.parseLong( cmds[ 1 ] );
				// this arg shouldn't exist
				if( argExist( arg ) ) return false;
				start( arg );
			} catch( NumberFormatException e ) { return false; }
			return true;

		case "cancel":
			try {
				long arg = Long.parseLong( cmds[ 1 ] );
				// this arg should exist
				if( !argExist( arg ) ) return false;
				cancel( arg );
			} catch( NumberFormatException e ) { return false; }
			return true;

		case "running":
			running();
			return true;

		case "get":
			try {
				long arg = Long.parseLong( cmds[ 1 ] );
				// this arg should exist
				if( !argExist( arg ) ) return false;
				get( arg );
			} catch( NumberFormatException e ) { return false; }
			return true;

		case "exit":
			exit();
			return true;

		case "abort":
			abort();
			return true;
		}
		return false;
	}

	// check if the arg is already in the map
	private boolean argExist( long arg ) {
		return threads.containsKey( arg );
	}

	// start calculating with input N, on a new thread
	private void start( long arg ) {
		// create thread, store it into a map and run it
		SlowCalculator slow = new SlowCalculator( arg );
		threads.put( arg, slow );
		slow.start();
	}

	// cancel the calculation with input N that is currently running (do nothing if it already completed)
	private void cancel( long arg ) {
		threads.get( arg ).kill();
	}

	// print the total number of calculations now running, and their inputs N (in any order)
	private void running() {
		// record all running threads
		Set<Long> runnings = new HashSet<>();
		for( var entry : threads.entrySet() ) {
			if( entry.getValue().running() )
				runnings.add( entry.getKey() );
		}
		// print out running message
		System.out.print( runnings.size() + " calculations running:" );
		for( Long l : runnings ) {
			System.out.print( " " + l );
		}
		System.out.println();
	}

	// if calculation for N is finished, print result is M where M is the integer result;
	// if calculation is not yet finished, print calculating
	private void get( long arg ) {
		SlowCalculator slow = threads.get( arg );
		if( slow.running() ) {
			System.out.println( "calculating" );
		} else {
			System.out.println( "result is " + slow.getResult() );
		}
	}

	// finish running all calculations previously requested by the user, then exit
	private void exit() {
		for( var value : threads.values() ) {
			value.join();
		}
		bContinue = false;
	}

	// exit immediately, without finishing any running calculations
	private void abort() {
		for( var value : threads.values() ) {
			value.kill();
		}
		bContinue = false;
	}

	public static void main( String args[] ) {
		Solution sl = new Solution();
		while( sl.bContinue ) {
			sl.prompt();
		}
	}
}
