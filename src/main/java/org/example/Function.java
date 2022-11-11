package org.example;

import java.util.HashMap;
import java.util.List;
/**
 * Interface class with mathematics functions
 */
public interface Function {

    int apply(List<Integer> args);

    public static HashMap<String, Function> getFunctionMap() {
        HashMap<String, Function> functionTable = new HashMap<>();
        functionTable.put("min", args -> {
            if (args.isEmpty()) {
                throw new RuntimeException("No arguments for function min");
            }
            int min = args.get(0);
            for (Integer val : args) {
                if (val < min) {
                    min = val;
                }
            }
            return min;
        });
        functionTable.put("pow", args -> {
            if (args.size() != 2) {
                throw new RuntimeException("Wrong argument count for function pow: " + args.size());
            }
            return (int) Math.pow(args.get(0), args.get(1));
        });
        functionTable.put("avg", args -> {
            int sum = 0;
            for (int i = 0; i < args.size(); i++) {
                sum += args.get(i);
            }
            return sum / args.size();
        });
        functionTable.put("sqrt", args -> {
            if (args.size() != 1) {
                throw new RuntimeException("Wrong argument count for function sqrt: " + args.size());
            }
            return (int) Math.sqrt(args.get(0));
        });
        functionTable.put("abs", args -> {
            if (args.size() != 1) {
                throw new RuntimeException("Wrong argument count for function abs: " + args.size());
            }
            return (int) Math.abs(args.get(0));
        });

        return functionTable;
    }
}
