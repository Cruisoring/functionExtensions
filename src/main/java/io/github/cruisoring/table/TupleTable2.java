package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues;
import io.github.cruisoring.tuple.WithValues2;
import io.github.cruisoring.utility.ArrayHelper;

import java.util.List;
import java.util.function.Supplier;

public class TupleTable2<T, U> extends TupleTable<WithValues2<T, U>> {

    protected TupleTable2(Supplier<List<WithValues>> rowsSupplier, IColumns columns,
                          Class<? extends T> typeT, Class<? extends U> typeU){
        super(rowsSupplier, columns, typeT, typeU);
        if(columns.width() < 2){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }

    public boolean addValues(T t, U u, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u)));
    }
}
