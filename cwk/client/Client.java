import java.io.*;
import java.net.*;
import java.util.*;

public class Client
{
	private PrintWriter output;
	private BufferedReader input;

	public void run(String command) {
		Socket client = null;
		try {
			client = new Socket("localhost", 7777);
			output = new PrintWriter(client.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));

			output.println(command);
			String response;
			while ((response = input.readLine()) != null) {
				System.out.println(response);
			}
		}
		catch (UnknownHostException e) {
			System.err.println("Unknown host");
			System.exit(1);
		}
		catch (IOException e) {
			System.err.println("Could not connect to server");
			System.exit(1);
		}
		finally {
			try {
				if (output != null) output.close();
				if (input != null) input.close();
				if (client != null) client.close();
			} catch (IOException e) {
				System.err.println("Error closing resources");
			}
		}
	}

	public static void main( String[] args )
	{
		if (args.length < 1  || args.length > 2) {
            System.err.println("Usage: java Client [list/vote <option>]");
            System.exit(1);
        }
		
        Client client = new Client();
        client.run(String.join(" ", args));
	}
}
