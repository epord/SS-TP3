package experiments;

import java.util.ArrayList;
import java.util.List;

public enum Operation {
    MIN, MAX, MEAN, STD, MEDIAN;
    public static List<Operation> getAll(){
        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(MIN);
        operations.add(MAX);
        operations.add(MEAN);
        operations.add(STD);
        operations.add(MEDIAN);
        return operations;
    }
}
