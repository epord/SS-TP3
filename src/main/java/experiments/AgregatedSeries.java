package experiments;

import java.util.List;

public class AgregatedSeries<K> {
    private List<Stats> seriesData;
    private K series;

    public AgregatedSeries(List<Stats> seriesData, K series) {
        this.seriesData = seriesData;
        this.series = series;
    }

    public Integer size(){
        return seriesData.size();
    }

    public StringBuilder addHeaders(StringBuilder sb, List<Operation> operations){
        operations.stream().forEachOrdered( op -> {
            sb.append(series + " " + op + ",");
        });
        return sb;
    }
    public StringBuilder addStats(StringBuilder sb, List<Operation> operations, Integer index){
        if(index < 0 || index > seriesData.size()){
            throw new IllegalStateException("cant add stats, missing data");
        }
        operations.stream().forEachOrdered( op -> sb.append(seriesData.get(index).getValueByOperation(op))
                                                    .append(","));
        return sb;
    }
}
