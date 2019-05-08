import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

//Class to capture and compute topN pullrequest metric
public class TopNPullRequestMetric extends TopNRepoMetric {

	ProgramGlobalParam s = ProgramGlobalParam.getInstance(); 
	public TopNPullRequestMetric(int n) throws Exception
	{
		this.setN(n);
		this.manageTopNPullRequestMetric("pull_request");
	}
	
	public double processMetricCount(JsonObject jsonObject, String metricField, String fullName) throws Exception
	{
		if (s.getRate() == 0) return -1;
		
		// Traverse all the pull request result pages till we get empty result
		int pageCount = 1;
		double pullCount = 0;
		while(true)
		{
			JsonArray pulls = 
					(JsonArray)Utils.executeGitHubV3Api(jsonObject.get("pulls_url").getAsString().replace("{/number}", "?state=all&page=" +pageCount), false);
			if (pulls == null || pulls.size() == 0) break;
			pullCount += pulls.size();
			pageCount++;
		}
		return pullCount;
	}
}
