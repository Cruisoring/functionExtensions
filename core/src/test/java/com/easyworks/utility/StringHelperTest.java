package com.easyworks.utility;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.DayOfWeek;

import static org.junit.Assert.*;

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