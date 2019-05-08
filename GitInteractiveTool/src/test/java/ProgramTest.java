import java.util.List;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class ProgramTest {

	@DataProvider
    public Object[][] processRequestTestData() {

        return new Object[][]{
                {2, Constants.SupportedModes.topNForks.toString()},
                {10, Constants.SupportedModes.topNForks.toString()},
                {2, Constants.SupportedModes.topNStars.toString()},
                {10, Constants.SupportedModes.topNStars.toString()},
                {4, Constants.SupportedModes.topNPullRequest.toString()},
                {2, Constants.SupportedModes.topNContribution.toString()}
        };
    }

    @Test(description = "Test the processRequest API for different n and mode values", dataProvider = "processRequestTestData", enabled = true)
    public void processRequestTest(int n, String mode) {
        ProgramGlobalParam globalParams = ProgramGlobalParam.getInstance(); 
        globalParams.setMaxRepoParseCount(5);  
        globalParams.setToken("ec6e3240c8c62a56947d4514f0c5f0edba5f8c5a");
        
    		List<RepoMetric> result = Program.processRequest(n, mode);
    		Assert.assertTrue(result.size() == n);
    		Assert.assertTrue(result.get(0).getMetricCount() >= result.get(1).getMetricCount());
    }
}
