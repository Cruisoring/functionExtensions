package io.github.cruisoring.utility;

import org.junit.Test;

import java.time.LocalDate;

import static io.github.cruisoring.Asserts.assertEquals;
import static io.github.cruisoring.utility.DateTimeHelper.asString;

public class DateTimeHelperTest {

    @Test
    public void testAsString() {
        LocalDate date = LocalDate.of(2019, 1, 31);
        assertEquals("2019-01-31", asString(date));
        assertEquals("1-31, 19", asString(date, "M-D, yy"));
    }
}