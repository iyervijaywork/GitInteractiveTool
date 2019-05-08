import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

//Base class to capture and compute topN github repository metric
public class TopNRepoMetric {
	private List<RepoMetric> topNRepo;
	private int n;
	public List<RepoMetric> getTopNRepo() {
		return topNRepo;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	
	public void updateTopNRepoMetricFromJson(JsonObject json, String metricField) throws Exception
	{
		topNRepo = parseJsonObject(json, metricField);
		
	}
	
	public List<RepoMetric> parseJsonObject(JsonObject json, String metricField) throws Exception
	{
		JsonArray items = (JsonArray) json.get("items");
		return parseJsonArray(items, metricField, items.size());
	}
	
	public List<RepoMetric> parseJsonArray(JsonArray jsonArray, String metricField, int size) throws Exception
	{
		RepoMetric repoMetric;
		RepoObjectModel repoOM;
		List<RepoMetric> list = new ArrayList<>();
		for(int i = 0; i < size; i++)
		{
			JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
			
			//check if repo is public otherwise you will not be able to get the pull_request counts
			if (!jsonObject.get("private").getAsBoolean())
			{
				repoMetric = new RepoMetric();
				repoOM = new RepoObjectModel();
				repoOM.setId(jsonObject.get("id").getAsLong());
				repoOM.setFullName(jsonObject.get("full_name").getAsString());
				repoMetric.setRepoObject(repoOM);
				
				double metricCount = processMetricCount(jsonObject, metricField, repoOM.getFullName());
				if (metricCount != -1)
				{
					repoMetric.setMetricCount(metricCount);
				}
				
				list.add(repoMetric);
			}
		}
		return list;
	}
	
	public double processMetricCount(JsonObject jsonObject, String metricField, String fullName) throws Exception
	{
		return jsonObject.get(metricField).getAsLong();
	}
	
	private void buildTopNRepoMetric(List<RepoMetric> data)
	{
		Comparator<RepoMetric> comparator = new RepoMetricComparator();
		PriorityQueue<RepoMetric> queue = new PriorityQueue<RepoMetric>(this.n, comparator);
		for (RepoMetric temp : data)
		{
			queue.add(temp);
		}
		if (this.topNRepo != null)
		{
			for (RepoMetric temp : this.topNRepo)
			{
				queue.add(temp);
			}
		}
		
		//get top n elements from the heap
		List<RepoMetric> result = new ArrayList<>();
		int count = 0;
		while(queue.size() > 0 && count < this.n)
		{
			result.add(queue.poll());
			count++;
		}
		
		topNRepo = result;
	}
	
	public void manageTopNPullRequestMetric(String metricType) throws Exception
	{
		ProgramGlobalParam globalParam = ProgramGlobalParam.getInstance(); 
		
		List<RepoMetric> data = manageTopNPullRequestMetricHelper(metricType, Constants.GitHubV3Mapping.get("getrepos"));

		while (data != null && data.size() > 0 && globalParam.getMaxRepoParseCount() > 0 )
		{
			data = manageTopNPullRequestMetricHelper(metricType, Constants.GitHubV3Mapping.get("getrepossince").replace(
					"%id%", Long.toString(data.get(data.size() -1).getRepoObject().getId())));
		}
	}
	public List<RepoMetric> manageTopNPullRequestMetricHelper(String metricType, String Url) throws Exception
	{
		ProgramGlobalParam globalParam = ProgramGlobalParam.getInstance(); 
		JsonArray jsonArray = 
			(JsonArray) Utils.executeGitHubV3Api(Url, false);

		if (jsonArray == null) return null;
		
		List<RepoMetric> data = null;
		if (jsonArray.size() > globalParam.getMaxRepoParseCount())
		{
			data = this.parseJsonArray(jsonArray, metricType, globalParam.getMaxRepoParseCount());
			globalParam.setMaxRepoParseCount(0);
		}
		else
		{
			data = this.parseJsonArray(jsonArray, metricType, jsonArray.size());
			int temp = globalParam.getMaxRepoParseCount();
			globalParam.setMaxRepoParseCount(temp - jsonArray.size());
		}
		this.buildTopNRepoMetric(data);
		return data;
	}
	
}
