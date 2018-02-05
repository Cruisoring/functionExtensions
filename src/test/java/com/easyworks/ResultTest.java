package com.easyworks;

import com.easyworks.utilities.Result;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResultTest {
    @Test
    public void and() throws Exception {
        Result success = Result.Success;
        Result ok = new Result("OK");
        Result failure = Result.Failure;
        Result exception1 = new Result(new Exception("first error"));
        Result exception2 = new Result(new Exception("second error"));

        assertEquals(success, success.and(Result.Success));
        assertEquals(success, Result.Success.and(success));
        assertEquals(success, ok.and(Result.Success));
        assertEquals(ok, success.and(ok));

        assertEquals(failure, success.and(Result.Failure));
        assertEquals(failure, failure.and(success));
        assertEquals(failure, failure.and(exception1));
        assertEquals(exception1, exception1.and(failure));

        assertEquals(failure, success.and(Result.Failure));
        assertEquals(failure, failure.and(success));
        assertEquals(exception1, exception1.and(success));
        assertEquals(exception1, exception1.and(exception2));
    }

    @Test
    public void or() throws Exception {
        Result success = Result.Success;
        Result ok = new Result("OK");
        Result failure = Result.Failure;
        Result exception1 = new Result(new Exception("first error"));
        Result exception2 = new Result(new Exception("second error"));

        assertEquals(success, success.or(Result.Success));
        assertEquals(success, Result.Success.or(success));
        assertEquals(ok, ok.or(Result.Success));
        assertEquals(success, success.or(ok));

        assertEquals(success, success.or(Result.Failure));
        assertEquals(success, failure.or(success));
        assertEquals(ok, failure.or(ok));
        assertEquals(exception1, failure.or(exception1));
        assertEquals(exception2, exception1.or(exception2));
        assertEquals(ok, ok.or(exception2));
    }

}