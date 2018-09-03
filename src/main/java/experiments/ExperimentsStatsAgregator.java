package experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExperimentsStatsAgregator<K extends Enum> {
    List<ExperimentStatsHolder<K>> holders;
    List<Function<StringBuilder,StringBuilder>> builder = new ArrayList<>();


    public ExperimentsStatsAgregator() {
        holders = new ArrayList<>();
    }

    public void addStatsHolder(ExperimentStatsHolder<K> experimentStatsHolder){
        holders.add(experimentStatsHolder);
    }

    public StringBuilder buildStatsOutput(List<Operation> operations){
        StringBuilder stringBuilder = new StringBuilder();
        Set<K> activeSeries = holders.stream().findAny().get().getActiveSeries();
        
        List<AgregatedSeries<K>> timeseriesStats = activeSeries.stream().map(serie ->
            new AgregatedSeries<>(fillStats(holders.stream()
                    .map(holder -> holder.getDataSeries(serie))
                    .collect(Collectors.toList())),serie)
        ).collect(Collectors.toList());
        
        timeseriesStats.stream().forEachOrdered( agregatedSeries -> agregatedSeries.addHeaders(stringBuilder,operations));
        stringBuilder.append("\n");
        if(timeseriesStats.size() > 0){
            for (int i = 0; i < timeseriesStats.get(0).size(); i++) {
                for (AgregatedSeries<K> agregatedSeries : timeseriesStats) {
                    agregatedSeries.addStats(stringBuilder, operations, i);
                }
                stringBuilder.append("\n");
            }
        }
        return stringBuilder;
    }

    public static Double getStandardDeviation(List<Double> colors, Double mean) {
        Double standardDeviation = colors.stream()
                .mapToDouble(x -> Math.pow(x - mean, 2))
                .sum();

        return Math.sqrt(standardDeviation / colors.size());
    }

    public List<Stats> fillStats(List<List<DataPoint>> timeseries){
        List<Stats> statsList = new ArrayList<>();
        Integer minLenght = getMinLenght(timeseries);
        for (int i = 0; i < minLenght; i++) {
            DataPoint min = new DataPoint(-1.0,Double.MAX_VALUE);
            DataPoint max = new DataPoint(-1.0,-Double.MAX_VALUE);
            Double mean = 0.0;
            Double std;
            DataPoint median = null;
            List<Double> values = new ArrayList<>();
            for (int j = 0; j < timeseries.size(); j++) {
                DataPoint analized = timeseries.get(j).get(i);
                min = min.getValue() < analized.getValue()?min:analized;
                max = max.getValue() > analized.getValue()?max:analized;
                mean += analized.getValue()/timeseries.size();
                values.add(analized.getValue());
                if(j == timeseries.size()/2){
                    median = analized;
                }
            }
            std = getStandardDeviation(values,mean);
            Stats stats = new Stats(min,max,std,mean,median);
            statsList.add(stats);
        }
        return statsList;
    }

    public Integer getMinLenght(List<List<DataPoint>> timeseries){
        Integer min = Integer.MAX_VALUE;
        for(List<DataPoint> timeserie: timeseries){
            min = min < timeserie.size()?min:timeserie.size();
        }
        return min;
    }
}
