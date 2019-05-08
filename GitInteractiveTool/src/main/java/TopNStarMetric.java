import com.google.gson.JsonObject;

//Class to capture and compute topN star metric
public class TopNStarMetric extends TopNRepoMetric {

	public TopNStarMetric(int n) throws Exception
	{
		this.setN(n);
		this.BuildTopNStarMetric();
	}
	public void BuildTopNStarMetric() throws Exception
	{
		JsonObject json = 
			(JsonObject) Utils.executeGitHubV3Api(
			Constants.GitHubV3Mapping.get(
				Constants.SupportedModes.topNStars.toString().toLowerCase()).replace("%Num%", Integer.toString(this.getN())), false);

		if (json != null)
		this.updateTopNRepoMetricFromJson(json, "stargazers_count");
	}
}
