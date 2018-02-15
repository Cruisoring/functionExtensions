package com.easyworks.utility;

import com.easyworks.function.SupplierThrowable;
import org.junit.Test;

import java.time.Month;
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
        SupplierThrowable.PredicateThrowable<Class> intPredicate = TypeHelper.getClassPredicate(int.class);
        assertTrue(intPredicate.testNoThrows(int.class));
        assertTrue(intPredicate.testNoThrows(Integer.class));
        assertFalse(intPredicate.testNoThrows(Boolean.class));
    }

    @Test
    public void getClassPredicate_withValueArrayTypes_handleBothValueAndWrapperClass() {
        SupplierThrowable.PredicateThrowable<Class> intPredicate = TypeHelper.getClassPredicate(int[].class);
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
        assertEquals(null, TypeHelper.getGenericTypeFromType(Object.class));
        assertEquals(null, TypeHelper.getGenericTypeFromType(String.class));
        assertEquals(null, TypeHelper.getGenericTypeFromType(String[].class));
        assertEquals(null, TypeHelper.getGenericTypeFromType(Boolean.class));
        assertEquals(null, TypeHelper.getGenericTypeFromType(AtomicInteger.class));
        assertEquals(Enum.class, TypeHelper.getGenericTypeFromType(Enum.class));
        assertEquals(Enum.class, TypeHelper.getGenericTypeFromType(Month.class));

        assertEquals(ArrayList.class, TypeHelper.getGenericTypeFromType(ArrayList.class));
        assertEquals(CopyOnWriteArrayList.class, TypeHelper.getGenericTypeFromType(CopyOnWriteArrayList.class));
        assertEquals(ConcurrentSkipListMap.class, TypeHelper.getGenericTypeFromType(ConcurrentSkipListMap.class));

        assertEquals(List.class, TypeHelper.getGenericTypeFromType(List.class));


        assertEquals(GenericA.class, TypeHelper.getGenericTypeFromType(new GenericA<Byte>().getClass()));
        assertEquals(GenericA.class, TypeHelper.getGenericTypeFromType(GenericA.class));
        assertEquals(GenericA.class, TypeHelper.getGenericTypeFromType(GenericA1.class));
        assertEquals(GenericA.class, TypeHelper.getGenericTypeFromType(GenericA2.class));
        assertEquals(GenericA.class, TypeHelper.getGenericTypeFromType(GenericA3.class));

        assertEquals(GenericB.class, TypeHelper.getGenericTypeFromType(GenericB.class));
        assertEquals(GenericC.class, TypeHelper.getGenericTypeFromType(GenericC.class));
        assertEquals(GenericC.class, TypeHelper.getGenericTypeFromType(GenericC1.class));
        assertEquals(GenericC.class, TypeHelper.getGenericTypeFromType(GenericC2.class));
        assertEquals(GenericC.class, TypeHelper.getGenericTypeFromType(GenericC3.class));
        assertEquals(GenericD.class, TypeHelper.getGenericTypeFromType(GenericD.class));
        assertEquals(GenericD.class, TypeHelper.getGenericTypeFromType(GenericD1.class));
    }

    /**
     * Testing of getGenericType with Generic instance. Unfortunately, the argument type info cannot be kept.
     */
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

    @Test
    public void getGenericInfoFromType_withGivenClasses(){
        assertEquals("Object", TypeHelper.getGenericInfoFromType(Object.class).values[3]);
        assertEquals("String", TypeHelper.getGenericInfoFromType(String.class).values[3]);
        assertEquals("String[]", TypeHelper.getGenericInfoFromType(String[].class).values[3]);
        assertEquals("Boolean", TypeHelper.getGenericInfoFromType(Boolean.class).values[3]);
        assertEquals("Number", TypeHelper.getGenericInfoFromType(AtomicInteger.class).values[3]);
        assertEquals("Enum<E>", TypeHelper.getGenericInfoFromType(Enum.class).values[3]);
        assertEquals("Enum<Month>", TypeHelper.getGenericInfoFromType(Month.class).values[3]);

        assertEquals("ArrayList<E>", TypeHelper.getGenericInfoFromType(ArrayList.class).values[3]);
        assertEquals("CopyOnWriteArrayList<E>", TypeHelper.getGenericInfoFromType(CopyOnWriteArrayList.class).values[3]);
        assertEquals("ConcurrentSkipListMap<K,V>", TypeHelper.getGenericInfoFromType(ConcurrentSkipListMap.class).values[3]);

        assertEquals("List<E>", TypeHelper.getGenericInfoFromType(List.class).values[3]);


        assertEquals("GenericA<T>", TypeHelper.getGenericInfoFromType(new GenericA<Byte>().getClass()).values[3]);
        assertEquals("GenericA<T>", TypeHelper.getGenericInfoFromType(GenericA.class).values[3]);
        assertEquals("GenericA<Double>", TypeHelper.getGenericInfoFromType(GenericA1.class).values[3]);
        assertEquals("GenericA<Double>", TypeHelper.getGenericInfoFromType(GenericA2.class).values[3]);
        assertEquals("GenericA<Double>", TypeHelper.getGenericInfoFromType(GenericA3.class).values[3]);

        assertEquals("GenericB<T>", TypeHelper.getGenericInfoFromType(GenericB.class).values[3]);
        assertEquals("GenericC<T,U>", TypeHelper.getGenericInfoFromType(GenericC.class).values[3]);
        assertEquals("GenericC<String,Integer>", TypeHelper.getGenericInfoFromType(GenericC1.class).values[3]);
        assertEquals("GenericC<A,B>", TypeHelper.getGenericInfoFromType(GenericC2.class).values[3]);
        assertEquals("GenericC<GenericC1,GenericC2>", TypeHelper.getGenericInfoFromType(GenericC3.class).values[3]);
        assertEquals("GenericD<T,U>", TypeHelper.getGenericInfoFromType(GenericD.class).values[3]);
        assertEquals("GenericD<Boolean,Integer>", TypeHelper.getGenericInfoFromType(GenericD1.class).values[3]);
    }


    @Test
    public void getGenericInfo_withGivenInstance_getRightToken(){

        assertEquals("GenericA<T>", TypeHelper.getGenericInfo(new GenericA()).values[3]);
        assertEquals("GenericA<T>", TypeHelper.getGenericInfo(new GenericA<String>()).values[3]);
        assertEquals("GenericA<Double>", TypeHelper.getGenericInfo(new GenericA1()).values[3]);
        assertEquals("GenericA<Double>", TypeHelper.getGenericInfo(new GenericA2()).values[3]);
        assertEquals("GenericA<Double>", TypeHelper.getGenericInfo(new GenericA3()).values[3]);

        assertEquals("GenericB<T>", TypeHelper.getGenericInfo(new GenericB()).values[3]);
        assertEquals("GenericB<T>", TypeHelper.getGenericInfo(new GenericB<String>()).values[3]);
        assertEquals("GenericC<T,U>", TypeHelper.getGenericInfo(new GenericC()).values[3]);
        assertEquals("GenericC<T,U>", TypeHelper.getGenericInfo(new GenericC<Boolean, Integer>()).values[3]);
        assertEquals("GenericC<String,Integer>", TypeHelper.getGenericInfo(new GenericC1()).values[3]);
        assertEquals("GenericC<A,B>", TypeHelper.getGenericInfo(new GenericC2()).values[3]);
        assertEquals("GenericC<GenericC1,GenericC2>", TypeHelper.getGenericInfo(new GenericC3()).values[3]);
        assertEquals("GenericD<T,U>", TypeHelper.getGenericInfo(new GenericD()).values[3]);
        assertEquals("GenericD<T,U>", TypeHelper.getGenericInfo(new GenericD<String, Enum>()).values[3]);
        assertEquals("GenericD<Boolean,Integer>", TypeHelper.getGenericInfo(new GenericD1()).values[3]);

    }

}