package io.github.cruisoring.table;

import io.github.cruisoring.TypeHelper;
import io.github.cruisoring.tuple.Tuple2;
import io.github.cruisoring.utility.StringHelper;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NameValuePair)) {
            return false;
        } else if (obj == this) {
            return true;
        }

        NameValuePair other = (NameValuePair) obj;
        if (!other.canEqual(this)) {
            return false;
        }

        return getFirst().equals(other.getFirst()) && Objects.equals(getSecond(), other.getSecond());
    }

    public boolean canEqual(Object obj) {
        return obj instanceof NameValuePair;
    }
}
