import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TopNContributionMetric extends TopNRepoMetric {
	ProgramGlobalParam s = ProgramGlobalParam.getInstance(); 
	public TopNContributionMetric(int n) throws Exception
	{
		this.setN(n);
		this.manageTopNPullRequestMetric("contributions");
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
		
		JsonObject repoForks = 
				(JsonObject) Utils.executeGitHubV3Api(Constants.GitHubV3Mapping.get("searchRepoByFullName").replaceAll("%FullName%", fullName), false);
		
		if (repoForks == null) return 0;
		
		JsonArray forks = (JsonArray)repoForks.get("items").getAsJsonArray();
		if (forks.size() == 0) return 0;
		double forkCount = forks.get(0).getAsJsonObject().get("forks_count").getAsLong();
		
		if (pullCount == 0 || forkCount == 0 ) return 0;
		return ((double)pullCount/forkCount);
	}
}
