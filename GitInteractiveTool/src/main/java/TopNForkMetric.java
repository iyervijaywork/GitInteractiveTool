import com.google.gson.JsonObject;

public class TopNForkMetric extends TopNRepoMetric {

	public TopNForkMetric(int n) throws Exception
	{
		this.setN(n);
		this.BuildTopNForkMetric();
	}
	private void BuildTopNForkMetric() throws Exception
	{
		// TODO: Optimize with paginated calls for n > 500
		JsonObject json = 
			(JsonObject) Utils.executeGitHubV3Api(
			Constants.GitHubV3Mapping.get(
				Constants.SupportedModes.topNForks.toString().toLowerCase()).replace("%Num%", Integer.toString(this.getN())), false);

		if (json == null) return;
		
		this.updateTopNRepoMetricFromJson(json, "forks_count");
	}
}
