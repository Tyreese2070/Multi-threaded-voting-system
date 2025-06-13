import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;

public class Server
{
	private ServerSocket server = null;
	private String[] votingOptions;
	private Map<String, Integer> voteResults;
	private static PrintWriter logWriter;

	public Server(String[] votingOptions) {
		this.votingOptions = votingOptions;
		this.voteResults = new HashMap<>();

		// Initialize voteResults with 0 votes for each option
		for (String option : votingOptions) {
			voteResults.put(option, 0);
		}

		// Listen on port 7777
		try{ 
			server = new ServerSocket(7777);
		}
		catch (IOException e){
			System.err.println("Error: could not connect to port 7777");
			System.exit(1);
		}
	}

	public void run() {
		
		// Create an empty log file
		try {
            logWriter = new PrintWriter("log.txt");
            logWriter.close();
        } 
		catch (FileNotFoundException e) {
            System.err.println("Error: could not create log file");
            System.exit(1);
        }
		
		ExecutorService executor = Executors.newFixedThreadPool(30);
		while (true){

			// accept client and run client handler
			try {
				Socket client = server.accept();
                executor.execute(new ClientHandler(client, votingOptions, voteResults));
			}
			catch (IOException e){
				System.err.println("Could not accept client");
				System.exit(1);
			}
		}
	}

	public static void main( String[] args )
	{
		if (args.length < 2){
			System.err.println("Error: you must input more than two or more options");
            System.exit(1);
		}
		Server server = new Server(args);
		server.run();
		
	}
}

