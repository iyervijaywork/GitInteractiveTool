import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class UtilsTest {
	@DataProvider
    public Object[][] apiUrlTestData() {

        return new Object[][]{
                {Constants.GitHubV3Mapping.get(Constants.SupportedModes.topNForks.toString().toLowerCase()).replace("%Num%", "2"), false},
                {Constants.GitHubV3Mapping.get(Constants.SupportedModes.topNStars.toString().toLowerCase()).replace("%Num%", "2"), false},
                {Constants.GitHubV3Mapping.get("getrepos"), false},
                {Constants.GitHubV3Mapping.get("rateLimit"), false}
        };
    }

    @Test(description = "Test the API execution engine with different url strings", dataProvider = "apiUrlTestData", enabled = true)
	public void executeGitHubV3ApiTest(String url, Boolean ignoreRate) {
 
    			JsonObject json = (JsonObject)Utils.executeGitHubV3Api(url, ignoreRate);
    			JsonArray jarray = (JsonArray) json.get("items");
    			Assert.assertTrue(jarray.size() > 0);
	}
}
