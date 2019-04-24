package io.github.cruisoring;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class RevokableTest {

    @Test
    public void revokeSettingValue(){
        AtomicInteger value = new AtomicInteger();

        assertEquals(0, value.get());
        try(Revokable<Integer> revokable1 = new Revokable<Integer>(() -> value.get(), v -> value.set(v), 1)){
            assertEquals(1, value.get());
            try(Revokable<Integer> revokable2 = new Revokable<Integer>(() -> value.get(), v -> value.set(v), 2)){
                assertEquals(2, value.get());
                try(Revokable<Integer> revokable3 = new Revokable<Integer>(() -> value.get(), v -> value.set(v), 3)){
                    assertEquals(3, value.get());
                }
                assertEquals(2, value.get());
            }
            assertEquals(1, value.get());
        }
        assertEquals(0, value.get());
    }

    @Test
    public void revokeRunnables(){
        AtomicInteger value = new AtomicInteger();

        value.set(1);
        try(Revokable<Integer> revokable1 = new Revokable<Integer>(() -> value.set(0))){
            assertEquals(1, value.get());
            value.set(2);
            try(Revokable<Integer> revokable2 = new Revokable<Integer>(() -> value.set(1))){
                assertEquals(2, value.get());
                value.set(3);
                try(Revokable<Integer> revokable3 = new Revokable<Integer>(() -> value.set(2))){
                    assertEquals(3, value.get());
                }
                assertEquals(2, value.get());
            }
            assertEquals(1, value.get());
        }
        assertEquals(0, value.get());
    }

    @Test
    public void registerRevokeSetting() {
        AtomicInteger value = new AtomicInteger();

        Revokable.register(() -> value.get(), v -> value.set(v), 1);
        assertEquals(1, value.get());

        Revokable.register(() -> value.get(), v -> value.set(v), 2);
        assertEquals(2, value.get());

        Revokable.register(() -> value.get(), v -> value.set(v), 3);
        assertEquals(3, value.get());

        Revokable.revokeAll();

        assertEquals(0, value.get());
    }

    @Test
    public void registerRunnable() {
        AtomicInteger value = new AtomicInteger();

        value.set(1);
        Revokable.register(() -> value.set(0));
        assertEquals(1, value.get());

        value.set(2);
        Revokable.register(() -> value.set(1));
        assertEquals(2, value.get());

        value.set(3);
        Revokable.register(() -> value.set(2));
        assertEquals(3, value.get());

        Revokable.revokeAll();

        assertEquals(0, value.get());
    }
}