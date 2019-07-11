package io.github.cruisoring.logger;

import io.github.cruisoring.Revokable;
import io.github.cruisoring.utility.ArrayHelper;
import org.junit.Test;

import static io.github.cruisoring.Asserts.assertEquals;
import static io.github.cruisoring.Asserts.assertTrue;


public class MeasureTest {

    int times = 10;
    int length = 1000000;
    Integer[] integers1 = new Integer[length];
    Integer[] integers2 = new Integer[length];
    Integer[] integers3 = new Integer[length];
    Integer[] integers4 = new Integer[length];

    private void fillArray(Integer[] array){
        int length = array.length;
        for (int i = 0; i < length; i++) {
            array[i] = (31+i)*i + 31;
        }
    }

    private Integer[] getArray(){
        Integer[] array = new Integer[length];
        for (int i = 0; i < length; i++) {
            array[i] = (31+i)*i + 31;
        }
        return array;
    }

    @Test
    public void measureRunnables() {
        Measurement.measure("setAll", times, () -> ArrayHelper.setAll(integers1, i -> (31+i)*i + 31), LogLevel.info);
        Measurement.measure("setAllParallel", times, () -> ArrayHelper.setAllParallel(integers2, i -> (31+i)*i + 31), LogLevel.info);
        Measurement.measure("setAllSerial", times, () -> ArrayHelper.setAllSerial(integers3, i -> (31+i)*i + 31), LogLevel.info);
        Measurement.measure("conventional", times, () -> fillArray(integers4), LogLevel.info);

        assertEquals(integers1, integers2);
        assertEquals(integers3, integers2);
        assertEquals(integers3, integers4);
    }

    @Test
    public void measureSuppliers() {
        Integer[] array1 = Measurement.measure("conventional", times, this::getArray, LogLevel.info);
        Object array2 = Measurement.measure("create", times, () -> ArrayHelper.create(Integer.class, length, i -> (31+i)*i + 31), LogLevel.debug);

        assertEquals(array1, array2);
    }

    @Test
    public void measureSuppliers_withDisableMeasurement() {
        try (
                Revokable<Boolean> revokable = Revokable.register(()->Measurement.DisableMeasurement, v->Measurement.DisableMeasurement=v, true);
                ) {
            Measurement.purge(LogLevel.info);
            Integer[] array1 = Measurement.measure("conventional", times, this::getArray, LogLevel.info);
            Object array2 = Measurement.measure("create", times, () -> ArrayHelper.create(Integer.class, length, i -> (31 + i) * i + 31), LogLevel.debug);

            assertEquals(array1, array2);
            assertTrue(Measurement.getMeasuredNames().isEmpty(), "There shall be no measurements record created.");
        }
    }
}