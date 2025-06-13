import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket client = null;
    private String input;
    private PrintWriter out;
    private BufferedReader in;
    private String[] votingOptions;
    private Map<String, Integer> voteResults;

    public ClientHandler(Socket client, String[] votingOptions, Map<String, Integer> voteResults) {
        this.client = client;
        this.votingOptions = votingOptions;
        this.voteResults = voteResults;
    }

    @Override
    public void run() {
        try {
            // Get input and output streams
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // Read input from client
            while ((input = in.readLine()) != null) {
                String[] parsedInput = input.split(" ");

                // Output the number of votes for each candidate when input is list
                if (parsedInput[0].equalsIgnoreCase("list")){
                    synchronized(voteResults){
                        if (parsedInput.length > 1){
                            out.println("List Usage: java Client list");
                            client.close();
                            break;
                        }

                        for (String option : votingOptions) {
                            out.println(option + " has " + voteResults.get(option) + " votes" );
                    }}
                }

                // Increment the votes for the given option when input it vote <option>
                else if(parsedInput[0].equalsIgnoreCase("vote")){
                    synchronized(voteResults){
                        if (voteResults.containsKey(parsedInput[1])){
                            voteResults.put(parsedInput[1], voteResults.get(parsedInput[1]) + 1);
                            out.println("Incremented the number of votes for " + parsedInput[1]);
                        }
                        else {
                            out.println("Cannot find option: " + parsedInput[1]);
                    }}
                }
                else {
                    out.println("Invalid command");
                }

                // Get data for log file, and output it to log.txt
                try (PrintWriter logWriter = new PrintWriter(new FileWriter("log.txt", true))) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);

                    String date = day + "-" + month + "-" + year;
                    String time = hour + ":" + minute + ":" + second;

                    String logEntry = String.format("%s|%s|%s|%s", date, time, client.getInetAddress(), input);
                    logWriter.println(logEntry);

                    logWriter.close();
                } catch (IOException e) {
                    // System.err.println("Log error:");
                }

                client.close();
            }
        }
        catch (IOException e) {
            //System.err.println(e);
        }
    }
}