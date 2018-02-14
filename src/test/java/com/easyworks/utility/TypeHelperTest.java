package com.easyworks.utility;

import com.easyworks.function.SupplierThrows;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.junit.Test;

import java.lang.reflect.GenericArrayType;
import java.time.Month;
import java.time.temporal.TemporalUnit;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.Assert.*;

public class TypeHelperTest {

    @Test
    public void getClassPredicate_withValueTypes_handleBothValueAndWrapperClass() {
        SupplierThrows.PredicateThrows<Class> intPredicate = TypeHelper.getClassPredicate(int.class);
        assertTrue(intPredicate.testNoThrows(int.class));
        assertTrue(intPredicate.testNoThrows(Integer.class));
        assertFalse(intPredicate.testNoThrows(Boolean.class));
    }

    @Test
    public void getClassPredicate_withValueArrayTypes_handleBothValueAndWrapperClass() {
        SupplierThrows.PredicateThrows<Class> intPredicate = TypeHelper.getClassPredicate(int[].class);
        assertTrue(intPredicate.testNoThrows(int[].class));
        assertTrue(intPredicate.testNoThrows(Integer[].class));
        assertFalse(intPredicate.testNoThrows(Boolean[].class));
        assertFalse(intPredicate.testNoThrows(Object[].class));
        assertFalse(intPredicate.testNoThrows(int.class));
    }

    interface Testable{}
    class A implements Testable{}
    class B extends A{}
    class C extends B{}
    class X implements  Testable{}


    @Test(expected = NullPointerException.class)
    public void getDeclaredTypeTest_withNoneInstance_returnObjectType() {
        assertEquals(Object.class, TypeHelper.getDeclaredType());
        assertEquals(Object.class, TypeHelper.getDeclaredType(null));
    }

    @Test
    public void getDeclaredTypeTest_ofSingleInstance_getTypeAsDeclared() {
        String str = null;
        assertEquals(String.class, TypeHelper.getDeclaredType(str));
        assertEquals(Integer.class, TypeHelper.getDeclaredType(33));
        List<Integer> intList = null;
        assertEquals(List.class, TypeHelper.getDeclaredType(intList));
        assertEquals(GenericA.class, TypeHelper.getDeclaredType(new GenericA<String>()));
        assertEquals(GenericA.class, TypeHelper.getDeclaredType(new GenericA[]{new GenericA3()}));
        assertEquals(IA.class, TypeHelper.getDeclaredType(new IA[]{new GenericA3()}));
        assertEquals(GenericA3.class, TypeHelper.getDeclaredType(new GenericA3()));
        assertEquals(GenericA.class, TypeHelper.getDeclaredType(new GenericA[]{new GenericC2(), null}));

        IAB iab = new GenericD1();
        assertEquals(IAB.class, TypeHelper.getDeclaredType(iab));
    }

    @Test
    public void getDeclaredTypeTest_ofMultipleInstances_getBestMatchedType(){
        assertEquals(Integer.class, TypeHelper.getDeclaredType(33, 22, Integer.valueOf(11)));

        assertEquals(A.class, TypeHelper.getDeclaredType(new A(), new A()));
        assertEquals(A.class, TypeHelper.getDeclaredType(new A(), new B()));
        assertEquals(A.class, TypeHelper.getDeclaredType(new A(), new B(), new C()));
        assertEquals(Testable.class, TypeHelper.getDeclaredType(new A(), new B(), new C(),  new X()));
        assertEquals(B.class, TypeHelper.getDeclaredType(new B(), new C()));
        assertEquals(Object.class, TypeHelper.getDeclaredType(new B(), new C(), false));

        A[] array = new A[]{new C(), new C()};
        assertEquals(A.class, TypeHelper.getDeclaredType(array));
        B[] arrayB = new B[]{new C(), new C()};
        assertEquals(B.class, TypeHelper.getDeclaredType(arrayB));

        List<String> listString = new ArrayList<String>();
        Collection<String> list2 = new ArrayList<>();

        assertEquals(Collection.class, TypeHelper.getDeclaredType(listString, list2));

        GenericA[] aArray = new GenericA[]{new GenericA(), new GenericA1(), new GenericA2(), new GenericA3()};
        assertEquals(GenericA.class, TypeHelper.getDeclaredType(aArray));
        IA[] iaArray = new IA[]{new GenericA<>(), new GenericA1(), new GenericA1(), new GenericA3(), new GenericC1(), new GenericD1()};
        assertEquals(IA.class, TypeHelper.getDeclaredType(iaArray));
    }


