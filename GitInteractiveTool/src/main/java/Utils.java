import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {

	//executeGitHubV3Api executes the Git hub V3 API call and tracks rate limit.
	//if sleep for rate is enabled then this method will poll and resume when new rate becomes available
	public static Object executeGitHubV3Api(String url, Boolean ignoreRate) throws IOException, InterruptedException
	{
		HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
		ProgramGlobalParam s = ProgramGlobalParam.getInstance(); 
		
		if (s.getRate() <= 0 && !ignoreRate)
		{
			int count = Constants.maxAttemptsForSleepForRate;
			s.setRate(ParseRate((JsonObject)executeGitHubV3Api(Constants.GitHubV3Mapping.get("rateLimit"), true)));
			while(count > 0 && s.getRate() == 0)
			{
				count--;
				if (s.getSleepForRate())
				{
					System.out.println("Rate limit exceeded. Sleeping 10 minutes... (retry - " + (Constants.maxAttemptsForSleepForRate - count) + ")");
					Thread.sleep(600000);
				}
				s.setRate(ParseRate((JsonObject)executeGitHubV3Api(Constants.GitHubV3Mapping.get("rateLimit"), true)));
			}
			if (s.getRate() == 0) 
			{
				System.out.println("Retry limit exhausted. Tool exiting...");
				return null;
			}
			else
			{
				System.out.println("Rate limit is : " + s.getRate());
				System.out.println("Continuing from where we left off...");
			}
		}
		
		// Decrement rate by 1 every time git hub api endpoints are called
		if (!ignoreRate) s.DecrementRate();
			
		//Dummy user-agent since github v3 request mandates user-agent in request
		httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");
		
		//Check if user passed authorization token. This will increase rate limit per hour to 5000 requests
		if (s.getToken() != null)
		{
			httpcon.addRequestProperty("Authorization", "token " + s.getToken());
		}
		
		// Only parse http response when response code is 200
		if (httpcon.getResponseCode() == 200 )
		return convertJsonStreamToObject(new InputStreamReader(httpcon.getInputStream()));
		
		return null;
	}
    private static int ParseRate(JsonObject json) {
		if (json == null) return 0;
		return ((JsonObject) json.get("rate")).get("remaining").getAsInt();
	}
	public static Object convertJsonStreamToObject(InputStreamReader input) throws IOException {
        BufferedReader br = new BufferedReader(input);
        String line;
        StringBuilder sb = new StringBuilder();
        JsonParser parser = new JsonParser();

        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }
        br.close();
        Object jsonFileTemplate = parser.parse(sb.toString());
        return jsonFileTemplate;
    }
}
