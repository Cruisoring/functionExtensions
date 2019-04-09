package io.github.cruisoring.logger;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple7;
import io.github.cruisoring.utility.StackTraceHelper;
import io.github.cruisoring.utility.StringHelper;

import java.util.*;
import java.util.stream.Collectors;

public class Measurement {
    static final String getCallerStackTraceKey = Measurement.class.getSimpleName();

    static final Map<String, List<Tuple>> namedMeasurements = new HashMap<>();

    public static Moment start(String format, Object... args) {
        return new Moment(format, args);
    }

    public static Moment start() {
        return new Moment();
    }

    public static void save(String label, Tuple details) {
        if (!namedMeasurements.containsKey(label)) {
            namedMeasurements.put(label, new ArrayList<>(Arrays.asList(details)));
        } else {
            namedMeasurements.get(label).add(details);
        }
    }

    public static Set<String> getMeasuredLabels() {
        return namedMeasurements.keySet();
    }

    public static List<Tuple> getMeasurements(String label) {
        if (!namedMeasurements.containsKey(label))
            return null;

        return Collections.unmodifiableList(namedMeasurements.get(label));
    }

    public static Tuple7<String, Long, Long, Long, Long, Long, Double> summaryOf(String label) {
        List<Tuple> tuples = getMeasurements(label);

        long total = 0;
        List<Long> elapsedList = new ArrayList<>();
        double std;
        for (Tuple tuple : tuples) {
            Long elapsed = (Long) tuple.getValue(0);
            total += elapsed;
            elapsedList.add(elapsed);
        }
        Collections.sort(elapsedList);
        int size = tuples.size();
        long mean = total / size;
        long min = elapsedList.get(0);
        long max = elapsedList.get(size - 1);
        long median = elapsedList.get(size / 2);

        double summation = 0;
        long dif;
        for (int i = 0; i < size; i++) {
            dif = elapsedList.get(i) - mean;
            summation += dif * dif;
        }
        Double standardDeviation = Math.sqrt(summation / size);
        String summary = String.format("%s: <mean=%d, median=%d, total=%d, min=%d, max=%d, std=%.2f%%>",
                label, mean, median, total, min, max, standardDeviation);
        return Tuple.create(summary, mean, median, total, min, max, standardDeviation);
    }

    public static Map<String, Tuple7<String, Long, Long, Long, Long, Long, Double>> getAllSummary() {
        Set<String> labels = getMeasuredLabels();
        Map<String, Tuple7<String, Long, Long, Long, Long, Long, Double>> all = labels.stream()
                .collect(Collectors.toMap(
                        label -> label,
                        label -> summaryOf(label)
                ));
        return all;
    }

    public static class Moment {
        final String label;
        final long createdAt;

        Moment() {
            StackTraceElement stack = StackTraceHelper.getCallerStackTrace(null, getCallerStackTraceKey);
            label = StringHelper.tryFormatString("%s(%s:%d)",
                    stack.getMethodName(), stack.getFileName(), stack.getLineNumber());

            //Ensure this is the last step to initialize the Moment instance
            createdAt = System.currentTimeMillis();
        }

        Moment(String format, Object... args) {
            Objects.requireNonNull(format);
            label = StringHelper.tryFormatString(format, args);

            //Ensure this is the last step to initialize the Moment instance
            createdAt = System.currentTimeMillis();
        }
    }


}