    interface IA<T> {}
    interface IB<T> {}
    interface IAB<T,U> extends IA<T>, IB<U>{}

    class GenericA<T> implements IA<T>{}
    class GenericA1 extends GenericA<Double> {}
    class GenericA2 extends GenericA1 {}
    class GenericA3 extends GenericA2 {}
    class GenericB<T> implements IB<T>{}
    class GenericC<T,U> extends GenericA<T> implements IB<U>, IAB<T,U>{}
    class GenericC1 extends GenericC<String, Integer>{}
    class GenericC2 extends GenericC<A, B>{}
    class GenericD<T,U> extends GenericB<U> implements IAB<T,U>, IA<T>{}
    class GenericC3 extends GenericC<GenericC1, GenericC2>{}
    class GenericD1 extends GenericD<Boolean, Integer>{}

    @Test
    public void getGenericType_withGivenClass_getRightGenericType(){
        assertEquals(null, TypeHelper.getGenericType(Object.class));
        assertEquals(null, TypeHelper.getGenericType(String.class));
        assertEquals(null, TypeHelper.getGenericType(String[].class));
        assertEquals(null, TypeHelper.getGenericType(Boolean.class));
        assertEquals(null, TypeHelper.getGenericType(AtomicInteger.class));
        assertEquals(Enum.class, TypeHelper.getGenericType(Enum.class));
        assertEquals(Enum.class, TypeHelper.getGenericType(Month.class));

        assertEquals(ArrayList.class, TypeHelper.getGenericType(ArrayList.class));
        assertEquals(CopyOnWriteArrayList.class, TypeHelper.getGenericType(CopyOnWriteArrayList.class));
        assertEquals(ConcurrentSkipListMap.class, TypeHelper.getGenericType(ConcurrentSkipListMap.class));

        assertEquals(List.class, TypeHelper.getGenericType(List.class));


        assertEquals(GenericA.class, TypeHelper.getGenericType(new GenericA<Byte>().getClass()));
        assertEquals(GenericA.class, TypeHelper.getGenericType(GenericA.class));
        assertEquals(GenericA.class, TypeHelper.getGenericType(GenericA1.class));
        assertEquals(GenericA.class, TypeHelper.getGenericType(GenericA2.class));
        assertEquals(GenericA.class, TypeHelper.getGenericType(GenericA3.class));

        assertEquals(GenericB.class, TypeHelper.getGenericType(GenericB.class));
        assertEquals(GenericC.class, TypeHelper.getGenericType(GenericC.class));
        assertEquals(GenericC.class, TypeHelper.getGenericType(GenericC1.class));
        assertEquals(GenericC.class, TypeHelper.getGenericType(GenericC2.class));
        assertEquals(GenericC.class, TypeHelper.getGenericType(GenericC3.class));
        assertEquals(GenericD.class, TypeHelper.getGenericType(GenericD.class));
        assertEquals(GenericD.class, TypeHelper.getGenericType(GenericD1.class));
    }

    @Test
    public void getGenericType_withGivenInstance_getRightGenericType(){
        assertEquals(GenericA.class, TypeHelper.getGenericType(new GenericA()));
        assertEquals(GenericA.class, TypeHelper.getGenericType(new GenericA<Byte>()));
        assertEquals(GenericA.class, TypeHelper.getGenericType(new GenericA1()));
        assertEquals(GenericA.class, TypeHelper.getGenericType(new GenericA2()));
        assertEquals(GenericA.class, TypeHelper.getGenericType(new GenericA3()));

        assertEquals(GenericB.class, TypeHelper.getGenericType(new GenericB()));
        assertEquals(GenericB.class, TypeHelper.getGenericType(new GenericB<String>()));
        assertEquals(GenericC.class, TypeHelper.getGenericType(new GenericC()));
        assertEquals(GenericC.class, TypeHelper.getGenericType(new GenericC<Boolean, Integer>()));
        assertEquals(GenericC.class, TypeHelper.getGenericType(new GenericC1()));
        assertEquals(GenericC.class, TypeHelper.getGenericType(new GenericC2()));
        assertEquals(GenericC.class, TypeHelper.getGenericType(new GenericC3()));
        assertEquals(GenericD.class, TypeHelper.getGenericType(new GenericD()));
        assertEquals(GenericD.class, TypeHelper.getGenericType(new GenericD<String, Enum>()));
        assertEquals(GenericD.class, TypeHelper.getGenericType(new GenericD1()));
    }

    @Test
    public void getGenericType_withLambdaClass(){
        Function<String, Boolean> fun1 = null;
        assertEquals(Function.class, TypeHelper.getGenericType(fun1));
    }
}