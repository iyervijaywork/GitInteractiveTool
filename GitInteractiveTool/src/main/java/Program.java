import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

// Main Program
public class Program {

	public static void main(String[] args) throws MalformedURLException, IOException, ParseException {
		
	    Options options = new Options();
	      options.addOption("u", "usage", false, "Shows usage information for this tool.")
	      	 .addOption("n", true, "(Compulsory option) Show results for these top n repos.")
	         .addOption("m", true, "(Compulsory option) Enter the operation (Supported operations topNStars, topNForks, topNPullRequest, topNContribution).")
	         .addOption("t", true, "(Optional option) OAuth2 token - to increase request count to 5000 request per hour.")
	         .addOption("s", "sleep-for-rate", false, "(Optional option)  Just sit silently till new API requests are available.")
	         .addOption("l", true, "(Optional option)  Set limit of how many repo to search to build the result. If this is not set then all the repos of github will be scanned.");

	  //Create a parser
	    CommandLineParser parser = new GnuParser();

	    //parse the options passed as command line arguments
	    CommandLine cmd = parser.parse( options, args);
	    
	    if(cmd.hasOption("u")) {
	    	  HelpFormatter formatter = new HelpFormatter();
	  	    formatter.printHelp("Giit", options);
	  	    System.exit(0);
	    }
	    int n = 0;
	    String mode = "";
	    Boolean sleepForRate = false;
	    String token = null;
	    int limit = Integer.MAX_VALUE;
	    //checks if option is present or not
	    if(cmd.hasOption("n")) {
	    		try {
	    			n = Integer.parseInt(cmd.getOptionValue("n"));
	    			if (n <= 0)
	    			{
	    				System.out.println("n should be greater than 0. Tool exiting...");
	    				System.exit(0);
	    			}
	    		}catch(NumberFormatException | NullPointerException nfe)
	    		{
	    			System.out.println("Invalid argument to option -n. Tool exiting...");
	    			System.exit(0);
	    		}
	    }
	    else {
	    		System.out.println("-n is a required option. Tool exiting...");
	    		System.exit(0);
	    }
	    if(cmd.hasOption("m")) {
	       mode = cmd.getOptionValue("m");
	       if (mode == null || mode.isEmpty()){
		    		System.out.println("Invalid mode. Tool exiting...");
		    		System.exit(0);
	       }
	    }
	    else {
	    		System.out.println("-m is a required option. Tool exiting...");
	    		System.exit(0);
	    }
	    if(cmd.hasOption("s")) {
		    sleepForRate = true;
		}
	    if(cmd.hasOption("t")) {
		    token = cmd.getOptionValue("t");
		}
	    if(cmd.hasOption("l")) {
		    	try {
			    limit = Integer.parseInt(cmd.getOptionValue("l"));
			}catch(NumberFormatException | NullPointerException nfe)
			{
				System.out.println("Invalid argument to option -n. Tool exiting...");
				System.exit(0);
			}
		}
	    ProgramGlobalParam globalParams = ProgramGlobalParam.getInstance(); 
	    globalParams.setSleepForRate(sleepForRate);
	    globalParams.setToken(token);
	    globalParams.setMaxRepoParseCount(limit);
	    
        List<RepoMetric> result = processRequest(n, mode);
        printResult(result, mode);
		
	}
	
	//print the topN results to the stdout
	private static void printResult(List<RepoMetric> result, String mode) {
		if (result == null || result.size() == 0)
			System.out.println("Failure : Unable to object the result set.");
		System.out.println("\nResult for " + mode);
		for(int i = 0; i < result.size(); i++)
		{
			System.out.println("\n" + (i+1) + ")");
			System.out.println("\tId: " + result.get(i).getRepoObject().getId());
			System.out.println("\tFull Name: " + result.get(i).getRepoObject().getFullName());
			System.out.println("\t" + mode.replace("topN", "") + " Count: " + result.get(i).getMetricCount());
		}
	}
	
	//process the topN request for the given mode
	public static List<RepoMetric> processRequest(int n, String mode){
		List<RepoMetric> result = null;
		try {
			switch(mode.toLowerCase())
			{
				case  "topnstars":
					TopNStarMetric topNStars = new TopNStarMetric(n);
					result = topNStars.getTopNRepo();
					break;
				case "topnforks":
					TopNForkMetric topNForks = new TopNForkMetric(n);
					result = topNForks.getTopNRepo();
					break;
				case "topnpullrequest":
					TopNPullRequestMetric topNPullRequest = new TopNPullRequestMetric(n);
					result = topNPullRequest.getTopNRepo();
					break;
				case "topncontribution":
					TopNContributionMetric topNContribution = new TopNContributionMetric(n);
					result = topNContribution.getTopNRepo();
					break;
				default:
					System.out.println("Error : Specified mode not supported");
			        System.out.println("Supported operations topNStars, topNForks, topNPullRequest, topNContribution");	
			        System.exit(0);
			}
		}catch(Exception e) {
			System.out.println("Encountered an exception when running the tool. Please look at the stacktrace and rerun. Please file a bug if needed.");
			e.printStackTrace();
		}
	    return result;
	}
}
