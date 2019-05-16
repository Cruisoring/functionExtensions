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

import static io.github.cruisoring.Asserts.checkWithoutNull;

/**
 * Utility class for performance measurement.
 */
public class Measurement {
    //Common columns used to log info
    public static final String START = "start";
    public static final String DURATION = "duration";

    public static final IColumns DefaultColumns = new Columns(START, DURATION);

    //Identifier to locate the caller stack trace quickly
    static final String getCallerStackTraceKey = Measurement.class.getSimpleName() + ".java";

    /**
     * Keep all measurements as {@code TupleTable}s by names.
     */
    static final Map<String, TupleTable> namedMeasurements = new HashMap<>();

    /**
     * Helper class to compose an unique name and initial a single measurement
     *
     * @param format the format used to compose the name to identify a kind of measurement
     * @param args   the arguments used to compose the unique name to identify a kind of measurement
     * @return the {@code Moment} instance
     */
    public static Moment start(String format, Object... args) {
        return new Moment(format, args);
    }

    /**
     * Helper class to compose an unique name of caller and initial a single measurement
     *
     * @return the {@code Moment} instance
     */
    public static Moment start() {
        return new Moment();
    }

    private static Class getClass(Object obj) {
        return obj == null ? Object.class : obj.getClass();
    }

    /**
     * Save a single measurement into its {@code TupleTable} by name.
     *
     * @param name    the unique name to identify performance of concerned business logic.
     * @param details a single measurement of the performance of concerned business logic.
     */
    public static void save(String name, TupleRow details) {
        checkWithoutNull(name, details);

        if (!namedMeasurements.containsKey(name)) {
            if (DefaultColumns == details.getColumnIndexes()) {
                TupleTable table = DefaultColumns.createTable(null, Long.class, Long.class);
                namedMeasurements.put(name, table);
            } else {
                List<String> columnNames = details.getColumnIndexes().getColumnNames();
                Class[] classes = (Class[]) ArrayHelper.create(Class.class, columnNames.size(), i -> getClass(details.getValueByName(columnNames.get(i))));
                IColumns columns = new Columns(columnNames.toArray(new String[0]));
                TupleTable table = columns.createTable(null, classes);
                namedMeasurements.put(name, table);
            }
        }
        namedMeasurements.get(name).addValues(details);
    }

    /**
     * Get names of all measurements.
     *
     * @return the names of all measurements as a Set.
     */
    public static Set<String> getMeasuredNames() {
        return namedMeasurements.keySet();
    }

    /**
     * Retrieve the {@code TupleTable} containing all measurements of performance of concerned business logic by name.
     *
     * @param name the unique name to identify performance of concerned business logic.
     * @return the {@code TupleTable} containing all measurements of performance of concerned
     * business logic if it exists, otherwise <tt>null</tt>
     */
    public static TupleTable getMeasurements(String name) {
        if (!namedMeasurements.containsKey(name))
            return null;

        return namedMeasurements.get(name);
    }

    /**
     * With given name of the concerned set of measurements, get the size, mean, median, total, min, max and stdDeviation of their performances.
     *
     * @param name the unique name to identify performance of concerned business logic.
     * @return a Tuple of 7 values to summarize the performance of concerned business logic
     */
    public static Tuple7<String, Long, Long, Long, Long, Long, Double> defaultSummaryOf(String name) {
        TupleTable table = getMeasurements(checkWithoutNull(name));
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
                name, size, mean, median, total, min, max, standardDeviation);
        return Tuple.create(summary, mean, median, total, min, max, standardDeviation);
    }

    /**
     * Get summaries of all sets of measurements identified by names as a {@code Map}
     *
     * @return a {@code Map} containing summaries of all sets of measurements identified by names
     */
    public static Map<String, String> getAllSummary() {
        Set<String> labels = getMeasuredNames();
        Map<String, String> all = labels.stream()
                .collect(Collectors.toMap(
                        label -> label,
                        label -> defaultSummaryOf(label).getFirst()
                ));
        return all;
    }

    /**
     * Measure performance of concerned {@code SupplierThrowable} identified by given name,
     * trying multiple times, then log with given LogLevel and return the last value returned by the concerned {@code SupplierThrowable}.
     *
     * @param name              the unique name to identify this {@code SupplierThrowable}
     * @param times             the times to execute the concerned {@code SupplierThrowable}
     * @param supplierThrowable the business logic returning value of type <tt>R</tt> to be measured.
     * @param levels            optional {@code LogLevel} to be used to log the measurement outcome.
     * @param <R>               the type of the returned value by the concerned {@code SupplierThrowable}
     * @return the last returned value by executing the concerned {@code SupplierThrowable}
     */
    public static <R> R measure(String name, int times, SupplierThrowable<R> supplierThrowable, LogLevel... levels) {
        checkWithoutNull(name, supplierThrowable);

        if (namedMeasurements.containsKey(name)) {
            namedMeasurements.get(name).clear();
        }

        R result=null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            result = supplierThrowable.orElse(null).get();
            save(name, DefaultColumns.createRow(start, System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
        }

        Tuple7<String, Long, Long, Long, Long, Long, Double> summary = defaultSummaryOf(name);
        LogLevel level = (levels==null || levels.length==0) ? Logger.DefaultMeasureLogLevel : levels[0];
        Logger.getDefault().log(level, summary.getFirst());
        return result;
    }

    /**
     * Measure performance of concerned {@code RunnableThrowable} identified by given name,
     * trying multiple times then log with given LogLevel.
     *
     * @param name              the unique name to identify this {@code RunnableThrowable}
     * @param times             the times to execute the concerned {@code RunnableThrowable}
     * @param runnableThrowable the business logic, without returning value, to be measured.
     * @param levels            optional {@code LogLevel} to be used to log the measurement outcome.
     */
    public static void measure(String name, int times, RunnableThrowable runnableThrowable, LogLevel... levels) {
        checkWithoutNull(name);
        checkWithoutNull(runnableThrowable);

        if (namedMeasurements.containsKey(name)) {
            namedMeasurements.get(name).clear();
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            runnableThrowable.tryRun();
            save(name, DefaultColumns.createRow(start, System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
        }

        Tuple7<String, Long, Long, Long, Long, Long, Double> summary = defaultSummaryOf(name);
        LogLevel level = (levels==null || levels.length==0) ? Logger.DefaultMeasureLogLevel : levels[0];
        Logger.getDefault().log(level, summary.getFirst());
    }

    //region Definition of Moment class.
    /**
     * The {@code Moment} class identify a measurement with name that would be used to identify an unique set of measurements.
     */
    public static class Moment {
        final String label;
        final long createdAt;

        /**
         * Constructor to compose name of the measurement by its caller info.
         */
        Moment() {
            StackTraceElement stack = StackTraceHelper.getCallerStackByEntry(null, getCallerStackTraceKey);
            label = StringHelper.tryFormatString("%s(%s:%d)",
                    stack.getMethodName(), stack.getFileName(), stack.getLineNumber());

            //Ensure this is the last step to initialize the Moment instance
            createdAt = System.currentTimeMillis();
        }

        /**
         * Constructor to compose the name of the measurement with customised name.
         * @param format    the format to compose the name.
         * @param args      the arguments to compose the name.
         */
        Moment(String format, Object... args) {
            checkWithoutNull(format);
            label = StringHelper.tryFormatString(format, args);

            //Ensure this is the last step to initialize the Moment instance
            createdAt = System.currentTimeMillis();
        }
    }
    //endregion
}
