package io.github.cruisoring;

import io.github.cruisoring.function.ConsumerThrowable;
import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.logger.LogLevel;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.utility.StackTraceHelper;
import io.github.cruisoring.utility.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static io.github.cruisoring.Asserts.checkWithoutNull;

/**
 * Utility class to enable automatic revoking.
 * @param <T>   Type of the settings to be updated/revoked.
 */
public class Revokable<T> implements AutoCloseable {
    //Identifier to locate the caller stack trace quickly
    static final String getCallerStackTraceKey = Revokable.class.getSimpleName() + ".java";

    public static LogLevel DefualtLogLevel = LogLevel.debug;

    static final List<Revokable> All = new ArrayList<>();

    /**
     * Perform updating a value and register the revoking operation for later calling.
     * @param getter        accessor to retrieve the setting.
     * @param setter        accessor to change the setting.
     * @param newSetting    new setting to replace existing one.
     * @param <T>           type of the setting.
     */
    public static <T> void register(Supplier <T> getter, ConsumerThrowable<T> setter, T newSetting){
        Revokable<T> revokable = new Revokable(getter, setter, newSetting);
        if(revokable.revoker != null){
            All.add(revokable);
        }
    }

    /**
     * Register the revoking operation for later calling.
     * @param runnableThrowable operation to be called later.
     */
    public static void register(RunnableThrowable runnableThrowable){
        Revokable revokable = new Revokable(runnableThrowable);
        All.add(revokable);
    }

    /**
     * Revoke all registered operation in reversed order as they are registered.
     */
    public static void revokeAll(){
        for (int i = All.size()-1; i >= 0; i--) {
            Revokable revokable = All.get(i);
            if(revokable != null && !revokable.isClosed){
                revokable.close();
            }
            All.remove(i);
        }
    }
    

    final Long timeStamp;
    final String label;
    final T old;
    RunnableThrowable revoker;
    private boolean isClosed = false;

    public Revokable(Supplier <T> getter, ConsumerThrowable<T> setter, T newSetting){
        checkWithoutNull(getter);
        checkWithoutNull(setter);

        timeStamp = System.currentTimeMillis();
        StackTraceElement stack = StackTraceHelper.getCallerStackTrace(null, getCallerStackTraceKey);
        label = StringHelper.tryFormatString("%s(%s:%d)",
            stack.getMethodName(), stack.getFileName(), stack.getLineNumber());

        old = getter.get();
        try{
            setter.accept(newSetting);
            revoker = () -> setter.accept(old);
        }catch (Exception e){
            Logger.getDefault().log(DefualtLogLevel, e.getMessage());
        }
    }

    public Revokable(RunnableThrowable runnableThrowable){
        checkWithoutNull(runnableThrowable);

        timeStamp = System.currentTimeMillis();
        old = null;
        StackTraceElement stack = StackTraceHelper.getCallerStackTrace(null, getCallerStackTraceKey);
        label = StringHelper.tryFormatString("%s(%s:%d)",
            stack.getMethodName(), stack.getFileName(), stack.getLineNumber());
        revoker = runnableThrowable;
    }
    
    public T getValue(){
        return old;
    }

    /**
     * When value created, closing it and release any resource bounded if the instance is AutoCloseable.
     */
    public void closing() {
        if (!isClosed) {
            isClosed = true;
            try {
                revoker.run();
                Logger.getDefault().log(DefualtLogLevel, "%s is reverted.", label);
            } catch (Exception e) {
                Logger.getDefault().log(DefualtLogLevel, e);
            }
        }
    }

    @Override
    public void close() {
        closing();
    }
}
