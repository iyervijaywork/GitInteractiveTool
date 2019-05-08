import java.util.HashMap;

public class Constants {

	//Mapping GitHub V3 API endpoint with different supported modes.
	public static final HashMap<String, String> GitHubV3Mapping = new HashMap<String, String>();
	  static {
	        GitHubV3Mapping.put("topnstars", "https://api.github.com/search/repositories?q=stars%3A%3E0&sort=stars&per_page=%Num%"); // Even if dataType is Integer, SQL Number range is higher than int range of Java.
	        GitHubV3Mapping.put("topnforks", "https://api.github.com/search/repositories?q=forks%3A%3E0&sort=forks&per_page=%Num%");
	        GitHubV3Mapping.put("getrepos", "https://api.github.com/repositories");
	        GitHubV3Mapping.put("getrepossince", "https://api.github.com/repositories?since=%id%");
	        GitHubV3Mapping.put("searchRepoByFullName", "https://api.github.com/search/repositories?q=%22%FullName%%22%20in:full_name");
	        GitHubV3Mapping.put("rateLimit", "https://api.github.com/rate_limit");
	    }
	  
	//Different supported mode
	public enum SupportedModes {
	    		topNStars, topNForks, topNPullRequest, topNContribution
	}
	
	//Maximum retry attempts for API rate checks.
	public static final int maxAttemptsForSleepForRate = 6;

}
