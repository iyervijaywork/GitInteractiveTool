import com.google.gson.JsonObject;

public class TopNStarMetric extends TopNRepoMetric {

	public TopNStarMetric(int n) throws Exception
	{
		this.setN(n);
		this.BuildTopNStarMetric();
	}
	public void BuildTopNStarMetric() throws Exception
	{
		// TODO: Optimize with paginated calls for n > 500
		JsonObject json = 
			(JsonObject) Utils.executeGitHubV3Api(
			Constants.GitHubV3Mapping.get(
				Constants.SupportedModes.topNStars.toString().toLowerCase()).replace("%Num%", Integer.toString(this.getN())), false);

		if (json != null)
		this.updateTopNRepoMetricFromJson(json, "stargazers_count");
	}
}
