package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.WithValues1;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable1<T> extends TupleTable<WithValues1<T>> {

    protected TupleTable1(IColumns columns, Class<? extends T> typeT){
        super(columns, typeT);
        if(columns.width() < 1){
            throw new UnsupportedOperationException("There is no column defined!");
        }
    }

    public boolean addValues(T t, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t)));
    }
}
