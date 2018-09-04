import experiments.ExperimentStatsHolder;
import experiments.ExperimentsStatsAgregator;
import experiments.Operation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import static experiments.ExperimentsStatsAgregator.*;

public class MetricsTest {
    @Test
    public void testMetrics(){
        ExperimentStatsHolder<GasMetrics> firstResults = new ExperimentStatsHolder<>();
        firstResults.addDataPoint(GasMetrics.DY_PRESSURE,1.0,0.0);
        firstResults.addDataPoint(GasMetrics.DY_PRESSURE,1.0,50.0);
        firstResults.addDataPoint(GasMetrics.DY_PRESSURE,1.0,-1.0);
        firstResults.addDataPoint(GasMetrics.EQ_TEMPERATURE,1.0,-1.0);

        ExperimentStatsHolder<GasMetrics> secondResults = new ExperimentStatsHolder<>();
        secondResults.addDataPoint(GasMetrics.DY_PRESSURE,1.0,0.0);
        secondResults.addDataPoint(GasMetrics.DY_PRESSURE,1.0,25.0);
        secondResults.addDataPoint(GasMetrics.DY_PRESSURE,1.0,1.0);
        secondResults.addDataPoint(GasMetrics.EQ_TEMPERATURE,1.0,1.0);

        ExperimentsStatsAgregator<GasMetrics> experimentsStatsAgregator = new ExperimentsStatsAgregator<>();
        experimentsStatsAgregator.addStatsHolder(firstResults);
        experimentsStatsAgregator.addStatsHolder(secondResults);

//        StringBuilder sb = experimentsStatsAgregator.buildStatsOutput(Operation.getAll());
        StringBuilder sb = experimentsStatsAgregator.buildStatsOutput(Arrays.asList(Operation.MEAN,Operation.STD));
        System.out.println(sb.toString());
    }

    @Test
    public void testStandardDeviation(){
        assertEquals(new Double(1.4142135623730951), getStandardDeviation(Arrays.asList(-1.0,1.0)));
        assertEquals(new Double(1.0), getStandardDeviation(Arrays.asList(0.0,1.0,2.0)));
    }
}
