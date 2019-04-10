package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.tuple.Tuple2;

import java.util.Objects;

public class NameValuePair<T> extends Tuple2<String, T> {

    public NameValuePair(String name, T value) {
        super(name, value);
        Objects.requireNonNull(name);
    }

    public String getName() {
        return getFirst();
    }

    public T getValue() {
        return getSecond();
    }

    @Override
    public String toString() {
        if (_toString == null) {
            _toString = getFirst() + ": " + TypeHelper.deepToString(values[1]);
        }
        return _toString;
    }
}
