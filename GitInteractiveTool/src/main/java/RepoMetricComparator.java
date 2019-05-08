import java.util.Comparator;

public class RepoMetricComparator implements Comparator<RepoMetric>
{
    @Override
    public int compare(RepoMetric x, RepoMetric y)
    {
        if (x.getMetricCount() < y.getMetricCount())
        {
            return 1;
        }
        if (x.getMetricCount() > y.getMetricCount())
        {
            return -1;
        }
        return 0;
    }
}