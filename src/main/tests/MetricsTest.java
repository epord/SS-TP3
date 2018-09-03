import experiments.ExperimentStatsHolder;
import experiments.ExperimentsStatsAgregator;
import experiments.Operation;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.lang.annotation.Target;

public class MetricsTest {
    @Test
    public void testMetrics(){
        ExperimentStatsHolder<GasMetrics> firstResults = new ExperimentStatsHolder<>();
        firstResults.addDataPoint(GasMetrics.PRESSURE,1.0,0.0);
        firstResults.addDataPoint(GasMetrics.PRESSURE,1.0,50.0);
        firstResults.addDataPoint(GasMetrics.PRESSURE,1.0,-50.0);

        ExperimentStatsHolder<GasMetrics> secondResults = new ExperimentStatsHolder<>();
        secondResults.addDataPoint(GasMetrics.PRESSURE,1.0,0.0);
        secondResults.addDataPoint(GasMetrics.PRESSURE,1.0,25.0);
        secondResults.addDataPoint(GasMetrics.PRESSURE,1.0,50.0);

        ExperimentsStatsAgregator<GasMetrics> experimentsStatsAgregator = new ExperimentsStatsAgregator<>();
        experimentsStatsAgregator.addStatsHolder(firstResults);
        experimentsStatsAgregator.addStatsHolder(secondResults);

        StringBuilder sb = experimentsStatsAgregator.buildStatsOutput(Operation.getAll());
        System.out.println(sb.toString());
    }
}
