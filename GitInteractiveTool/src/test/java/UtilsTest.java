import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class UtilsTest {
	
	@BeforeTest
    public void beforeTest() {
        System.out.println("Program Tests Start!");
        ProgramGlobalParam globalParams = ProgramGlobalParam.getInstance(); 
        globalParams.setMaxRepoParseCount(5);  
        globalParams.setToken("3becb36126b3b09a5ab106eec426f9ab22b26052");
    }
	
	@DataProvider
    public Object[][] topNForksTestData() {

        return new Object[][]{
                {Constants.GitHubV3Mapping.get(Constants.SupportedModes.topNForks.toString().toLowerCase()).replace("%Num%", "2"), 2},
                {Constants.GitHubV3Mapping.get(Constants.SupportedModes.topNForks.toString().toLowerCase()).replace("%Num%", "4"), 4},
                {Constants.GitHubV3Mapping.get(Constants.SupportedModes.topNForks.toString().toLowerCase()).replace("%Num%", "10"), 10}

        };
    }

    @Test(description = "Test the API execution engine with TopNForks url strings", dataProvider = "topNForksTestData", enabled = true)
	public void topNForksTest(String url, int count) {
		try {  
    			JsonObject json = (JsonObject)Utils.executeGitHubV3Api(url, false);
    			JsonArray jarray = (JsonArray) json.get("items");
    			Assert.assertTrue(jarray.size() == count);
		}catch(Exception e)
		{
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
    
	@DataProvider
    public Object[][] topNStarsTestData() {

        return new Object[][]{
                {Constants.GitHubV3Mapping.get(Constants.SupportedModes.topNStars.toString().toLowerCase()).replace("%Num%", "2"), 2},
                {Constants.GitHubV3Mapping.get(Constants.SupportedModes.topNStars.toString().toLowerCase()).replace("%Num%", "4"), 4},
                {Constants.GitHubV3Mapping.get(Constants.SupportedModes.topNStars.toString().toLowerCase()).replace("%Num%", "10"), 10}

        };
    }

    @Test(description = "Test the API execution engine with topNStars url string", dataProvider = "topNStarsTestData", enabled = true)
	public void topNStarsTest(String url, int count) {
		try {  
    			JsonObject json = (JsonObject)Utils.executeGitHubV3Api(url, false);
    			JsonArray jarray = (JsonArray) json.get("items");
    			Assert.assertTrue(jarray.size() == count);
		}catch(Exception e)
		{
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
}
