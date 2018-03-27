package com.easyworks.repository;

import com.easyworks.tuple.Tuple;
import com.easyworks.tuple.Tuple2;
import org.junit.Test;

import static org.junit.Assert.*;

public class DualValuesRepositoryTest {

    DualValuesRepository<String, Integer, Boolean> dualValuesRepository =
            DualValuesRepository.fromKey(s -> Tuple.create(s.length(), s.contains("a")));

    @Test
    public void retrieve() {
        Tuple2 value = dualValuesRepository.retrieve("abc");
        assertNotNull(value);
        assertEquals(Integer.valueOf(3), dualValuesRepository.getFirstValue("abc"));
        assertEquals(true, dualValuesRepository.getSecondValue("abc"));

        assertEquals(Integer.valueOf(0), dualValuesRepository.getFirstValue(""));
        assertEquals(2, dualValuesRepository.getValue().size());

        assertEquals(Integer.valueOf(10), dualValuesRepository.getFirstValue("0123456789"));
        assertEquals(3, dualValuesRepository.getValue().size());

        assertNull(dualValuesRepository.retrieve(null));
        assertEquals(3, dualValuesRepository.getValue().size());
        assertEquals(null, dualValuesRepository.getFirstValue(null));
        assertEquals(null, dualValuesRepository.getSecondValue(null));
    }
}