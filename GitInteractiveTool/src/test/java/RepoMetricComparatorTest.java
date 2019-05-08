import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class RepoMetricComparatorTest {

    @Test(description = "Test the repo metric comparator a < b", enabled = true)
    public void compareTest1() {
	    RepoMetric repoMetric1 = new RepoMetric();
	    repoMetric1.setMetricCount(2);
	    RepoMetric repoMetric2 = new RepoMetric();
	    repoMetric2.setMetricCount(20);
	    
	    RepoMetricComparator comparator = new RepoMetricComparator();
	    int result = comparator.compare(repoMetric1, repoMetric2);
	    
	    Assert.assertTrue(result == 1);
	 }
    
    @Test(description = "Test the repo metric comparator a > b", enabled = true)
    public void compareTest2() {
	    RepoMetric repoMetric1 = new RepoMetric();
	    repoMetric1.setMetricCount(2000);
	    RepoMetric repoMetric2 = new RepoMetric();
	    repoMetric2.setMetricCount(20);
	    
	    RepoMetricComparator comparator = new RepoMetricComparator();
	    int result = comparator.compare(repoMetric1, repoMetric2);
	    
	    Assert.assertTrue(result == -1);
	 }
}
