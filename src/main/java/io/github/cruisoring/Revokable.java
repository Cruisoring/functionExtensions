package io.github.cruisoring;

import io.github.cruisoring.logger.LogLevel;
import io.github.cruisoring.logger.Logger;
import io.github.cruisoring.throwables.ConsumerThrowable;
import io.github.cruisoring.throwables.RunnableThrowable;
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

    //region Static members and methods
    //Identifier to locate the caller stack trace quickly
    static final String[] calledKeywords = new String[]{
        Revokable.class.getSimpleName() + ".java",
        Logger.class.getSimpleName() + ".java"
    };

    public static LogLevel DefaultLogLevel = LogLevel.verbose;

    //Indicates if the newSettings shall be closed if it is AutoCloseable when closing() is called so as to release its rescources
    public static boolean DefaultCloseNewSetting = true;

    static final List<Revokable> All = new ArrayList<>();

    /**
     * Perform updating a value and register the revoking operation for later calling.
     * @param getter        accessor to retrieve the setting.
     * @param setter        accessor to change the setting.
     * @param newSetting    new setting to replace existing one.
     * @param <T>           type of the setting.
     * @return      the {@code Revokable} instance which would revert the changes when closing.
     */
    public static <T> Revokable register(Supplier <T> getter, ConsumerThrowable<T> setter, T newSetting){
        Revokable<T> revokable = new Revokable(getter, setter, newSetting);
        if(revokable.revoker != null){
            All.add(revokable);
        }
        return revokable;
    }

    /**
     * Register the revoking operation for later calling.
     * @param runnableThrowable operation to be called later.
     * @return      the {@code Revokable} instance which would revert the changes when closing.
     */
    public static Revokable register(RunnableThrowable runnableThrowable){
        Revokable revokable = new Revokable(runnableThrowable);
        All.add(revokable);
        return revokable;
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
    //endregion

    //region Instance variables
    final LocalDateTime timeStamp;
    final String label;
    final T originalSetting;
    T newSetting;
    final RunnableThrowable revoker;
    private boolean isClosed = false;
    //endregion

    //region Constructors
    public Revokable(Supplier <T> getter, ConsumerThrowable<T> setter, T newSetting){
        checkWithoutNull(getter, setter);

        timeStamp = LocalDateTime.now();
        label = StackTraceHelper.getCallerLabel(null, calledKeywords);

        originalSetting = getter.get();
        this.newSetting = newSetting;
        boolean setSuccessfully = false;
        try{
            setter.accept(newSetting);
            setSuccessfully = true;
        }catch (Exception e){
            Logger.getDefault().log(DefaultLogLevel, "Failed to update setting: %s", e.getMessage());
        } finally {
            revoker = setSuccessfully ? () -> setter.accept(originalSetting) : null;
        }
    }

    public Revokable(RunnableThrowable runnableThrowable){
        checkWithoutNull(runnableThrowable);

        timeStamp = LocalDateTime.now();
        label = StackTraceHelper.getCallerLabel(null, calledKeywords);

        originalSetting = null;
        newSetting = null;
        revoker = runnableThrowable;
    }
    //endregion

    //region Instance methods
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

            if (revoker != null) {
                revoker.withHandler(Functions::logAndReturnsNull).run();
            }
            if (originalSetting == null && newSetting == null) {
                Logger.getDefault().log(DefaultLogLevel, "%s is reverted.", label);
            } else {
                Logger.getDefault().log(DefaultLogLevel, "%s is reverted from %s back to %s.",
                    label, newSetting, originalSetting);
            }
            if (newSetting != null && newSetting instanceof AutoCloseable) {
                if(closeNew) {
                    //Close the newSetting to release any resources associated
                    RunnableThrowable runnableThrowable = ((AutoCloseable) newSetting)::close;
                    runnableThrowable.withHandler(Functions::logAndReturnsNull).run();
                }
                newSetting = null;
            }
        }
    }

    @Override
    public void close() {
        closing(DefaultCloseNewSetting);
    }
    //endregion
}
