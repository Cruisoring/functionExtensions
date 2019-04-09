package io.github.cruisoring.utility;

import org.junit.Test;

import java.time.DayOfWeek;

import static org.junit.Assert.assertEquals;

public class StringHelperTest {

    @Test
    public void parseEnum() throws Exception {
        DayOfWeek day = StringHelper.parseEnum(DayOfWeek.class, "Tuesday");
        assertEquals(DayOfWeek.TUESDAY, day);
    }

    @Test
    public void parse() {
        DayOfWeek day = StringHelper.parse("Wednesday", DayOfWeek.class, DayOfWeek.MONDAY);
        assertEquals(DayOfWeek.WEDNESDAY, day);

        int intValue = StringHelper.parse("34", int.class, -1);
        assertEquals(34, intValue);
    }
}