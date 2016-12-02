package org.ilya.scheduler.schedule;

public interface RequestExecutor<T> {

    boolean execute(Action<Schedule<T>> action);

}
