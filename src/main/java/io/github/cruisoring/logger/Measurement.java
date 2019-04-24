package io.github.cruisoring.logger;

import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.function.SupplierThrowable;
import io.github.cruisoring.table.Columns;
import io.github.cruisoring.table.IColumns;
import io.github.cruisoring.table.TupleRow;
import io.github.cruisoring.table.TupleTable;
import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple7;
import io.github.cruisoring.utility.ArrayHelper;
import io.github.cruisoring.utility.StackTraceHelper;
import io.github.cruisoring.utility.StringHelper;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Measurement {
    //Common columns used to log info
    public static final String START = "start";
    public static final String DURATION = "duration";
//    public static final String WITH_EXCEPTION = "withException";
//    public static final String LOAD = "load";

    public static final IColumns DefaultColumns = new Columns(START, DURATION);

    //Identifier to locate the caller stack trace quickly
    static final String getCallerStackTraceKey = Measurement.class.getSimpleName() + ".java";

    static final Map<String, TupleTable> namedMeasurements = new HashMap<>();

    public static Moment start(String format, Object... args) {
        return new Moment(format, args);
    }

    public static Moment start() {
        return new Moment();
    }

    private static Class getClass(Object obj) {
        return obj == null ? Object.class : obj.getClass();
    }

    public static void save(String label, TupleRow details) {
        if (!namedMeasurements.containsKey(label)) {
            if (DefaultColumns == details.getColumnIndexes()) {
                TupleTable table = DefaultColumns.createTable(null, Long.class, Long.class);
                namedMeasurements.put(label, table);
            } else {
                List<String> columnNames = details.getColumnIndexes().getColumnNames();
                Class[] classes = ArrayHelper.create(Class.class, columnNames.size(), i -> getClass(details.getValueByName(columnNames.get(i))));
                IColumns columns = new Columns(columnNames.toArray(new String[0]));
                TupleTable table = columns.createTable(null, classes);
                namedMeasurements.put(label, table);
            }
        }
        namedMeasurements.get(label).addValues(details);
    }

    public static Set<String> getMeasuredLabels() {
        return namedMeasurements.keySet();
    }

    public static TupleTable getMeasurements(String label) {
        if (!namedMeasurements.containsKey(label))
            return null;

        return namedMeasurements.get(label);
    }

    public static Tuple7<String, Long, Long, Long, Long, Long, Double> defaultSummaryOf(String label) {
        TupleTable table = getMeasurements(label);
        if (table == null || !Long.class.equals(table.getColumnElementType(DURATION))) {
            return null;
        }

        Long[] durations = (Long[]) table.getColumnValues(DURATION);

        AtomicLong sum = new AtomicLong(0);
        List<Long> durationList = new ArrayList<>();
        Arrays.stream(durations).forEach(d -> {
            durationList.add(d);
            sum.getAndAdd(d);
        });

        Collections.sort(durationList);
        long total = sum.get();
        int size = durationList.size();
        long mean = total / size;
        long min = durationList.get(0);
        long max = durationList.get(size - 1);
        long median = durationList.get(size / 2);

        double summation = 0;
        long dif;
        for (int i = 0; i < size; i++) {
            dif = durationList.get(i) - mean;
            summation += dif * dif;
        }
        Double standardDeviation = Math.sqrt(summation / size);
        String summary = String.format("%s: <size=%d, mean=%d, median=%d, total=%d, min=%d, max=%d, std=%.2f%%>",
                label, size, mean, median, total, min, max, standardDeviation);
        return Tuple.create(summary, mean, median, total, min, max, standardDeviation);
    }

    public static Map<String, String> getAllSummary() {
        Set<String> labels = getMeasuredLabels();
        Map<String, String> all = labels.stream()
                .collect(Collectors.toMap(
                        label -> label,
                        label -> defaultSummaryOf(label).getFirst()
                ));
        return all;
    }

    public static <R> R measure(String label, int times, SupplierThrowable<R> supplierThrowable, LogLevel level){
        Objects.requireNonNull(label);
        Objects.requireNonNull(supplierThrowable);

        if(namedMeasurements.containsKey(label)){
            namedMeasurements.get(label).clear();
        }

        R result=null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            result = supplierThrowable.orElse(null).get();
            save(label, DefaultColumns.createRow(start, System.currentTimeMillis()-start));
            start = System.currentTimeMillis();
        }

        Tuple7<String, Long, Long, Long, Long, Long, Double> summary = defaultSummaryOf(label);
        Logger.getDefault().log(level, summary.getFirst());
        return result;
    }

    public static void measure(String label, int times, RunnableThrowable runnableThrowable, LogLevel level){
        Objects.requireNonNull(label);
        Objects.requireNonNull(runnableThrowable);

        if(namedMeasurements.containsKey(label)){
            namedMeasurements.get(label).clear();
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            runnableThrowable.tryRun();
            save(label, DefaultColumns.createRow(start, System.currentTimeMillis()-start));
            start = System.currentTimeMillis();
        }

        Tuple7<String, Long, Long, Long, Long, Long, Double> summary = defaultSummaryOf(label);
        Logger.getDefault().log(level, summary.getFirst());
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
