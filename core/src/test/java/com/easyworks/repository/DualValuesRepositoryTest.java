package com.easyworks.repository;

import com.easyworks.tuple.Dual;
import com.easyworks.tuple.Tuple;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DualValuesRepositoryTest {

    DualValuesRepository<String, Integer, Boolean> dualValuesRepository =
            DualValuesRepository.fromKey(s -> Tuple.create(s.length(), s.contains("a")));

    @Test
    public void retrieve() {
        Dual value = dualValuesRepository.retrieve("abc");
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