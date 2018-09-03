package experiments;

public class Stats {
    private DataPoint min;
    private DataPoint max;
    private Double std;
    private Double mean;
    private DataPoint median;

    public DataPoint getMax() {
        return max;
    }

    public Double getStd() {
        return std;
    }

    public Double getMean() {
        return mean;
    }

    public DataPoint getMedian() {
        return median;
    }

    public DataPoint getMin() {
        return min;

    }

    public Double getValueByOperation(Operation operation){
        switch (operation){
            case MIN:
                return getMin().getValue();
            case MAX:
                return getMax().getValue();
            case MEAN:
                return getMean();
            case STD:
                return getStd();
            case MEDIAN:
                return getMedian().getValue();
        }
        throw new IllegalStateException("No operation found");
    }

    public Stats(DataPoint min, DataPoint max, Double std, Double mean, DataPoint median) {
        this.min = min;
        this.max = max;
        this.std = std;
        this.mean = mean;
        this.median = median;
    }
}
