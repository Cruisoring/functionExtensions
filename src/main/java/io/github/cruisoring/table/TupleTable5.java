package io.github.cruisoring.table;

import io.github.cruisoring.tuple.Tuple;
import io.github.cruisoring.tuple.Tuple5;
import io.github.cruisoring.tuple.WithValues5;
import io.github.cruisoring.utility.ArrayHelper;

public class TupleTable5<T, U, V, W, X> extends TupleTable<WithValues5<T, U, V, W, X>> {

    protected TupleTable5(IColumns columns, Class<? extends T> typeT, Class<? extends U> typeU, Class<? extends V> typeV,
                          Class<? extends W> typeW, Class<? extends X> typeX) {
        super(columns, typeT, typeU, typeV, typeW, typeX);
        if(columns.width() < 5){
            throw new UnsupportedOperationException("Not enough columns defined!");
        }
    }


    public boolean addValues(T t, U u, V v, W w, X x) {
        return addValues(Tuple.create(t, u, v, w, x));
    }

    public boolean addValues(T t, U u, V v, W w, X x, Object... more) {
        return addValues(Tuple.of(ArrayHelper.append(more, t, u, v, w, x)));
    }

    public boolean add(Tuple5<T, U, V, W, X> row){
        return row == null ? false : rows.add(row);
    }

    public boolean add(WithValuesByName5<T, U, V, W, X> row) {
        if (row == null) {// || row.getColumnIndexes() != this.columns) {
            return false;
        }

        return rows.add(row.getValues());
    }

    @Override
    public boolean add(WithValuesByName withValues) {
        try {
            WithValues5<T, U, V, W, X> values = (WithValues5<T, U, V, W, X>)withValues;

            if (values == null) {
                return false;
            }

            T t = values.getFirst();
            U u = values.getSecond();
            V v = values.getThird();
            W w = values.getFourth();
            X x = values.getFifth();

            return addValues(t, u, v, w, x);
        }catch (Exception e){
            return false;
        }
    }
}
