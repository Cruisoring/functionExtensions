package io.github.cruisoring.utility;

import org.junit.Test;

import java.time.DayOfWeek;

import static io.github.cruisoring.Asserts.*;


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

    @Test
    public void containsAll() {
        assertTrue(
            StringHelper.containsAll("The containsAll test failed with 246, \"clock\", '^', 33.26, false, null",
                null),
            StringHelper.containsAll("The containsAll test failed with 246, \"clock\", '^', 33.26, false, null",
                null, null),
            StringHelper.containsAll("The containsAll test failed with 246, \"clock\", '^', 33.26, null, false",
                "clock", '^', 33.26, false, "containsAll", 246, null)
        );
        assertFalse(
            StringHelper.containsAll("The containsAll test failed with 246, \"clock\", '^', 33.26, false",
                null),
            StringHelper.containsAll("The containsAll test failed with 246, \"clock\", '^', 33.26, Null",
                null),
            StringHelper.containsAll("The containsAll test failed with 246, \"clock\", '^', 33.26, false",
                "containsAll", 246, "Clock", '^', 33.26, false),
            StringHelper.containsAll("The containsAll test failed with 246, \"clock\", '^', 33.26, false",
                "containsAll", null, "Clock", '^', 33.26, false)
        );
    }

    @Test
    public void containsAllIgnoreCase() {
        assertTrue(
            StringHelper.containsAllIgnoreCase("The containsAll test failed with 246, \"clock\", '^', 33.26, false, NULL"),
            StringHelper.containsAllIgnoreCase("The containsAll test failed with 246, \"clock\", '^', 33.26, false, NULL",
                null),
            StringHelper.containsAllIgnoreCase("The containsAll test failed with 246, \"clock\", '^', 33.26, false, null",
                '^', 33.26, false, "ContainsALL", 246, "Clock", null)
        );
        assertFalse(
            StringHelper.containsAllIgnoreCase("The containsAll test failed with 246, \"clock\", '^', 33.26, false",
                null),
            StringHelper.containsAllIgnoreCase("The containsAll test failed with 246, \"clock\", '^', 33.26, Null",
                null, 247)
        );
    }

    @Test
    public void containsAny() {
        assertException(() -> StringHelper.containsAny(null, 123), NullPointerException.class);
        assertException(() -> StringHelper.containsAny(null, null), NullPointerException.class);

        assertTrue(
            StringHelper.containsAny("123, 432, true, null", 463, false, null, "abc"),
            StringHelper.containsAny("123, 432, true, null", 463, true, "abc"),
            StringHelper.containsAny("123, 432, true, null", 463, "true", "abc")
        );

        assertFalse(
            StringHelper.containsAny("context"),
            StringHelper.containsAny("123, 432, true, null, 2019-04-01", 463, false, "abc"),
            StringHelper.containsAny("123, 432, true, null, 2019-04-01", 463, "True", "abc")
        );
    }

    @Test
    public void tryFormatString() {
        assertEquals("name: 12", StringHelper.tryFormatString("%s: %d", "name", 12));
        assertEquals("MalFormated format: '%s: %d' where args[2]: 'name, 12'", StringHelper.tryFormatString("%s: %d", "name", "12"));
        assertEquals("MalFormated format: '%s: %d' where args[2]: 'null, 12'", StringHelper.tryFormatString("%s: %d", null, "12"));
        assertEquals("MalFormated format: '%s: %d' where args[1]: 'name'", StringHelper.tryFormatString("%s: %d", "name"));
        assertEquals("MalFormated format: '%s: %d' where args[3]: 'name, 12, 345'", StringHelper.tryFormatString("%s: %d", "name", "12", 345));
    }
}