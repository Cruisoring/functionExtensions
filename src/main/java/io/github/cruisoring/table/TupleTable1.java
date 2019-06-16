package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues1;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.List;
import java.util.function.Supplier;

public class TupleTable1<T> extends TupleTable<WithValues1<T>> {

    protected TupleTable1(Supplier<List<WithValues>> rowsSupplier, IColumns columns,
                          Class<? extends T> typeT){
        super(rowsSupplier, columns, typeT);
        if(columns.width() < 1){
            throw new UnsupportedOperationException("There is no column defined!");
        }
    }

    public boolean addValues(T t, Object... more) {
        return addValues(Tuple.of(ArrayHelper.mergeVarargsFirst(more, t)));
    }
}
