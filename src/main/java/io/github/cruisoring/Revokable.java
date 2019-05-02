package io.github.cruisoring;

import io.github.cruisoring.function.ConsumerThrowable;
import io.github.cruisoring.function.RunnableThrowable;
import io.github.cruisoring.logger.LogLevel;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.utility.StackTraceHelper;

import java.time.LocalDateTime;
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

    public static LogLevel DefaultLogLevel = LogLevel.verbose;
    public static boolean DefaultCloseNew = false;

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


    final LocalDateTime timeStamp;
    final String label;
    final T originalSetting;
    final T newSetting;
    RunnableThrowable revoker;
    private boolean isClosed = false;

    public Revokable(Supplier <T> getter, ConsumerThrowable<T> setter, T newSetting){
        checkWithoutNull(getter, setter);

        timeStamp = LocalDateTime.now();
        label = StackTraceHelper.getCallerLabel(null);

        originalSetting = getter.get();
        this.newSetting = newSetting;
        try{
            setter.accept(newSetting);
            revoker = () -> setter.accept(originalSetting);
        }catch (Exception e){
            Logger.getDefault().log(DefaultLogLevel, "Failed to update setting: %s", e.getMessage());
        }
    }

    public Revokable(RunnableThrowable runnableThrowable){
        checkWithoutNull(runnableThrowable);

        timeStamp = LocalDateTime.now();
        label = StackTraceHelper.getCallerLabel(null);

        originalSetting = null;
        newSetting = null;
        revoker = runnableThrowable;
    }

    /**
     * Get the time when this {@code Revokable} was created.
     *
     * @return {@code LocalDateTime} instance when the {@code Revokable} was created.
     */
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    /**
     * Get the original setting of this {@code Revokable} that would be reverted when the {@code Revokable} is closed.
     *
     * @return the original setting of type <tt>T</tt>, or null when only RunnableThrowable is provided.
     */
    public T getOriginalSetting() {
        return originalSetting;
    }

    /**
     * Get the new setting of this {@code Revokable} that has been updated by this {@code Revokable}
     *
     * @return the new setting of type <tt>T</tt> updated by this {@code Revokable}, or null when only RunnableThrowable is provided.
     */
    public T getNewSetting() {
        return newSetting;
    }

    /**
     * Get the state of this {@code Revokable}
     *
     * @return <tt>true</tt> if this {@code Revokable} has been closed which means revoking action has been executed, otherwise <tt>false</tt>
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * When value created, closing it and release any resource bounded if the instance is AutoCloseable.
     * @param closeNew  indicates if the newSetting shall be closed, <tt>true</tt> would close it if it is AutoCloseable.
     */
    public void closing(boolean closeNew) {
        if (!isClosed) {
            isClosed = true;
            try {
                revoker.run();
                if (originalSetting == null && newSetting == null) {
                    Logger.getDefault().log(DefaultLogLevel, "%s is reverted.", label);
                } else {
                    Logger.getDefault().log(DefaultLogLevel, "%s is reverted from %s back to %s.",
                            label, newSetting, originalSetting);
                }
                if (closeNew && newSetting != null && newSetting instanceof AutoCloseable) {
                    ((AutoCloseable) newSetting).close();
                }
            } catch (Exception e) {
                Logger.getDefault().log(DefaultLogLevel, e);
            }
        }
    }

    @Override
    public void close() {
        closing(DefaultCloseNew);
    }
}
