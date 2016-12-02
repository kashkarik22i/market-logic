package org.ilya.scheduler.schedule;

public interface Action<T> {

    boolean apply(T object) throws Exception;

    void onFailure();

    void onSuccess();

}
