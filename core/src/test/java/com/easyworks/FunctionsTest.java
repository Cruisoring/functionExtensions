package com.easyworks;

import com.easyworks.function.*;
import com.easyworks.utility.Logger;
import com.easyworks.utility.TypeHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class FunctionsTest {

    private static List<String> logs = new ArrayList();

    static Functions logToList = new Functions<Object>(
            ex -> {
                String msg = ex.getMessage();
                msg = ex.getClass().getSimpleName() + (msg == null?"":":"+msg);
                logs.add(msg);
                Logger.L(msg);
            },
            lambda -> {
                Class returnType = TypeHelper.getReturnType(lambda);
                return TypeHelper.getDefaultValue(returnType);
            });

    @Before
    public void beforeTest(){
        logs.clear();
    }

    private void testRun(){
        logs.add("testRun is called.");
    }

    public static int sumOf(String num1, int num2) throws Exception{
        return Integer.valueOf(num1) + num2;
    }

    private static void doNothing(int number){}

    @Test
    public void getReturnType() {
        ConsumerThrowable<Integer> consumerInteger = FunctionsTest::doNothing;
        assertEquals(void.class, TypeHelper.getReturnType(consumerInteger));
        assertEquals(Integer.class, TypeHelper.getReturnType((SupplierThrowable<Integer>)(()->100)));

        BiFunctionThrowable<String, Integer, Integer> intFunc = FunctionsTest::sumOf;
        assertEquals(Integer.class, TypeHelper.getReturnType(intFunc));

        SupplierThrowable<List<String>> stringListSupplier = () -> new ArrayList<String>();
        assertEquals(List.class, TypeHelper.getReturnType(stringListSupplier));
    }

    @Test
    public void run() {
        Functions.ThrowsRuntimeException.run(this::testRun);
        assertEquals("testRun is called.", logs.get(0));
        Functions.ThrowsRuntimeException.run(()->doNothing(33));
        logToList.run(()->sumOf("eight", 7));
        assertTrue(logs.get(1).contains(NumberFormatException.class.getSimpleName()));
    }

    @Test
    public void apply() {
        int intValue = (int) logToList.apply(() -> 111);
        assertEquals(111, intValue);
        //Java is not smart enough to treat the Lambda as SupplierThrowable<Integer>, specifying it clearly
        intValue = (int) logToList.apply((SupplierThrowable<Integer>) () -> (int)(111/0));
        assertEquals(0, intValue);
        assertTrue(logs.get(0).contains("ArithmeticException"));
    }

    @Test
    public void run1() {
        Functions.ThrowsRuntimeException.run(System.out::println, "Good");
        //logToList.run(null, (String s) -> System.out.print(s.length())); //Not working: expect Object but found String
        Functions.ThrowsRuntimeException.run(s -> logs.add((String) s), "OK");
        assertEquals("OK", logs.get(0));
        logToList.run(s -> System.out.print(((String)s).length()), null);
        assertTrue(logs.get(1).contains(NullPointerException.class.getSimpleName()));
    }

    BiConsumerThrowable<String,Integer> f2 = (str, num) -> System.out.println(str.substring(0, num));

    @Test
    public void run2() {
        Functions.ThrowsRuntimeException.run( f2, "Good", 3);
        logToList.run(f2, "Bad", -3);
        assertTrue(logs.get(0).contains("OutOfBoundsException"));
        logToList.run(f2, null, 2);
        assertTrue(logs.get(1).contains("NullPointerException"));
    }

    TriConsumerThrowable<String,Integer,Boolean> f3 = (str, num, expected) -> Logger.L("%s", str.length()>num == expected);

    @Test
    public void run3() {
        Functions.ThrowsRuntimeException.run(f3, "Good", 3, true);
        logToList.run(f3, null, -3, false);
        assertTrue(logs.get(0).contains("NullPointerException"));
    }


    QuadConsumerThrowable<String,Integer,Boolean, String> f4 = (str, num, expected, s2) -> Logger.L("%s",
            s2.length()>str.substring(num).length() == expected);

    @Test
    public void run4() {
        Functions.ThrowsRuntimeException.run(f4, "Good", 3, true, "");
        logToList.run(f4, "something", -3, false, "");
        assertTrue(logs.get(0).contains("OutOfBoundsException"));
        logToList.run(f4, "good", 3, false, "Good enough");
        assertTrue(logs.size() == 1);
    }

    PentaConsumerThrowable<Character, Character,Character,Character, String> f5 = (a, b, c, d, str) -> Logger.L("%s",
            (int)a+(int)b+(int)c+(int)d > Integer.valueOf(str));

    @Test
    public void run5() {
        Functions.ThrowsRuntimeException.run(f5, 'a', 'b', 'c', 'd', "111");
        logToList.run(f5, 'a', 'b', 'c', 'd', "One");
        assertTrue(logs.get(0).contains("NumberFormatException"));
        logToList.run(f5, null,'b', 'c', 'd', "111");
        assertTrue(logs.get(1).contains("NullPointerException"));

    }

    HexaConsumerThrowable<Character, Character,Character,Character, Integer, String> f6 = (a, b, c, d, n, str) -> Logger.L("%s",
            (int)a+(int)b+(int)c+(int)d + n > Integer.valueOf(str));

    @Test
    public void run6() {
        Functions.ThrowsRuntimeException.run(f6, 'a', 'b', 'c', 'd', -100, "111");
        logToList.run(f6, 'a', 'b', 'c', 'd', 21, "One");
        assertTrue(logs.get(0).contains("NumberFormatException"));
        logToList.run(f6, null,'b', 'c', 'd', 100, "111");
        assertTrue(logs.get(1).contains("NullPointerException"));
    }

    HeptaConsumerThrowable<Character, Character,Character,Character, Integer, Integer, String> f7 = (a, b, c, d, n, bt, str) -> Logger.L("%s",
            (int)a+b+c+d + n -bt > Integer.valueOf(str));

    @Test
    public void run7() {
        Functions.ThrowsRuntimeException.run(f7, 'a', 'b', 'c', 'd', -100, -100, "111");
        logToList.run(f7, 'a', 'b', 'c', 'd', 21, null, "One");
        assertTrue(logs.get(0).contains("NullPointerException"));
        logToList.run(f7, 'a', 'b', 'c', 'd', 21, 3, "One");
        assertTrue(logs.get(1).contains("NumberFormatException"));
        logToList.run(f6, null,'b', 'c', 'd', 100, "111");
        assertTrue(logs.get(2).contains("NullPointerException"));
    }

    PredicateThrowable<String> p1 = s -> s.length() > 10;
    @Test
    public void test1() {
        boolean result = Functions.ThrowsRuntimeException.test(p1, "Abc");
        assertEquals(false, result);
        result = logToList.test(p1, null);
        assertEquals(false, result);
        assertTrue(logs.get(0).contains("NullPointerException"));
    }

    BiPredicateThrowable<String, Boolean> p2 = (s, b) -> (s.length() > 10) || b;
    @Test
    public void test2() {
        boolean result = Functions.ThrowsRuntimeException.test(p2, "Abc", true);
        assertEquals(true, result);
        result = logToList.test(p2, null, false);
        assertEquals(false, result);
        assertTrue(logs.get(0).contains("NullPointerException"));
    }

    FunctionThrowable<String, Integer> f8 = s -> Integer.valueOf(s);
    @Test
    public void apply1() {
        Integer result = (Integer) Functions.ThrowsRuntimeException.apply(f8, "8");
        assertEquals(Integer.valueOf(8), result);
        result = (Integer)logToList.apply(f8, "8");
        assertEquals(Integer.valueOf(8), result);
        result = (Integer)logToList.apply(f8, "8.0");
        assertEquals(Integer.valueOf(0), result);
        assertTrue(logs.get(0).contains("NumberFormatException"));

        //Following statement would get null
        Integer r = (Integer) Functions.ReturnsDefaultValue.apply(s -> Integer.valueOf((String)s), "33.3");
        r = (Integer) Functions.ReturnsDefaultValue.apply(f8, "33.3");
        Assert.assertEquals(Integer.valueOf(0), r);
    }

    BiFunctionThrowable<String, Boolean, Integer> f9 = (s, b) -> Integer.valueOf(s) + (b ? 1 : 0);
    @Test
    public void apply2() {
        Integer result = (Integer) Functions.ThrowsRuntimeException.apply(f9, "8", true);
        assertEquals(Integer.valueOf(9), result);
        result = (Integer)logToList.apply(f9, "8", false);
        assertEquals(Integer.valueOf(8), result);
        result = (Integer)logToList.apply(f9, "8.0", true);
        assertEquals(Integer.valueOf(0), result);
        assertTrue(logs.get(0).contains("NumberFormatException"));
    }

    TriFunctionThrowable<String, Integer[], Boolean, Integer> f10 =
            (s, ints, b) -> Integer.valueOf(s) + ints.length + (b ? 1 : 0);
    @Test
    public void apply3() {
        Integer result = (Integer) Functions.ThrowsRuntimeException.apply(f10, "8", new Integer[]{1,2}, true);
        assertEquals(Integer.valueOf(11), result);
        result = (Integer)logToList.apply(f10, "8", new Integer[0], false);
        assertEquals(Integer.valueOf(8), result);
        result = (Integer)logToList.apply(f10, "8", null, true);
        assertEquals(Integer.valueOf(0), result);
        assertTrue(logs.get(0).contains("NullPointerException"));
    }

    QuadFunctionThrowable<Boolean, String, Integer, String[], String[]> f11 =
            (b, s, i, ss) -> new String[] {b.toString(), s, i.toString(), ss[0]};
    @Test
    public void apply4() {
        String[] result = (String[]) Functions.ThrowsRuntimeException.apply(f11, true, "a", 33, new String[]{"", "b"});
        assertTrue(TypeHelper.deepEquals(new String[]{"true", "a", "33", ""}, result));

        result = (String[]) logToList.apply(f11, true, "a", 33, new String[]{});
        if((System.getProperties().containsKey("EMPTY_ARRAY_AS_DEFAULT")
                && "false".equalsIgnoreCase(System.getProperty("EMPTY_ARRAY_AS_DEFAULT"))))
            assertNull(result);
        else
            assertEquals(0, result.length);
        assertTrue(logs.get(0).contains("OutOfBoundsException"));
    }

    PentaFunctionThrowable<Boolean, String, Integer, String[], Integer, Boolean> f12 =
            (b, s, i1, ss, i2) -> (b?1:0)+s.length()+i1+ss.length<i2;
    @Test
    public void apply5() {
        Boolean result = (Boolean) Functions.ThrowsRuntimeException.apply(f12, true, "abc", 3, new String[]{""}, 10);
        assertTrue(result);

        result = (Boolean) logToList.apply(f12, true, "abc", 3, "", 10);
        assertFalse(result);
        assertTrue(logs.get(0).contains("ClassCastException"));
    }

    HexaFunctionThrowable<String, String, String, String,String, String, Integer> f13 =
            (s1, s2, s3, s4, s5, s6) -> s1.length() + s2.length()+s3.length()+s4.length()+s5.length()+s6.length();
    @Test
    public void apply6() {
        Integer result = (Integer) Functions.ThrowsRuntimeException.apply(f13, "a", "bc", "def", "ghij", "klmno", "pqrstu");
        assertEquals(Integer.valueOf(21), result);

        result = (Integer) logToList.apply(f13, "a", "bc", "def", "ghij", "klmno", null);
        assertEquals(Integer.valueOf(0), result);
        assertTrue(logs.get(0).contains("NullPointerException"));
    }

    HeptaFunctionThrowable<Boolean, Boolean, Boolean, Boolean,Boolean, Boolean, Boolean, Integer> f14 =
            (b1, b2, b3, b4, b5, b6, b7) -> (b1?1:0) + (b2?1:0) + (b3?1:0) + (b4?1:0) + (b5?1:0) + (b6?1:0) + (b7?1:0);
    @Test
    public void apply7() {
        Integer result = (Integer) Functions.ThrowsRuntimeException.apply(f14, false, true, false, true, true, true, false);
        assertEquals(Integer.valueOf(4), result);

        result = (Integer) logToList.apply(f14, false, null, null, null, null, null, null);
        assertEquals(Integer.valueOf(0), result);
        assertTrue(logs.get(0).contains("NullPointerException"));
    }

    @Test
    public void buildFunctions(){
        Class<? extends Exception>[] negligibles = new Class[]{
                NullPointerException.class, IllegalArgumentException.class
        };
        Class<? extends Exception>[] noticeables = new Class[]{
                SQLException.class, NumberFormatException.class
        };
        List<String> logs = new ArrayList();
        ExceptionHandler exHandler = ex -> {
            final Class<? extends Exception> exceptionType = ex.getClass();
            if(IntStream.range(0, noticeables.length).anyMatch(i -> noticeables[i].isAssignableFrom(exceptionType))){
                String msg = ex.getMessage();
                msg = ex.getClass().getSimpleName() + (msg == null?"":":"+msg);
                logs.add(msg);
            }else if(IntStream.range(0, negligibles.length).allMatch(i -> !negligibles[i].isAssignableFrom(exceptionType))){
                throw new RuntimeException(ex);
            }
        };
        DefaultReturner defaultReturner = lambda -> {
            final Class returnType = TypeHelper.getReturnType(lambda);
            if(returnType == Integer.class || returnType == int.class)
                return -1;
            return TypeHelper.getDefaultValue(returnType);
        };

        Functions customFunctions = Functions.buildFunctions(exHandler, defaultReturner);

        Integer result = (Integer) customFunctions.apply(f14, false, null, null, null, null, null, null);
        assertEquals(Integer.valueOf(-1), result);
        assertTrue(logs.isEmpty());

        result = (Integer)customFunctions.apply(f9, "8.0", true);
        assertEquals(Integer.valueOf(-1), result);
        assertTrue(logs.get(0).contains("NumberFormatException"));

        BiFunctionThrowable<String[], Integer, Integer> ff = (sArray, index) -> sArray[index].length();
        result = (Integer)customFunctions.apply(ff, new String[]{"a", "ab"}, 1);
        assertEquals(Integer.valueOf(2), result);

        result = (Integer)customFunctions.apply(ff, new String[]{null, null}, 1);
        assertEquals(Integer.valueOf(-1), result);

        //Following statement would throw RuntimeException caused by ArrayIndexOutOfBoundsException
        //result = (Integer)customFunctions.apply(new String[]{"a", "ab"}, 2, ff);
    }

    @Test
    public void switchingFunctions(){
        String inReleaseMode = (String)Functions.ReturnsDefaultValue.apply(s -> System.getProperty((String) s), "isRelease");
        Functions.Default = (inReleaseMode == null) ? Functions.ReturnsDefaultValue : Functions.ThrowsRuntimeException;
        BiFunctionThrowable<String[], Integer, Integer> ff = (sArray, index) -> sArray[index].length();
        Integer result = (Integer) Functions.Default.apply(ff, new String[]{"a", "ab"}, 1);
        assertEquals(Integer.valueOf(2), result);

        result = (Integer)Functions.Default.apply(ff, new String[]{null, null}, 1);
        assertEquals(Integer.valueOf(0), result);
    }
}