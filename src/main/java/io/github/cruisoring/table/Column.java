package io.github.cruisoring.table;

import io.github.cruisoring.function.PredicateThrowable;

import java.lang.reflect.Type;
import java.util.Objects;

public class Column {
    final int index;
    final String name;
    final PredicateThrowable<Object> isValid;
    final String[] aliases;

    protected Column(int index, String name, PredicateThrowable<Object> isValid, String... aliases){
        Objects.requireNonNull(name);

        this.index = index;
        this.name = name;
        this.isValid = isValid;
        this.aliases = aliases;
    }

    @Override
    public String toString() {
        return String.format("%s[%d]", name, index);
    }
}
